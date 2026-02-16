package ru.yandex.practicum.telemetry.collector.service.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubService {
    void catchEvent(HubEventProto event);

    HubEventProto.PayloadCase getMessageType();
}
