package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;

@Service
public class DeviceRemovedEventService extends BaseHubService<DeviceRemovedEventAvro> {
    public DeviceRemovedEventService(ProducerClient producer) {
        super(producer);
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEventProto event) {
        DeviceRemovedEventProto deviceRemovedEvent = event.getDeviceRemoved();
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }
}
