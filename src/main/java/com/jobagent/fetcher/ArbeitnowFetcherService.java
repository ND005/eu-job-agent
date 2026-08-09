package com.jobagent.fetcher;

import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArbeitnowFetcherService {

    private final JobPostingRepository jobPostingRepository;
    private final RestTemplate restTemplate;

    private static final String ARBEITNOW_API_URL = "https://www.arbeitnow.com/api/job-board-api";

    public ArbeitnowFetcherService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetches job listings from Arbeitnow API and performs batch duplicate checking
     * to prevent primary key / unique constraint violations in SQLite.
     */
    @Transactional
    public List<JobPosting> fetchAndSaveJobs() {
        Map<String, Object> response = restTemplate.getForObject(ARBEITNOW_API_URL, Map.class);

        if (response == null || !response.containsKey("data")) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> rawJobs = (List<Map<String, Object>>) response.get("data");
        List<JobPosting> scrapedJobs = new ArrayList<>();

        for (Map<String, Object> rawJob : rawJobs) {
            String slug = (String) rawJob.get("slug");
            if (slug == null || slug.isBlank()) continue;

            JobPosting job = new JobPosting();
            job.setExternalJobId("arbeitnow-" + slug);
            job.setTitle((String) rawJob.get("title"));
            job.setCompany((String) rawJob.get("company_name"));
            job.setLocation((String) rawJob.get("location"));
            job.setSourcePortal("Arbeitnow");
            job.setJobUrl((String) rawJob.get("url"));
            job.setRawDescription((String) rawJob.get("description"));

            // Check visa sponsorship indicator if present
            Boolean visa = (Boolean) rawJob.get("visa");
            job.setVisaSponsorship(visa != null ? visa : false);

            // Parse timestamp
            Number createdAtEpoch = (Number) rawJob.get("created_at");
            if (createdAtEpoch != null) {
                job.setPostedAt(LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(createdAtEpoch.longValue()), 
                        ZoneId.systemDefault()
                ));
            } else {
                job.setPostedAt(LocalDateTime.now());
            }

            scrapedJobs.add(job);
        }

        // --- Optimized Batch Check to avoid SQLite duplicates ---
        Set<String> incomingIds = scrapedJobs.stream()
                .map(JobPosting::getExternalJobId)
                .collect(Collectors.toSet());

        Set<String> existingIds = jobPostingRepository.findExistingExternalIds(incomingIds);

        List<JobPosting> newJobsToSave = scrapedJobs.stream()
                .filter(job -> !existingIds.contains(job.getExternalJobId()))
                .collect(Collectors.toList());

        if (!newJobsToSave.isEmpty()) {
            return jobPostingRepository.saveAll(newJobsToSave);
        }

        return Collections.emptyList();
    }
}