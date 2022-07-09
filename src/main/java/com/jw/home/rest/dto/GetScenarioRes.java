package com.jw.home.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jw.home.common.spec.ActionType;
import com.jw.home.common.spec.CommandType;
import com.jw.home.common.spec.ConditionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetScenarioRes {
    private List<GetScenarioDto> scenarios = new ArrayList<>();

    @Getter
    @Setter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GetScenarioDto {
        private String name;
        private String description;
        private Boolean enable;
        private String homeId;
        private List<GetConditionDto> conditions;
        private List<GetActionDto> actions;
    }

    @Getter
    @Setter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GetConditionDto {
        private ConditionType type;
        private String startTime;
        private String endTime;
        private List<String> day;
        private String deviceId;
    }

    @Getter
    @Setter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GetActionDto {
        private ActionType type;
        private String deviceId;
        private CommandType command;
        private Map<String, Object> params;
    }

    public void add(GetScenarioDto getScenarioDto) {
        scenarios.add(getScenarioDto);
    }
}
