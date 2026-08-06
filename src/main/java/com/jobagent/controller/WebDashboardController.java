package com.jobagent.controller;

import com.jobagent.common.JobPostingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class WebDashboardController {

    private final JobPostingRepository jobPostingRepository;

    public WebDashboardController(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("jobs", jobPostingRepository.findAll());
        return "dashboard";
    }
}