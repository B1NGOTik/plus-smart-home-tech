package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;

@Service
public class DeviceAddedEventService extends BaseHubService<DeviceAddedEventAvro> {
    public DeviceAddedEventService(ProducerClient producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEventProto event) {
        DeviceAddedEventProto deviceAddedEvent = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getType().name()))
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}
