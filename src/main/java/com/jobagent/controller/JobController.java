package com.jobagent.controller;

import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import com.jobagent.drafter.ApplicationDraft;
import com.jobagent.drafter.ApplicationDrafterService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class JobController {

    private final JobPostingRepository jobPostingRepository;
    private final ApplicationDrafterService applicationDrafterService;

    public JobController(JobPostingRepository jobPostingRepository,
                         ApplicationDrafterService applicationDrafterService) {
        this.jobPostingRepository = jobPostingRepository;
        this.applicationDrafterService = applicationDrafterService;
    }

    // 1. Dashboard View
    @GetMapping("/")
    public String index(Model model) {
        List<JobPosting> jobs = jobPostingRepository.findAll();
        model.addAttribute("jobs", jobs);
        return "dashboard";
    }

    // 2. AI Application Pitch Draft Endpoint
    @PostMapping("/api/jobs/{id}/draft")
    @ResponseBody
    public ResponseEntity<ApplicationDraft> generateDraft(@PathVariable Long id) {
        ApplicationDraft draft = applicationDrafterService.createDraftForJob(id);
        return ResponseEntity.ok(draft);
    }
}