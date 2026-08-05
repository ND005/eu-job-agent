package com.jobagent.controller;

import com.jobagent.fetcher.ArbeitnowFetcherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobFetcherController {

    private final ArbeitnowFetcherService fetcherService;

    public JobFetcherController(ArbeitnowFetcherService fetcherService) {
        this.fetcherService = fetcherService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<Map<String, Object>> fetchJobs() {
        int newJobs = fetcherService.fetchAndSaveJobs();
        return ResponseEntity.ok(Map.of(
            "message", "Job ingestion completed successfully",
            "newJobsSaved", newJobs
        ));
    }
}