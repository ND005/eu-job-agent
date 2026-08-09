package com.jobagent.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobagent.matcher.JobMatcherService;

import java.util.Map;

@RestController
@RequestMapping("/api/matcher") // Changed base path to separate concerns
public class JobMatcherController {

    private final JobMatcherService jobMatcherService;

    public JobMatcherController(JobMatcherService jobMatcherService) {
        this.jobMatcherService = jobMatcherService;
    }

    // Endpoint dedicated to running the scoring/matching algorithm on existing jobs
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runJobMatching() {
        int processedCount = jobMatcherService.processAndScoreJobs();
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "jobsScored", processedCount
        ));
    }
}