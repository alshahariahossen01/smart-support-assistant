package com.example.chatbot.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FollowUpService {
    private static final Logger log = LoggerFactory.getLogger(FollowUpService.class);

    public void sendFollowUp(String sessionId, String lastMessage) {
        log.info("Sending follow-up for session {}: {}", sessionId, lastMessage);
    }
}


