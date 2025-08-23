package com.example.chatbot.util;

public class Sanitizer {
    public static String sanitize(String input) {
        if (input == null) return "";
        String s = input.strip();
        // Remove control chars
        s = s.replaceAll("\\p{Cntrl}", "");
        return s;
    }
}


