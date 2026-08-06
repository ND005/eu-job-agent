package com.jobagent.controller;

import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import com.jobagent.matcher.JobMatcherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobMatcherController {

    private final JobMatcherService jobMatcherService;
    private final JobPostingRepository jobPostingRepository;

    public JobMatcherController(JobMatcherService jobMatcherService, JobPostingRepository jobPostingRepository) {
        this.jobMatcherService = jobMatcherService;
        this.jobPostingRepository = jobPostingRepository;
    }

    @PostMapping("/match")
    public Map<String, Object> matchJobs(@RequestParam(defaultValue = "5") int limit) {
        // Call evaluateUnscoredJobs (matching your JobMatcherService method name)
        int count = jobMatcherService.evaluateUnscoredJobs(limit);
        
        return Map.of(
            "jobsEvaluated", count,
            "message", "AI job matching completed"
        );
    }

    @GetMapping("/scored")
    public List<JobPosting> getScoredJobs() {
        return jobPostingRepository.findByMatchScoreIsNotNull();
    }
}