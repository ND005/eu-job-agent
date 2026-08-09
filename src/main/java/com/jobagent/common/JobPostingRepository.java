package com.jobagent.common;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    
    // Efficient batch check to avoid N+1 queries during job scraping
    @Query("SELECT j.externalJobId FROM JobPosting j WHERE j.externalJobId IN :ids")
    Set<String> findExistingExternalIds(@Param("ids") Set<String> ids);

    List<JobPosting> findAllByOrderByCreatedAtDesc();
}