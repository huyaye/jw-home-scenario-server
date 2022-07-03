package com.jw.home.quartz.controller;

import com.jw.home.quartz.dto.QuartzJobInfoDto;
import com.jw.home.quartz.service.QuartzService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuartzWebController {
    private final QuartzService quartzService;

    @GetMapping("/jobs")
    public String index(Model model){
        List<QuartzJobInfoDto> jobList = quartzService.getAllScheduledJob();
        model.addAttribute("jobs", jobList);
        return "index";
    }
}
