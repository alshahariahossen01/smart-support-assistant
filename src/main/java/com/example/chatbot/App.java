package com.example.chatbot;

import com.example.chatbot.ai.AiClient;
import com.example.chatbot.ai.OpenAiClient;
import com.example.chatbot.automation.FollowUpService;
import com.example.chatbot.automation.IssueLoggerService;
import com.example.chatbot.automation.SchedulerService;
import com.example.chatbot.config.Config;
import com.example.chatbot.kb.KnowledgeBase;
import com.example.chatbot.metrics.MetricsRegistry;
import com.example.chatbot.nlp.EntityExtractor;
import com.example.chatbot.nlp.IntentDetector;
import com.example.chatbot.services.CRMService;
import com.example.chatbot.services.OrderService;
import com.example.chatbot.services.PaymentService;
import com.example.chatbot.session.SessionManager;
import com.example.chatbot.util.Sanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Config config = Config.load();
        MetricsRegistry metrics = new MetricsRegistry();
        KnowledgeBase kb = new KnowledgeBase();
        kb.loadDefaults();

        AiClient aiClient = new OpenAiClient(config);
        IntentDetector intentDetector = new IntentDetector();
        EntityExtractor entityExtractor = new EntityExtractor();
        CRMService crmService = new CRMService();
        OrderService orderService = new OrderService();
        PaymentService paymentService = new PaymentService();
        SchedulerService scheduler = new SchedulerService();
        FollowUpService followUpService = new FollowUpService();
        IssueLoggerService issueLogger = new IssueLoggerService();
        SessionManager sessions = new SessionManager();

        log.info("AI Support Chatbot started (console mode)");
        System.out.println("Welcome to the AI Support Chatbot! Type 'exit' to quit.");

        String sessionId = sessions.startSession();
        metrics.incrementSessions();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("You: ");
                String userInput = scanner.nextLine();
                if (userInput == null) continue;
                if ("exit".equalsIgnoreCase(userInput.trim())) {
                    break;
                }

                String input = Sanitizer.sanitize(userInput);
                Instant t0 = Instant.now();

                IntentDetector.Intent intent = intentDetector.detectIntent(input);
                EntityExtractor.Entities entities = entityExtractor.extract(input);

                String response;
                boolean resolved = true;

                switch (intent) {
                    case ORDER_STATUS -> {
                        Optional<String> orderId = entities.orderId();
                        if (orderId.isPresent()) {
                            response = orderService.getOrderStatus(orderId.get());
                        } else {
                            response = "Please provide your order number (e.g., 12345).";
                            resolved = false;
                        }
                    }
                    case REFUND_REQUEST -> {
                        Optional<String> orderId = entities.orderId();
                        response = orderId.map(paymentService::startRefund)
                                .orElse("I can help with a refund. What is your order number?");
                        resolved = orderId.isPresent();
                    }
                    case PRODUCT_INFO -> {
                        response = crmService.getProductInfo(entities.productKeyword().orElse("product"));
                    }
                    case SCHEDULE_APPOINTMENT -> {
                        response = scheduler.schedule(entities.datetimeText().orElse("a convenient time"));
                    }
                    case FAQ -> {
                        response = kb.answer(input).orElse("I couldn't find that in our FAQ.");
                        if (response.contains("couldn't")) resolved = false;
                    }
                    case UNKNOWN -> {
                        // Try KB first
                        Optional<String> kbAnswer = kb.answer(input);
                        if (kbAnswer.isPresent()) {
                            response = kbAnswer.get();
                            resolved = true;
                        } else {
                            // Fall back to AI
                            response = aiClient.respond(input, sessions.getContext(sessionId));
                            resolved = !response.toLowerCase().contains("unsure");
                        }
                    }
                    default -> {
                        response = aiClient.respond(input, sessions.getContext(sessionId));
                        resolved = !response.toLowerCase().contains("unsure");
                    }
                }

                sessions.appendToContext(sessionId, "User: " + input);
                sessions.appendToContext(sessionId, "Bot: " + response);

                Instant t1 = Instant.now();
                long elapsedMs = Duration.between(t0, t1).toMillis();
                metrics.addResponseTime(elapsedMs);
                if (resolved) metrics.incrementResolved(); else metrics.incrementEscalated();

                System.out.println("Bot: " + response);
                log.info("[session={}] intent={} resolved={} latencyMs={}", sessionId, intent, resolved, elapsedMs);

                // Simple automation: follow-up if unresolved
                if (!resolved) {
                    followUpService.sendFollowUp(sessionId, input);
                    issueLogger.logIssue(sessionId, input, "Unresolved");
                }
            }
        } catch (Exception e) {
            log.error("Fatal error in console loop", e);
        } finally {
            sessions.endSession(sessionId);
            log.info("Sessions: {} | Resolved: {} | Escalated: {} | AvgLatencyMs: {}", metrics.getTotalSessions(), metrics.getResolved(), metrics.getEscalated(), metrics.getAverageResponseTimeMs());
            System.out.println("Goodbye!");
        }
    }
}


