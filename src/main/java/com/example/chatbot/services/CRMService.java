package com.example.chatbot.services;

public class CRMService {
    public String getCustomerByEmail(String email) {
        return "Customer record for " + email + " (mock).";
    }

    public String getProductInfo(String keyword) {
        return "Product info for '" + keyword + "': High-quality, 2-year warranty (mock).";
    }
}


