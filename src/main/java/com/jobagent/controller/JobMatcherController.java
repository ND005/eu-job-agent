package com.jobagent.controller;

import com.jobagent.matcher.JobMatcherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobMatcherController {

    private final JobMatcherService matcherService;

    public JobMatcherController(JobMatcherService matcherService) {
        this.matcherService = matcherService;
    }

    @PostMapping("/match")
    public ResponseEntity<Map<String, Object>> evaluateJobs(@RequestParam(defaultValue = "10") int limit) {
        int evaluated = matcherService.evaluateUnscoredJobs(limit);
        return ResponseEntity.ok(Map.of(
            "message", "AI job matching completed",
            "jobsEvaluated", evaluated
        ));
    }
}