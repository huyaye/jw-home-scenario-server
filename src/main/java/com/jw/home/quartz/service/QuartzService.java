package com.jw.home.quartz.service;

import com.jw.home.common.spec.ConditionDay;
import com.jw.home.domain.ScenarioCondition;
import com.jw.home.exception.QuartzException;
import com.jw.home.quartz.dto.QuartzJobInfoDto;
import com.jw.home.quartz.job.ScenarioActionJob;
import com.jw.home.quartz.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzService {
    private final Scheduler scheduler;

    public void addSchedule(String id, String description, ScenarioCondition condition, String timezone) throws ClassNotFoundException, SchedulerException, ParseException {
        @SuppressWarnings("unchecked")
        JobDetail jobDetail = JobBuilder
                .newJob((Class<? extends QuartzJobBean>) Class.forName(ScenarioActionJob.class.getName()))
                .withIdentity(id, "scenario")
                .withDescription(description)
                .build();
        Trigger trigger = createCronTrigger(id, condition, timezone);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void deleteSchedule(String scenarioId) {
        try {
            scheduler.deleteJob(new JobKey(scenarioId, "scenario"));
        } catch (SchedulerException e) {
            log.error("Failed to delete quartz job", e);
            throw QuartzException.INSTANCE;
        }
    }

    public List<QuartzJobInfoDto> getAllScheduledJob() {
        List<QuartzJobInfoDto> jobs = new ArrayList<>();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    @SuppressWarnings("unchecked")
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    if (triggers.size() == 0) {
                        continue;
                    }
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    CronTrigger trigger = (CronTrigger) triggers.get(0);

                    QuartzJobInfoDto jobInfo = QuartzJobInfoDto.builder()
                            .jobName(jobKey.getName())
                            .groupName(jobKey.getGroup())
                            .description(jobDetail.getDescription())
                            .timezone(trigger.getTimeZone().getDisplayName())
                            .scheduledTime(DateTimeUtils.toString(trigger.getStartTime()))
                            .lastFiredTime(DateTimeUtils.toString(trigger.getPreviousFireTime()))
                            .nextFireTime(DateTimeUtils.toString(trigger.getNextFireTime()))
                            .cronExpression(trigger.getCronExpression())
                            .state(scheduler.getTriggerState(trigger.getKey()).name())
                            .build();
                    jobs.add(jobInfo);
                }
            }
        } catch (SchedulerException e) {
            log.error("Failed to get quartz jobs.", e);
        }
        return jobs;
    }

    private Trigger createCronTrigger(String id, ScenarioCondition condition, String timezone) throws ParseException {
        String time = condition.getStartTime();
        String hour = time.substring(0, 2);
        String minute = time.substring(3, 5);
        String weekTime = condition.getDay().stream().map(ConditionDay::toString).collect(Collectors.joining(","));
        String cronExpression = String.format("0 %s %s ? * %s * ", minute, hour, weekTime);

        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(id);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setTimeZone(TimeZone.getTimeZone(timezone));
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }
}
