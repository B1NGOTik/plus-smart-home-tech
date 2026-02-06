package ru.yandex.practicum.telemetry.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface ProducerClient {
    Producer<String, SpecificRecordBase> getProducer();

    void stop();
}
