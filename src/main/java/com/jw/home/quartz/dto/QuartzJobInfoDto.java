package com.jw.home.quartz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class QuartzJobInfoDto {
    private String jobName;
    private String groupName;
    private String description;
    private String scheduledTime;
    private String lastFiredTime;
    private String nextFireTime;
    private String timezone;
    private String state;
    private String cronExpression;
}
