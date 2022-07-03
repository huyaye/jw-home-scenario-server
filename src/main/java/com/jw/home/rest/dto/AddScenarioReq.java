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
public class AddScenarioReq {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean enable;
    @NotEmpty
    private String homeId;
    @Valid
    @Size(min = 1, message = "at least 1 condition")
    private List<AddConditionDto> conditions;
    @Size(min = 1, message = "at least 1 action")
    private List<AddActionDto> actions;

    @Getter
    @Setter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AddConditionConstraint
    public static class AddConditionDto {
        @NotNull
        private ConditionType type;
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]", groups = { WeekCondition.class, WeekIntervalCondition.class })
        @Null(groups = DeviceCondition.class)
        private String startTime;
        @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]", groups = WeekIntervalCondition.class)
        @Null(groups = { WeekCondition.class, DeviceCondition.class })
        private String endTime;
        @Size(min = 1, groups = { WeekCondition.class, WeekIntervalCondition.class })
        @Null(groups = DeviceCondition.class)
        private List<String> day;
        @NotEmpty(groups = DeviceCondition.class)
        @Null(groups = { WeekCondition.class, WeekIntervalCondition.class })
        private String deviceId;
    }

    @Getter
    @Setter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AddActionDto {
        @NotNull
        private ActionType type;
        @NotEmpty
        private String deviceId;
        @NotNull
        private CommandType command;
        @NotNull
        private Map<String, Object> params;
    }
}
