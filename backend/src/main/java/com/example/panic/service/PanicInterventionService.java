package com.example.panic.service;

import com.example.panic.prompt.CalmPrompt;
import com.example.panic.prompt.CalmPromptBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PanicInterventionService {
    private final SonarService sonar;
    private final LLMServiceProxy llmProxy;

    public PanicInterventionService() {
        this.sonar    = new SonarServiceMock();
        LLMService mockLLM = new LLMServiceMock();
        this.llmProxy = new LLMServiceProxy(mockLLM, mockLLM);
    }

    /**
     * Builds a prompt, runs Sonar → LLM, and returns the calming response.
     */
    public String detectAndRespond(
            String sms,
            String email,
            Map<String,Object> wearables,
            String mood,
            int heartRate,
            int historyCount
    ) {
        double score = sonar.analyze(sms, email, wearables);

        CalmPrompt prompt = new CalmPromptBuilder()
                .withUserMood(mood)
                .withHeartRate(heartRate)
                .withSonarScore(score)
                .withHistoryCount(historyCount)
                .build();

        return llmProxy.getCalmingResponse(prompt);
    }
}