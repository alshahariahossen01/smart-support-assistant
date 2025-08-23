package com.example.chatbot.nlp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntentDetectorTest {
    @Test
    void detectsOrderStatus() {
        IntentDetector d = new IntentDetector();
        assertEquals(IntentDetector.Intent.ORDER_STATUS, d.detectIntent("What's the order status for order 12345?"));
    }

    @Test
    void detectsRefund() {
        IntentDetector d = new IntentDetector();
        assertEquals(IntentDetector.Intent.REFUND_REQUEST, d.detectIntent("I need a refund"));
    }

    @Test
    void unknownOtherwise() {
        IntentDetector d = new IntentDetector();
        assertEquals(IntentDetector.Intent.UNKNOWN, d.detectIntent("Hello there"));
    }
}


