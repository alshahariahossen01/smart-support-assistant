package com.example.chatbot.kb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;

public class KnowledgeBase {
    private final Map<String, String> questionToAnswer = new HashMap<>();
    private final Map<String, String> keywordToAnswer = new HashMap<>();

    public void loadDefaults() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("kb/faq.json")) {
            if (is == null) return;
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> list = mapper.readValue(is, new TypeReference<>(){});
            for (Map<String, Object> row : list) {
                String q = Objects.toString(row.get("question"), "");
                String a = Objects.toString(row.get("answer"), "");
                questionToAnswer.put(q.toLowerCase(Locale.ROOT), a);
                String qLower = q.toLowerCase(Locale.ROOT);
                if (qLower.contains("shipping")) keywordToAnswer.put("shipping", a);
                if (qLower.contains("refund")) keywordToAnswer.put("refund", a);
                if (qLower.contains("return")) keywordToAnswer.put("return", a);
                if (qLower.contains("contact")) keywordToAnswer.put("contact", a);
            }
        } catch (Exception ignored) { }
    }

    public Optional<String> answer(String userInput) {
        if (userInput == null || userInput.isBlank()) return Optional.empty();
        String lower = userInput.toLowerCase(Locale.ROOT);
        // Exact / contains matching
        for (Map.Entry<String, String> e : questionToAnswer.entrySet()) {
            if (lower.contains(e.getKey().substring(0, Math.min(6, e.getKey().length())))) {
                return Optional.ofNullable(e.getValue());
            }
        }
        for (Map.Entry<String, String> e : keywordToAnswer.entrySet()) {
            if (lower.contains(e.getKey())) return Optional.ofNullable(e.getValue());
        }
        return Optional.empty();
    }
}


