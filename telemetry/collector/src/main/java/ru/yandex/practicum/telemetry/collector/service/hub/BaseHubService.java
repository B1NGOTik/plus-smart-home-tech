package ru.yandex.practicum.telemetry.collector.service.hub;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.ProducerClient;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

@RequiredArgsConstructor
public abstract class BaseHubService<T extends SpecificRecordBase> implements HubService {
    protected final ProducerClient producer;
    protected String topic = "telemetry.hubs.v1";

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void catchEvent(HubEvent event) {
        if (!event.getType().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизвестный тип события: " + event.getType());
        }
        T payload = mapToAvro(event);

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
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
