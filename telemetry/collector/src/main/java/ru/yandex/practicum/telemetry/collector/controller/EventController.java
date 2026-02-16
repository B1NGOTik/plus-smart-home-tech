package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.hub.HubService;
import ru.yandex.practicum.telemetry.collector.service.sensor.SensorService;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorService> sensorServices;
    private final Map<HubEventProto.PayloadCase, HubService> hubServices;

    public EventController(Set<SensorService> sensorServices,
                           Set<HubService> hubServices) {
        this.sensorServices = sensorServices
                .stream()
                .collect(Collectors.toMap(
                        SensorService::getMessageType,
                        Function.identity()
                ));
        this.hubServices = hubServices
                .stream()
                .collect(Collectors.toMap(
                        HubService::getMessageType,
                        Function.identity()
                ));
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Получили событие от датчика: {}", request);
        try {
            if (sensorServices.containsKey(request.getPayloadCase())) {
                sensorServices.get(request.getPayloadCase()).catchEvent(request);
            } else {
                throw new IllegalArgumentException("Не найден обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Получили событие от хаба: {}", request);
        try {
            if (hubServices.containsKey(request.getPayloadCase())) {
                hubServices.get(request.getPayloadCase()).catchEvent(request);
            } else {
                throw new IllegalArgumentException("Не найден обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
