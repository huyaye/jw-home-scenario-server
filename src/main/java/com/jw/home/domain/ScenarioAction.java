package com.jw.home.domain;

import com.jw.home.common.spec.ActionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ScenarioAction {
    private ActionType type;
    private String deviceId;
    private String command;
    private Map<String, Object> params;
}
