package com.example.chatbot.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsRegistry {
    private final AtomicInteger totalSessions = new AtomicInteger(0);
    private final AtomicInteger resolved = new AtomicInteger(0);
    private final AtomicInteger escalated = new AtomicInteger(0);
    private final AtomicLong totalResponseTimeMs = new AtomicLong(0);
    private final AtomicInteger responseCount = new AtomicInteger(0);

    public void incrementSessions() { totalSessions.incrementAndGet(); }
    public void incrementResolved() { resolved.incrementAndGet(); }
    public void incrementEscalated() { escalated.incrementAndGet(); }
    public void addResponseTime(long ms) { totalResponseTimeMs.addAndGet(ms); responseCount.incrementAndGet(); }

    public int getTotalSessions() { return totalSessions.get(); }
    public int getResolved() { return resolved.get(); }
    public int getEscalated() { return escalated.get(); }
    public long getAverageResponseTimeMs() {
        int count = responseCount.get();
        return count == 0 ? 0 : totalResponseTimeMs.get() / count;
    }
}


