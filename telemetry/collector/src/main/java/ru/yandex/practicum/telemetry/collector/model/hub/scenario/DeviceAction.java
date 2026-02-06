package ru.yandex.practicum.telemetry.collector.model.hub.scenario;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DeviceAction {
    private String sensorId;
    private ScenarioActionType type;
    private int value;
}
