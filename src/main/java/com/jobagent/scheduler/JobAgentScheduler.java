package com.jobagent.scheduler;

import com.jobagent.fetcher.ArbeitnowFetcherService;
import com.jobagent.matcher.JobMatcherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobAgentScheduler {

    private final ArbeitnowFetcherService fetcherService;
    private final JobMatcherService matcherService;

    public JobAgentScheduler(ArbeitnowFetcherService fetcherService, JobMatcherService matcherService) {
        this.fetcherService = fetcherService;
        this.matcherService = matcherService;
    }

    /**
     * Runs 10 seconds after application startup,
     * then executes automatically every 6 hours (21,600,000 ms).
     */
    @Scheduled(initialDelay = 10000, fixedRate = 21600000)
    public void runAutomatedPipeline() {
        System.out.println("⏰ [Scheduler] Starting automated job fetch & evaluation pipeline...");
        
        try {
            int newJobs = fetcherService.fetchAndSaveJobs();
            System.out.println("⏰ [Scheduler] Fetch complete. Ingested new postings: " + newJobs);
            
            int scoredJobs = matcherService.evaluateUnscoredJobs(10);
            System.out.println("⏰ [Scheduler] Evaluation complete. Scored jobs: " + scoredJobs);
            
            System.out.println("✅ [Scheduler] Pipeline run finished successfully!");
        } catch (Exception e) {
            System.err.println("❌ [Scheduler] Error during scheduled execution: " + e.getMessage());
        }
    }
}