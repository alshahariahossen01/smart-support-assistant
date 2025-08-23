package com.example.chatbot.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<String, StringBuilder> sessionContexts = new ConcurrentHashMap<>();

    public String startSession() {
        String id = UUID.randomUUID().toString();
        sessionContexts.put(id, new StringBuilder());
        return id;
    }

    public void endSession(String id) {
        sessionContexts.remove(id);
    }

    public String getContext(String id) {
        return sessionContexts.getOrDefault(id, new StringBuilder()).toString();
    }

    public void appendToContext(String id, String line) {
        sessionContexts.computeIfAbsent(id, k -> new StringBuilder()).append(line).append('\n');
    }
}


