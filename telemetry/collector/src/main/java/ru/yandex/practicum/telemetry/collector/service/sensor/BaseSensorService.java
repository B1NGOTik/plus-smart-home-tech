package ru.yandex.practicum.telemetry.collector.service.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

@RequiredArgsConstructor
public abstract class BaseSensorService<T extends SpecificRecordBase> implements SensorService {
    protected final ProducerClient producer;
    protected String topic = "telemetry.sensors.v1";

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void catchEvent(SensorEvent event) {
        if (!event.getType().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизвестный тип события: " + event.getType());
        }
        T payload = mapToAvro(event);

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                event.getTimestamp().toEpochMilli(),
                eventAvro.getHubId(),
                eventAvro
        );

        producer.getProducer().send(record);
    }
}
