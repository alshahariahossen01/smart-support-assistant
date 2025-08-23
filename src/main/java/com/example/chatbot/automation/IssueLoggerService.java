package com.example.chatbot.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueLoggerService {
    private static final Logger log = LoggerFactory.getLogger(IssueLoggerService.class);

    public void logIssue(String sessionId, String message, String summary) {
        log.warn("Issue logged [session={}]: {} | {}", sessionId, summary, message);
    }
}


