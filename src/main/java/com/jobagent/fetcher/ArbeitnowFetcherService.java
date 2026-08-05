package com.jobagent.fetcher;

import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class ArbeitnowFetcherService {

    private final JobPostingRepository repository;
    private final RestClient restClient;

    public ArbeitnowFetcherService(JobPostingRepository repository) {
        this.repository = repository;
        this.restClient = RestClient.create();
    }

    public int fetchAndSaveJobs() {
        String apiUrl = "https://www.arbeitnow.com/api/job-board-api";

        ArbeitnowResponseDto response = restClient.get()
                .uri(apiUrl)
                .retrieve()
                .body(ArbeitnowResponseDto.class);

        if (response == null || response.getData() == null) {
            return 0;
        }

        int newJobsSaved = 0;

        for (ArbeitnowJobDto dto : response.getData()) {
            // De-duplication check using unique slug
            if (!repository.existsByExternalJobId(dto.getSlug())) {
                JobPosting job = new JobPosting();
                job.setExternalJobId(dto.getSlug());
                job.setTitle(dto.getTitle());
                job.setCompany(dto.getCompanyName());
                job.setLocation(dto.getLocation());
                job.setSourcePortal("Arbeitnow");
                job.setJobUrl(dto.getUrl());
                job.setRawDescription(dto.getDescription());
                
                // Set visa status if specified
                boolean hasVisaSupport = "true".equalsIgnoreCase(dto.getVisaSponsorship());
                job.setVisaSponsorship(hasVisaSupport);
                
                job.setPostedAt(LocalDateTime.now());

                repository.save(job);
                newJobsSaved++;
            }
        }

        return newJobsSaved;
    }
}