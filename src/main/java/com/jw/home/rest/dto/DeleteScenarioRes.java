package com.jw.home.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DeleteScenarioRes {
    private List<String> scenarioIds;
}
