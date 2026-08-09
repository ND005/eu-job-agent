package com.jobagent.repository;

import com.jobagent.common.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStatus(JobApplication.ApplicationStatus status);
    long countByStatus(JobApplication.ApplicationStatus status);
}