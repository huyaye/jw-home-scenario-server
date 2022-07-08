package com.jw.home.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jw.home.common.spec.ActionType;
import com.jw.home.common.spec.CommandType;
import com.jw.home.common.spec.ConditionType;
import com.jw.home.rest.validator.AddConditionConstraint;
import com.jw.home.rest.validator.DeviceCondition;
import com.jw.home.rest.validator.WeekCondition;
import com.jw.home.rest.validator.WeekIntervalCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class DeleteScenarioReq {
    @Size(min = 1, message = "at least 1 scenarioId")
    private List<String> scenarioIds;
}
