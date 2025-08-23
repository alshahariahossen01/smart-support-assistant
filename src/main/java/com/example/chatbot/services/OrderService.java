package com.example.chatbot.services;

public class OrderService {
    public String getOrderStatus(String orderId) {
        return "Order " + orderId + " is in transit and expected in 2 days (mock).";
    }
}


