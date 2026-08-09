package com.jobagent.controller;

import com.jobagent.common.JobApplication;
import com.jobagent.common.JobPosting;
import com.jobagent.repository.JobApplicationRepository;
import com.jobagent.common.JobPostingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DashboardController {

    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public DashboardController(JobPostingRepository jobPostingRepository, 
                               JobApplicationRepository jobApplicationRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    // 1. All Jobs Dashboard View
    @GetMapping({"/", "/dashboard"})
    public String viewDashboard(Model model) {
        List<JobPosting> allJobs = jobPostingRepository.findAllByOrderByCreatedAtDesc();
        
        model.addAttribute("jobs", allJobs);
        model.addAttribute("totalScraped", allJobs.size());
        model.addAttribute("totalApplied", jobApplicationRepository.countByStatus(JobApplication.ApplicationStatus.APPLIED));
        model.addAttribute("totalReplies", jobApplicationRepository.countByStatus(JobApplication.ApplicationStatus.REPLY_RECEIVED));
        
        return "dashboard";
    }

    // 2. Applied Jobs Tracker View
    @GetMapping("/applications")
    public String viewApplications(Model model) {
        List<JobApplication> applications = jobApplicationRepository.findAll();
        model.addAttribute("applications", applications);
        model.addAttribute("statuses", JobApplication.ApplicationStatus.values());
        return "applications";
    }

    // 3. Move Job to "Applied" Status
    @PostMapping("/applications/track")
    public String trackApplication(@RequestParam("jobId") Long jobId, 
                                   @RequestParam(value = "template", required = false) String template) {
        JobPosting job = jobPostingRepository.findById(jobId).orElseThrow();
        
        if (job.getJobApplication() == null) {
            JobApplication app = new JobApplication();
            app.setJobPosting(job);
            app.setStatus(JobApplication.ApplicationStatus.APPLIED);
            app.setTemplateUsed(template != null ? template : "Default Germany Resume");
            jobApplicationRepository.save(app);
        }
        return "redirect:/applications";
    }

    // 4. Update Status (e.g., Change to Got Reply / Interview)
    @PostMapping("/applications/update-status")
    public String updateStatus(@RequestParam("appId") Long appId, 
                               @RequestParam("status") JobApplication.ApplicationStatus status) {
        JobApplication app = jobApplicationRepository.findById(appId).orElseThrow();
        app.setStatus(status);
        jobApplicationRepository.save(app);
        return "redirect:/applications";
    }

    // 5. Resume & Cover Letter Templates View
    @GetMapping("/templates")
    public String viewTemplates() {
        return "templates";
    }
}