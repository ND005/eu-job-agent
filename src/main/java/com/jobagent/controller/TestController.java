package com.jobagent.controller;

import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final JobPostingRepository repository;

    public TestController(JobPostingRepository repository) {
        this.repository = repository;
    }

    // Test 1: Check if REST Controller scanning works
    @GetMapping("/ping")
    public String ping() {
        return "EU Job Agent Backend is up and running!";
    }

    // Test 2: Check if SQLite Database persistence works
    @GetMapping("/db-check")
    public List<JobPosting> testDatabase() {
        JobPosting dummyJob = new JobPosting();
        dummyJob.setExternalJobId("TEST-" + System.currentTimeMillis());
        dummyJob.setTitle("Senior SDET / Quality Automation Engineer");
        dummyJob.setCompany("TechCorp Germany GmbH");
        dummyJob.setLocation("Berlin / Remote");
        dummyJob.setSourcePortal("Arbeitsagentur");
        dummyJob.setMatchScore(85);
        dummyJob.setVisaSponsorship(true);
        dummyJob.setPostedAt(LocalDateTime.now());

        // Save to SQLite
        repository.save(dummyJob);

        // Fetch all jobs back from SQLite
        return repository.findAll();
    }
}