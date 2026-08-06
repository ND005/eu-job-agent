package com.jobagent.common;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    boolean existsByExternalJobId(String externalJobId);

    List<JobPosting> findByMatchScoreIsNull(Pageable pageable);

    // Add this line for the new GET endpoint:
    List<JobPosting> findByMatchScoreIsNotNull();
}