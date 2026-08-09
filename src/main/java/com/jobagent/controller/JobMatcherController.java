package com.jobagent.controller;

import com.jobagent.common.JobPosting;
import com.jobagent.fetcher.ArbeitnowFetcherService;
import com.jobagent.matcher.JobMatcherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobMatcherController {

    private final JobMatcherService jobMatcherService;
    private final ArbeitnowFetcherService arbeitnowFetcherService;

    public JobMatcherController(JobMatcherService jobMatcherService, 
                                ArbeitnowFetcherService arbeitnowFetcherService) {
        this.jobMatcherService = jobMatcherService;
        this.arbeitnowFetcherService = arbeitnowFetcherService;
    }

    /**
     * Triggers Arbeitnow scraper sync manually via API
     */
    @PostMapping("/fetch/arbeitnow")
    public ResponseEntity<Map<String, Object>> fetchArbeitnowJobs() {
        List<JobPosting> newlySaved = arbeitnowFetcherService.fetchAndSaveJobs();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("newJobsFetched", newlySaved.size());
        response.put("jobs", newlySaved);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Triggers evaluation and match scoring for unscored jobs
     */
    @PostMapping("/process-scores")
    public ResponseEntity<Map<String, Object>> processScores() {
        List<JobPosting> processedJobs = jobMatcherService.processAndScoreJobs();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalProcessed", processedJobs.size());
        
        return ResponseEntity.ok(response);
    }
}