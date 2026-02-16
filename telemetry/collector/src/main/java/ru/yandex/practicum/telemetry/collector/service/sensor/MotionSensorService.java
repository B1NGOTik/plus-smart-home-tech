package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;

@Service
public class MotionSensorService extends BaseSensorService<MotionSensorEventAvro> {
    public MotionSensorService(ProducerClient producer) {
        super(producer);
    }

    @Override
    protected MotionSensorEventAvro mapToAvro(SensorEventProto event) {
        MotionSensorProto motionSensorEvent = event.getMotionSensorEvent();
        return MotionSensorEventAvro.newBuilder()
                .setMotion(motionSensorEvent.getMotion())
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .setVoltage(motionSensorEvent.getVoltage())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }


}
