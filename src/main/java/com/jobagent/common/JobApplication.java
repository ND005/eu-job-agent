package com.jobagent.common;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private LocalDate appliedDate = LocalDate.now();

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String templateUsed; // e.g., "Germany Tech Resume - Senior SDET"

    public enum ApplicationStatus {
        SAVED,
        APPLIED,
        REPLY_RECEIVED,
        INTERVIEW_SCHEDULED,
        OFFER,
        REJECTED
    }

    public JobApplication() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JobPosting getJobPosting() { return jobPosting; }
    public void setJobPosting(JobPosting jobPosting) { this.jobPosting = jobPosting; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDate getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getTemplateUsed() { return templateUsed; }
    public void setTemplateUsed(String templateUsed) { this.templateUsed = templateUsed; }
}