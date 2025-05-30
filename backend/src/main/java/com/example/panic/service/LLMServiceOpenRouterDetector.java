package com.example.panic.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Uses OpenRouter LLM to estimate panic likelihood (0.0–1.0).
 */
@Service
public class LLMServiceOpenRouterDetector implements SonarService {

    private final WebClient client;
    private final String model;

    public LLMServiceOpenRouterDetector(
            WebClient openRouterClient,
            @Value("${openrouter.api.model}") String model
    ) {
        this.client = openRouterClient;
        this.model  = model;
    }

    @Override
    public double analyze(String sms, String email, Map<String,Object> wearableData) {
        // Build a chat-prompt asking the model for a panic score
        String userContent = String.format(
                "SMS: \"%s\"\nEmail: \"%s\"\nHeart rate: %s\n\n"
                        + "On a scale from 0.0 (no panic) to 1.0 (maximum panic), return only a single decimal number.",
                sms, email, wearableData.getOrDefault("hr", "unknown")
        );

        Map<String,Object> body = Map.of(
                "model",    model,
                "messages", List.of(
                        Map.of("role", "system",  "content", "You are an assistant that returns only a numeric panic likelihood."),
                        Map.of("role", "user",    "content", userContent)
                )
        );

        Mono<OpenRouterResponse> resp = client.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OpenRouterResponse.class)
                .timeout(Duration.ofSeconds(10));

        OpenRouterResponse or = resp.block();
        if (or == null || or.choices.isEmpty() || or.choices.get(0).message == null) {
            throw new IllegalStateException("No response from OpenRouter for panic score");
        }

        String text = or.choices.get(0).message.content.trim();
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid panic score from LLM: " + text, e);
        }
    }

    // DTOs matching OpenRouter’s response schema
    public static class OpenRouterResponse {
        public List<Choice> choices;
        public static class Choice { public Message message; }
        public static class Message { public String role; public String content; }
    }
}