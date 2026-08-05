package com.jobagent.common;

public enum ApplicationStatus {
    SCRAPED,     // Fetched in last 24h
    MATCHED,     // Evaluated > 75% score
    APPROVED,    // Approved by you via Telegram/UI
    APPLIED,     // Form submitted or completed
    REJECTED     // Filtered out or skipped
}