package com.example.chatbot.kb;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class KnowledgeBaseTest {
    @Test
    void loadsAndAnswersShipping() {
        KnowledgeBase kb = new KnowledgeBase();
        kb.loadDefaults();
        Optional<String> ans = kb.answer("What are your shipping times?");
        assertTrue(ans.isPresent());
        assertTrue(ans.get().toLowerCase().contains("shipping"));
    }
}


