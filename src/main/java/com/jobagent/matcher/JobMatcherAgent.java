package com.jobagent.matcher;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "openAiChatModel")
public interface JobMatcherAgent {
	@UserMessage("""
			You are an expert technical recruiter analyzing job postings for a Senior SDET / Quality Automation Engineer profile with expertise in Java, Selenium, Appium, Playwright, Cucumber BDD, and LLM/GenAI evaluation.

			Analyze the following job description and provide:
			1. matchScore: Integer from 0 to 70 indicating fit for a Senior SDET / Automation Engineer / Test Engineer / QA Automation.
			2. reasoning: A 2-3 sentence summary explaining key matching skills or reasons for a low score.
			3. requiredSkills: List of key skills mentioned in the job description Java,C# (Dot Net), Playwright, Selenium, Appium, Cucumber BDD ,Junit ,Rest Assured,
			   LLM/GenAI,Spring,Jira ,GitLab, CI/CD Pipelines,Selenium Grid, SQL, Telemetry processing, DeepEval, Confluence, Git, Grafana, Eclipse, VS Code etc.

			Job Description:
			{{description}}
			""")
	JobEvaluationResult evaluateJob(String description); // <-- Removed @V("description")
}