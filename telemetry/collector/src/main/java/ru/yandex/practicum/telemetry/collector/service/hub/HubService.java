package ru.yandex.practicum.telemetry.collector.service.hub;

import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;

public interface HubService {
    void catchEvent(HubEvent event);

    HubEventType getMessageType();
}
