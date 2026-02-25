package ru.yandex.practicum.telemetry.collector.service.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorService {
    void catchEvent(SensorEventProto event);

    SensorEventProto.PayloadCase getMessageType();
}
