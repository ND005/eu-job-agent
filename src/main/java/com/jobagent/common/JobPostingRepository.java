package com.jobagent.common; // Ensure this matches your package structure

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    // 1. Fixes the issue in ArbeitnowFetcherService
    boolean existsByExternalJobId(String externalJobId);

    // 2. Used by JobMatcherService for batch processing
    List<JobPosting> findByMatchScoreIsNull(Pageable pageable);
}