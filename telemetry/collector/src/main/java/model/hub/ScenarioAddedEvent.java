package model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.hub.scenario.ScenarioActionType;
import model.hub.scenario.ScenarioCondition;

import java.util.List;

@ToString(callSuper = true)
@Getter
@Setter
public class ScenarioAddedEvent extends BaseHubEvent{
    private String name;
    private List<ScenarioCondition> conditions;
    private List<ScenarioActionType> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
