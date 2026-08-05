package com.jobagent.matcher;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;


@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "openAiChatModel")
public interface JobMatcherAgent {

	@SystemMessage("""
	        You are an expert technical recruiter evaluating job descriptions for a Senior SDET position.
	        Compare the job requirements with the candidate profile and output the evaluation result.
	        """)
	    JobEvaluationResult evaluateJob(@UserMessage String jobDescription);
}