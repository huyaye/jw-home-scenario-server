package com.jw.home.quartz.job;

import com.jw.home.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@DisallowConcurrentExecution
//@Component
public class ScenarioActionJob extends QuartzJobBean {

    @Autowired
    private ScenarioService scenarioService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        String scenarioId = context.getJobDetail().getKey().getName();
        log.info("Start scenario : {}", scenarioId);
        scenarioService.executeAction(scenarioId);
    }
}
