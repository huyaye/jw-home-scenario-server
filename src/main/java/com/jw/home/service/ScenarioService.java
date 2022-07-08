package com.jw.home.service;

import com.jw.home.common.spec.ConditionType;
import com.jw.home.domain.Scenario;
import com.jw.home.domain.ScenarioCondition;
import com.jw.home.exception.QuartzException;
import com.jw.home.quartz.service.QuartzService;
import com.jw.home.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final QuartzService quartzService;

    // TODO Transaction
    public String addScenario(Scenario scenario, String timezone) {
        scenario = scenarioRepository.save(scenario);
        List<ScenarioCondition> conditions = scenario.getConditions();
        for (ScenarioCondition condition : conditions) {
            if (condition.getType().equals(ConditionType.week)) {
                try {
                    quartzService.addSchedule(scenario.getId(), scenario.getName(), condition, timezone);
                } catch (Exception e) {
                    log.error("Quartz exception", e);
                    scenarioRepository.delete(scenario);
                    throw QuartzException.INSTANCE;
                }
            }
        }
        return scenario.getId();
    }

    public void executeAction(String scenarioId) {
        scenarioRepository.findById(scenarioId).ifPresent(scenario -> {
            // TODO
        });
    }

    public void deleteScenarios(List<String> scenarioIds) {
        List<String> deletedScenarioIds = new ArrayList<>();
        for (String scenarioId : scenarioIds) {
            quartzService.deleteSchedule(scenarioId);
        }
        scenarioRepository.deleteAllById(scenarioIds);
    }

    public Iterable<Scenario> getScenarios(List<String> scenarioIds) {
        return scenarioRepository.findAllById(scenarioIds);
    }
}
