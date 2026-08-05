package com.jobagent.common;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_postings")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String externalJobId; // Prevents duplicate job scraping

    private String title;
    private String company;
    private String location;
    private String sourcePortal; // e.g., "Arbeitsagentur", "Adzuna"
    private String jobUrl;

    @Column(columnDefinition = "TEXT")
    private String rawDescription;

    private Integer matchScore;
    private Boolean visaSponsorship;
    
    private LocalDateTime postedAt;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExternalJobId() { return externalJobId; }
    public void setExternalJobId(String externalJobId) { this.externalJobId = externalJobId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSourcePortal() { return sourcePortal; }
    public void setSourcePortal(String sourcePortal) { this.sourcePortal = sourcePortal; }

    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }

    public String getRawDescription() { return rawDescription; }
    public void setRawDescription(String rawDescription) { this.rawDescription = rawDescription; }

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public Boolean getVisaSponsorship() { return visaSponsorship; }
    public void setVisaSponsorship(Boolean visaSponsorship) { this.visaSponsorship = visaSponsorship; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}