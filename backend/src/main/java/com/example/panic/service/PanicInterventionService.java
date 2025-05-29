package com.example.panic.service;

import com.example.panic.prompt.CalmPrompt;
import com.example.panic.prompt.CalmPromptBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PanicInterventionService {
    private final SonarService sonar;
    private final LLMService llm;

    // Spring will inject your SonarServiceImpl and LLMServiceOpenRouter
    public PanicInterventionService(SonarService sonar, LLMService llm) {
        this.sonar = sonar;
        this.llm   = llm;
    }

    /**
     * Builds a prompt from inputs, calls Sonar → LLM, and returns the text.
     */
    public String detectAndRespond(
            String sms,
            String email,
            Map<String, Object> wearables,
            String mood,
            int heartRate,
            int historyCount
    ) {
        // 1. Get a panic score from Sonar
        double score = sonar.analyze(sms, email, wearables);

        // 2. Build the CalmPrompt
        CalmPrompt prompt = new CalmPromptBuilder()
                .withUserMood(mood)
                .withHeartRate(heartRate)
                .withSonarScore(score)
                .withHistoryCount(historyCount)
                .build();

        // 3. Send to your LLM service and return the result
        return llm.getCalmingResponse(prompt);
    }
}