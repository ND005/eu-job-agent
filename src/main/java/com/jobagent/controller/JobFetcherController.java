package com.jobagent.controller;

import com.jobagent.common.JobPosting;
import com.jobagent.fetcher.ArbeitnowFetcherService;
import com.jobagent.fetcher.SerpApiFetcherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobFetcherController {

    private final ArbeitnowFetcherService arbeitnowFetcherService;
    private final SerpApiFetcherService serpApiFetcherService;

    public JobFetcherController(ArbeitnowFetcherService arbeitnowFetcherService,
                                SerpApiFetcherService serpApiFetcherService) {
        this.arbeitnowFetcherService = arbeitnowFetcherService;
        this.serpApiFetcherService = serpApiFetcherService;
    }

    // Existing Arbeitnow endpoint
    @PostMapping("/fetch/arbeitnow")
    public ResponseEntity<Map<String, Object>> fetchArbeitnow() {
        List<JobPosting> newJobs = arbeitnowFetcherService.fetchAndSaveJobs();
        return ResponseEntity.ok(Map.of(
            "message", "Arbeitnow ingestion completed",
            "newJobsSaved", newJobs.size()
        ));
    }

    // New Google Jobs / SerpApi endpoint (covers Jaabz, Indeed, XING, LinkedIn, etc.)
    @PostMapping("/fetch/google")
    public ResponseEntity<Map<String, Object>> fetchGoogleJobs(
            @RequestParam(defaultValue = "SDET") String keyword,
            @RequestParam(defaultValue = "Germany") String location) {

        List<JobPosting> newJobs = serpApiFetcherService.fetchAndSaveJobs(keyword, location);

        return ResponseEntity.ok(Map.of(
            "message", "Google Jobs ingestion completed",
            "newJobsSaved", newJobs.size()
        ));
    }
}