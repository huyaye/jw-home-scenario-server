package com.jw.home.domain.mapper;

import com.jw.home.domain.Scenario;
import com.jw.home.rest.dto.AddScenarioReq;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScenarioMapper {
    ScenarioMapper INSTANCE = Mappers.getMapper(ScenarioMapper.class);

    Scenario toScenario(AddScenarioReq dto);
}
