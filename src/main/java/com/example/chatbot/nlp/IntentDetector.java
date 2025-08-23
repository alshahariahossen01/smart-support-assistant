package com.example.chatbot.nlp;

import org.apache.commons.lang3.StringUtils;

public class IntentDetector {
    public enum Intent { ORDER_STATUS, REFUND_REQUEST, PRODUCT_INFO, SCHEDULE_APPOINTMENT, FAQ, UNKNOWN }

    public Intent detectIntent(String text) {
        String t = StringUtils.defaultString(text).toLowerCase();
        if (t.contains("order status") || t.matches(".*order\\s*#?\\s*\\d+.*")) return Intent.ORDER_STATUS;
        if (t.contains("refund") || t.contains("return")) return Intent.REFUND_REQUEST;
        if (t.contains("product") || t.contains("specs") || t.contains("price")) return Intent.PRODUCT_INFO;
        if (t.contains("schedule") || t.contains("callback") || t.contains("appointment")) return Intent.SCHEDULE_APPOINTMENT;
        if (t.contains("faq") || t.contains("shipping") || t.contains("policy")) return Intent.FAQ;
        return Intent.UNKNOWN;
    }
}


