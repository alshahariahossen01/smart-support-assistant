package com.example.chatbot.ai;

import com.example.chatbot.config.Config;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OpenAiClient implements AiClient {
    private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);

    private final Config config;
    private final OkHttpClient http = new OkHttpClient();

    public OpenAiClient(Config config) {
        this.config = config;
    }

    @Override
    public String respond(String userInput, String conversationContext) {
        if (config.openAiApiKey == null || config.openAiApiKey.isBlank()) {
            return heuristicResponse(userInput);
        }

        try {
            String body = "{\n" +
                    "  \"model\": \"" + config.openAiModel + "\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"system\", \"content\": \"You are a helpful customer support assistant. Use the context if provided.\"},\n" +
                    "    {\"role\": \"system\", \"content\": \"Context: " + escape(conversationContext) + "\"},\n" +
                    "    {\"role\": \"user\", \"content\": \"" + escape(userInput) + "\"}\n" +
                    "  ]\n" +
                    "}";

            Request request = new Request.Builder()
                    .url(config.openAiBaseUrl + "/chat/completions")
                    .header("Authorization", "Bearer " + config.openAiApiKey)
                    .header("Content-Type", "application/json")
                    .post(RequestBody.create(body, MediaType.parse("application/json")))
                    .build();

            try (Response response = http.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.warn("OpenAI API failure: {}", response.code());
                    return heuristicResponse(userInput);
                }
                String resp = response.body() != null ? response.body().string() : "";
                // Very lightweight extraction to avoid full JSON mapping
                int idx = resp.indexOf("\"content\":");
                if (idx >= 0) {
                    String cut = resp.substring(idx + 10);
                    int start = cut.indexOf('"');
                    int end = cut.indexOf('"', start + 1);
                    if (start >= 0 && end > start) {
                        return cut.substring(start + 1, end).replace("\\n", "\n");
                    }
                }
                return "I'm here to help. Could you please rephrase that?";
            }
        } catch (IOException e) {
            log.error("Error calling OpenAI", e);
            return heuristicResponse(userInput);
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String heuristicResponse(String userInput) {
        String lower = userInput.toLowerCase();
        if (lower.contains("order")) return "To check order status, please share your order number.";
        if (lower.contains("refund")) return "You can request a refund within 30 days. What is your order number?";
        if (lower.contains("return")) return "I can help with returns. Do you have the order number?";
        if (lower.contains("product")) return "Which product are you interested in?";
        return "I'm not sure, but I'll learn from this. Could you add more details?";
    }
}


