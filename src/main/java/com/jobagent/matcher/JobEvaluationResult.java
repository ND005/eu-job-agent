package com.jobagent.matcher;

import java.util.List;

public class JobEvaluationResult {

	private int matchScore; // 0 - 100
	private List<String> requiredSkills;
	private String primaryLanguage; // "English", "German", or "Both"
	private boolean visaSponsorshipMentioned;
	private String fitSummary; // Brief reason for the score
	private String reasoning;

	// Getters and Setters

	public List<String> getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(List<String> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public boolean isVisaSponsorshipMentioned() {
		return visaSponsorshipMentioned;
	}

	public void setVisaSponsorshipMentioned(boolean visaSponsorshipMentioned) {
		this.visaSponsorshipMentioned = visaSponsorshipMentioned;
	}

	public String getFitSummary() {
		return fitSummary;
	}

	public void setFitSummary(String fitSummary) {
		this.fitSummary = fitSummary;
	}

	public JobEvaluationResult() {
	}

	public JobEvaluationResult(int matchScore, String reasoning) {
		this.matchScore = matchScore;
		this.reasoning = reasoning;
	}

	public int getMatchScore() {
		return matchScore;
	}

	public void setMatchScore(int matchScore) {
		this.matchScore = matchScore;
	}

	public String getReasoning() {
		return reasoning;
	}

	public void setReasoning(String reasoning) {
		this.reasoning = reasoning;
	}

}