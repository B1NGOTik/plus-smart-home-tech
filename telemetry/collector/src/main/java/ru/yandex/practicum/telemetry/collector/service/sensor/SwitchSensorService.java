package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;

@Service
public class SwitchSensorService extends BaseSensorService<SwitchSensorEventAvro> {
    public SwitchSensorService(ProducerClient producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorEventAvro mapToAvro(SensorEventProto event) {
        SwitchSensorProto switchSensorEvent = event.getSwitchSensorEvent();
        return SwitchSensorEventAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }
}
