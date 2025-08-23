package com.example.chatbot.api;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootLauncher {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootLauncher.class, args);
    }

    @Bean Config config() { return Config.load(); }
    @Bean MetricsRegistry metrics() { return new MetricsRegistry(); }
    @Bean KnowledgeBase kb() { KnowledgeBase kb = new KnowledgeBase(); kb.loadDefaults(); return kb; }
    @Bean IntentDetector intentDetector() { return new IntentDetector(); }
    @Bean EntityExtractor entityExtractor() { return new EntityExtractor(); }
    @Bean CRMService crmService() { return new CRMService(); }
    @Bean OrderService orderService() { return new OrderService(); }
    @Bean PaymentService paymentService() { return new PaymentService(); }
    @Bean SchedulerService schedulerService() { return new SchedulerService(); }
    @Bean FollowUpService followUpService() { return new FollowUpService(); }
    @Bean IssueLoggerService issueLoggerService() { return new IssueLoggerService(); }
    @Bean SessionManager sessionManager() { return new SessionManager(); }
    @Bean AiClient aiClient(Config config) { return new OpenAiClient(config); }
}


