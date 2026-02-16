package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;

@Service
public class TemperatureSensorService extends BaseSensorService<TemperatureSensorEventAvro> {
    public TemperatureSensorService(ProducerClient producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorEventAvro mapToAvro(SensorEventProto event) {
        TemperatureSensorProto temperatureSensorEvent = event.getTemperatureSensorEvent();
        return TemperatureSensorEventAvro.newBuilder()
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
