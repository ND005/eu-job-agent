package com.jobagent.matcher;

import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
// Make sure this matches your actual entity package!

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobMatcherService {

    private static final Logger log = LoggerFactory.getLogger(JobMatcherService.class);

    private final JobMatcherAgent jobMatcherAgent;
    private final JobPostingRepository jobPostingRepository;

    // Inject both the LLM agent and the database repository
    public JobMatcherService(JobMatcherAgent jobMatcherAgent, JobPostingRepository jobPostingRepository) {
        this.jobMatcherAgent = jobMatcherAgent;
        this.jobPostingRepository = jobPostingRepository;
    }

    public int evaluateUnscoredJobs(int limit) {
        // 1. Fetch unscored jobs from DB (Fixes 'unscoredJobs cannot be resolved')
        List<JobPosting> unscoredJobs = jobPostingRepository.findByMatchScoreIsNull(PageRequest.of(0, limit));
        
        int count = 0;

        // 2. Loop through the fetched list
        for (JobPosting job : unscoredJobs) {
            try {
                String description = job.getRawDescription();
                
                // Truncate raw description to save tokens
                if (description != null && description.length() > 3000) {
                    description = description.substring(0, 3000);
                }

                // Call LLM
                JobEvaluationResult result = jobMatcherAgent.evaluateJob(description);

                // Update entity with result
                if (result != null) {
                    job.setMatchScore(result.getMatchScore());
                    // Save back to database
                    jobPostingRepository.save(job);
                    count++;
                }

                // Pause for 4 seconds to stay under Groq TPM rate limits
                Thread.sleep(4000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error evaluating job ID {}: {}", job.getId(), e.getMessage());
            }
        }
        
        return count;
    }
}