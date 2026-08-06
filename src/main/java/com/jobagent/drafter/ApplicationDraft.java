package com.jobagent.drafter;

public class ApplicationDraft {

    private String coverLetter;
    private String keyHighlights;

    public ApplicationDraft() {}

    public ApplicationDraft(String coverLetter, String keyHighlights) {
        this.coverLetter = coverLetter;
        this.keyHighlights = keyHighlights;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getKeyHighlights() {
        return keyHighlights;
    }

    public void setKeyHighlights(String keyHighlights) {
        this.keyHighlights = keyHighlights;
    }
}