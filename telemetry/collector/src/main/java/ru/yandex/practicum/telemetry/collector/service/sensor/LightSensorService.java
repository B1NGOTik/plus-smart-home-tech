package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;

@Service
public class LightSensorService extends BaseSensorService<LightSensorEventAvro> {
    public LightSensorService(ProducerClient producer) {
        super(producer);
    }

    @Override
    protected LightSensorEventAvro mapToAvro(SensorEventProto event) {
        LightSensorProto lightSensorEvent = event.getLightSensorEvent();
        return LightSensorEventAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }
}
