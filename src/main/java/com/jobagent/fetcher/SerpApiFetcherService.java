package com.jobagent.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerpApiFetcherService {

    @Value("${serpapi.key:}")
    private String apiKey;

    private final JobPostingRepository jobPostingRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String SERPAPI_BASE_URL = "https://serpapi.com/search.json";

    public SerpApiFetcherService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Fetches jobs via SerpApi (Google Jobs) and saves unique listings into SQLite.
     */
    @Transactional
    public List<JobPosting> fetchAndSaveJobs(String keyword, String location) {
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("[SerpApiFetcher] Error: serpapi.key is missing in application.properties!");
            return Collections.emptyList();
        }

        // Build target Google Jobs query localized for Germany
        String requestUrl = UriComponentsBuilder.fromHttpUrl(SERPAPI_BASE_URL)
                .queryParam("engine", "google_jobs")
                .queryParam("q", keyword + " " + location)
                .queryParam("hl", "de")
                .queryParam("gl", "de")
                .queryParam("api_key", apiKey)
                .encode()
                .toUriString();

        try {
            String jsonResponse = restTemplate.getForObject(requestUrl, String.class);
            if (jsonResponse == null) return Collections.emptyList();

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode jobsNode = rootNode.path("jobs_results");

            if (!jobsNode.isArray() || jobsNode.isEmpty()) {
                System.out.println("[SerpApiFetcher] No jobs found for query: " + keyword + " in " + location);
                return Collections.emptyList();
            }

            List<JobPosting> scrapedJobs = new ArrayList<>();

            for (JsonNode jobNode : jobsNode) {
                String jobId = jobNode.path("job_id").asText("");
                if (jobId.isBlank()) continue;

                JobPosting job = new JobPosting();
                job.setExternalJobId("serpapi-" + jobId);
                job.setTitle(jobNode.path("title").asText("N/A"));
                job.setCompany(jobNode.path("company_name").asText("Unknown Company"));
                job.setLocation(jobNode.path("location").asText(location));

                // Original source portal indexed by Google (e.g. "Jaabz", "LinkedIn", "Indeed")
                String rawVia = jobNode.path("via").asText("Google Jobs");
                job.setSourcePortal(rawVia.replace("via ", "").trim());

                job.setRawDescription(jobNode.path("description").asText(""));

                // Extract application link
                JsonNode applyOptions = jobNode.path("apply_options");
                if (applyOptions.isArray() && !applyOptions.isEmpty()) {
                    job.setJobUrl(applyOptions.get(0).path("link").asText(""));
                } else {
                    job.setJobUrl("https://www.google.com/search?q=" + keyword + "+jobs");
                }

                job.setPostedAt(LocalDateTime.now());
                scrapedJobs.add(job);
            }

            // --- Batch Deduplication Check against SQLite ---
            Set<String> incomingIds = scrapedJobs.stream()
                    .map(JobPosting::getExternalJobId)
                    .collect(Collectors.toSet());

            Set<String> existingIds = jobPostingRepository.findExistingExternalIds(incomingIds);

            List<JobPosting> newJobsToSave = scrapedJobs.stream()
                    .filter(job -> !existingIds.contains(job.getExternalJobId()))
                    .collect(Collectors.toList());

            if (!newJobsToSave.isEmpty()) {
                System.out.println("[SerpApiFetcher] Successfully saved " + newJobsToSave.size() + " new job postings.");
                return jobPostingRepository.saveAll(newJobsToSave);
            }

        } catch (Exception e) {
            System.err.println("[SerpApiFetcher] Failed to fetch or parse jobs: " + e.getMessage());
        }

        return Collections.emptyList();
    }
}