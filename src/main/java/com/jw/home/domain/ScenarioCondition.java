package com.jw.home.domain;

import com.jw.home.common.spec.ConditionDay;
import com.jw.home.common.spec.ConditionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ScenarioCondition {
    protected ConditionType type;
    private String startTime;
    private String endTime;
    private List<ConditionDay> day;
    private String deviceId;
}
