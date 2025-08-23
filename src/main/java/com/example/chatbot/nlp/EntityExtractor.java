package com.example.chatbot.nlp;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityExtractor {
    private static final Pattern ORDER_ID = Pattern.compile("(?:order|#)\\s*#?\\s*(\\d{4,})", Pattern.CASE_INSENSITIVE);
    private static final Pattern DATETIME = Pattern.compile("(tomorrow|today|next week|\\d{1,2}(?:am|pm))", Pattern.CASE_INSENSITIVE);

    public record Entities(Optional<String> orderId, Optional<String> productKeyword, Optional<String> datetimeText) {}

    public Entities extract(String text) {
        String t = text == null ? "" : text;
        Optional<String> order = extractFirst(ORDER_ID, t);
        Optional<String> datetime = extractFirst(DATETIME, t);
        Optional<String> product = Optional.empty();
        return new Entities(order, product, datetime);
    }

    private Optional<String> extractFirst(Pattern p, String text) {
        Matcher m = p.matcher(text);
        if (m.find()) {
            return Optional.ofNullable(m.group(1));
        }
        return Optional.empty();
    }
}


