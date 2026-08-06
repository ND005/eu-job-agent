package com.jobagent.drafter;

import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationDrafterService {

    private final ApplicationDrafterAgent drafterAgent;
    private final JobPostingRepository jobPostingRepository;

    public ApplicationDrafterService(ApplicationDrafterAgent drafterAgent, JobPostingRepository jobPostingRepository) {
        this.drafterAgent = drafterAgent;
        this.jobPostingRepository = jobPostingRepository;
    }

    public ApplicationDraft createDraftForJob(Long jobId) {
        Optional<JobPosting> optionalJob = jobPostingRepository.findById(jobId);
        if (optionalJob.isEmpty()) {
            throw new IllegalArgumentException("Job posting not found for ID: " + jobId);
        }

        JobPosting job = optionalJob.get();
        return drafterAgent.generateDraft(
                job.getTitle(),
                job.getCompany(),
                // Fix: use getRawDescription() instead of getDescription()
                job.getRawDescription() != null ? job.getRawDescription() : "Senior SDET / Automation Role"
        );
    }
}