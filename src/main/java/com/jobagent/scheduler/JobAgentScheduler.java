package com.jobagent.scheduler;

import com.jobagent.common.JobPosting;
import com.jobagent.fetcher.ArbeitnowFetcherService;
import com.jobagent.matcher.JobMatcherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobAgentScheduler {

    private final ArbeitnowFetcherService fetcherService;
    private final JobMatcherService matcherService;

    public JobAgentScheduler(ArbeitnowFetcherService fetcherService, JobMatcherService matcherService) {
        this.fetcherService = fetcherService;
        this.matcherService = matcherService;
    }

    /**
     * Automatically fetches new jobs every 6 hours
     */
    @Scheduled(cron = "0 0 */6 * * *")
    public void runJobIngestion() {
        List<JobPosting> newlySavedJobs = fetcherService.fetchAndSaveJobs();
        System.out.println("[Scheduler] Ingestion completed. Saved " + newlySavedJobs.size() + " new jobs.");

        // Automatically run scoring engine for newly ingested jobs
        if (!newlySavedJobs.isEmpty()) {
            List<JobPosting> scoredJobs = matcherService.processAndScoreJobs();
            System.out.println("[Scheduler] Scoring completed for " + scoredJobs.size() + " total jobs.");
        }
    }
}