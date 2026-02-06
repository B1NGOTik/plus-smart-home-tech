package ru.yandex.practicum.telemetry.collector.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.hub.HubService;
import ru.yandex.practicum.telemetry.collector.service.sensor.SensorService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/events")
public class EventController {
    private final Map<SensorEventType, SensorService> sensorServices;
    private final Map<HubEventType, HubService> hubServices;

    public EventController(List<SensorService> sensorServiceList,
                           List<HubService> hubServiceList) {
        this.sensorServices = sensorServiceList.stream()
                .collect(Collectors.toMap(SensorService::getMessageType, Function.identity()));
        this.hubServices = hubServiceList.stream()
                .collect(Collectors.toMap(HubService::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void handleSensorMessage(@RequestBody SensorEvent event) {
        if (sensorServices.containsKey(event.getType())) {
            sensorServices.get(event.getType()).catchEvent(event);
        } else {
            throw new IllegalArgumentException("Не найден сервис обработки для события датчика");
        }
    }

    @PostMapping("/hubs")
    public void handleHubMessage(@RequestBody HubEvent event) {
        if (hubServices.containsKey(event.getType())) {
            hubServices.get(event.getType()).catchEvent(event);
        } else {
            throw new IllegalArgumentException("Не найден сервис обработки для события хаба");
        }
    }
}
