package com.jobagent.matcher;
import com.jobagent.common.JobPosting;
import com.jobagent.common.JobPostingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobMatcherService {

    private final JobPostingRepository jobPostingRepository;

    public JobMatcherService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    /**
     * Evaluates all un-scored job postings in the database, calculates a match score,
     * and saves the updated postings.
     */
    @Transactional
    public List<JobPosting> processAndScoreJobs() {
        List<JobPosting> allJobs = jobPostingRepository.findAll();

        for (JobPosting job : allJobs) {
            // Only calculate match score if it hasn't been set yet
            if (job.getMatchScore() == null) {
                int score = calculateMatchScore(job);
                job.setMatchScore(score);
                
                // Add automated reasoning based on the score
                if (score >= 80) {
                    job.setReasoning("High alignment with your target skills and preferences.");
                } else if (score >= 50) {
                    job.setReasoning("Moderate alignment. Review requirements manually.");
                } else {
                    job.setReasoning("Low match score. May require additional qualification checks.");
                }
            }
        }

        return jobPostingRepository.saveAll(allJobs);
    }

    /**
     * Rules-based heuristic engine for calculating match percentage.
     */
    private int calculateMatchScore(JobPosting job) {
        int score = 50; // Base score

        String title = job.getTitle() != null ? job.getTitle().toLowerCase() : "";
        String description = job.getRawDescription() != null ? job.getRawDescription().toLowerCase() : "";

        // Keyword checks for target roles
        if (title.contains("sdet") || title.contains("automation") || title.contains("quality engineer")) {
            score += 25;
        } else if (title.contains("qa") || title.contains("test")) {
            score += 15;
        }

        // Framework and language checks
        if (description.contains("selenium") || description.contains("playwright") || description.contains("appium")) {
            score += 10;
        }
        if (description.contains("java") || description.contains("c#")) {
            score += 10;
        }
        if (description.contains("jenkins") || description.contains("cicd") || description.contains("ci/cd")) {
            score += 5;
        }

        // Cap score at 100
        return Math.min(score, 100);
    }
}