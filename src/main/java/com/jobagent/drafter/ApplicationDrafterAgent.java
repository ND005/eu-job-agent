package com.jobagent.drafter;

import dev.langchain4j.service.UserMessage;

public interface ApplicationDrafterAgent {

    @UserMessage("""
        You are a career consultant drafting job application materials for a {{title}}.
        
        Candidate Profile:
        - Experience in Web,API & Mobile E2E Automation using Java, C#, Selenium, Appium, Playwright, and Cucumber BDD.
        - Expertise in CI/CD Jenkins freestyle/declarative pipelines and automated test execution.
        - Experience in GenAI & LLM evaluation (e.g., DeepEval, model benchmarking, telemetry data validation).
        - Deep understanding of ISTQB principles, software quality metrics, and test strategy design.
        
        Task:
        Analyze the following Job Description and generate a tailored application package:
        1. coverLetter: A concise (3-paragraph), professional cover letter emphasizing directly relevant frameworks, tools, and GenAI/LLM testing experience matching the job description.
        2. keyHighlights: 3 strong, high-impact bullet points customized for a resume summary or intro message to the recruiter.
        
        Job Title: {{title}}
        Company: {{company}}
        Job Description:
        {{description}}
        """)
    ApplicationDraft generateDraft(String title, String company, String description);
}