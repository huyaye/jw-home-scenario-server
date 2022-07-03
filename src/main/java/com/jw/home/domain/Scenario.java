package com.jw.home.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "scenario")
public class Scenario {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean enable;
    private String homeId;
    private List<ScenarioCondition> conditions;
    private List<ScenarioAction> actions;
}
