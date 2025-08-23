package com.example.chatbot.api;

import com.example.chatbot.ai.AiClient;
import com.example.chatbot.kb.KnowledgeBase;
import com.example.chatbot.metrics.MetricsRegistry;
import com.example.chatbot.nlp.EntityExtractor;
import com.example.chatbot.nlp.IntentDetector;
import com.example.chatbot.services.CRMService;
import com.example.chatbot.services.OrderService;
import com.example.chatbot.services.PaymentService;
import com.example.chatbot.session.SessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final AiClient ai;
    private final KnowledgeBase kb;
    private final MetricsRegistry metrics;
    private final IntentDetector intents;
    private final EntityExtractor entities;
    private final OrderService orders;
    private final PaymentService payments;
    private final CRMService crm;
    private final SessionManager sessions;

    public ChatController(AiClient ai, KnowledgeBase kb, MetricsRegistry metrics, IntentDetector intents, EntityExtractor entities, OrderService orders, PaymentService payments, CRMService crm, SessionManager sessions) {
        this.ai = ai; this.kb = kb; this.metrics = metrics; this.intents = intents; this.entities = entities; this.orders = orders; this.payments = payments; this.crm = crm; this.sessions = sessions;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "");
        String sessionId = payload.getOrDefault("sessionId", sessions.startSession());
        Instant t0 = Instant.now();

        IntentDetector.Intent intent = intents.detectIntent(message);
        EntityExtractor.Entities ents = entities.extract(message);
        String response;
        switch (intent) {
            case ORDER_STATUS -> response = ents.orderId().map(orders::getOrderStatus).orElse("Please provide your order number.");
            case REFUND_REQUEST -> response = ents.orderId().map(payments::startRefund).orElse("I can start a refund. What's your order number?");
            case PRODUCT_INFO -> response = crm.getProductInfo(ents.productKeyword().orElse("product"));
            case FAQ -> response = kb.answer(message).orElse("I couldn't find that in our FAQ.");
            default -> response = ai.respond(message, sessions.getContext(sessionId));
        }

        sessions.appendToContext(sessionId, "User: " + message);
        sessions.appendToContext(sessionId, "Bot: " + response);

        long ms = Duration.between(t0, Instant.now()).toMillis();
        metrics.addResponseTime(ms);

        return ResponseEntity.ok(Map.of("sessionId", sessionId, "response", response, "latencyMs", ms));
    }
}


