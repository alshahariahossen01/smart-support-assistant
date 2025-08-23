package com.example.chatbot.services;

public class PaymentService {
    public String startRefund(String orderId) {
        return "Refund process started for order " + orderId + ". You will receive confirmation via email (mock).";
    }
}


