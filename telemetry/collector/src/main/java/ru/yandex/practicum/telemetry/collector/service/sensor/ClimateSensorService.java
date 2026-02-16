package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;

@Component
public class ClimateSensorService extends BaseSensorService<ClimateSensorEventAvro> {
    public ClimateSensorService(ProducerClient producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorEventAvro mapToAvro(SensorEventProto event) {
        ClimateSensorProto payload = event.getClimateSensorEvent();
        return ClimateSensorEventAvro.newBuilder()
                .setTemperatureC(payload.getTemperatureC())
                .setHumidity(payload.getHumidity())
                .setCo2Level(payload.getCo2Level())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }
}
