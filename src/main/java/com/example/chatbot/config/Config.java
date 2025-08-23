package com.example.chatbot.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    public final String openAiApiKey;
    public final String openAiBaseUrl;
    public final String openAiModel;

    private Config(String key, String base, String model) {
        this.openAiApiKey = key;
        this.openAiBaseUrl = base;
        this.openAiModel = model;
    }

    public static Config load() {
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            String key = getenvOr(dotenv, "OPENAI_API_KEY", null);
            String base = getenvOr(dotenv, "OPENAI_API_BASE", "https://api.openai.com/v1");
            String model = getenvOr(dotenv, "OPENAI_MODEL", "gpt-3.5-turbo");
            if (key == null || key.isBlank()) {
                log.warn("OPENAI_API_KEY not set. Falling back to heuristic responses and KB.");
            }
            return new Config(key, base, model);
        } catch (Exception e) {
            log.error("Failed to load config; using defaults", e);
            return new Config(null, "https://api.openai.com/v1", "gpt-3.5-turbo");
        }
    }

    private static String getenvOr(Dotenv dotenv, String name, String deflt) {
        String v = System.getenv(name);
        if (v != null) return v;
        v = dotenv.get(name, deflt);
        return v;
    }
}


