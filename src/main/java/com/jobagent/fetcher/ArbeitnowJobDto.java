package com.jobagent.fetcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ArbeitnowJobDto {

    private String slug;
    
    @JsonProperty("company_name")
    private String companyName;
    
    private String title;
    private String description;
    private String location;
    private String url;
    
    @JsonProperty("visa_sponsorship")
    private String visaSponsorship;

    private List<String> tags;

    // Getters and Setters
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getVisaSponsorship() { return visaSponsorship; }
    public void setVisaSponsorship(String visaSponsorship) { this.visaSponsorship = visaSponsorship; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}