package com.example.panic.service;

import com.example.panic.prompt.CalmPrompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class LLMServiceOpenRouter implements LLMService {
    private final WebClient client;
    private final String model;

    public LLMServiceOpenRouter(WebClient openRouterClient,
                                @Value("${openrouter.api.model}") String model) {
        this.client = openRouterClient;
        this.model  = model;
    }

    @Override
    public String getCalmingResponse(CalmPrompt prompt) {
        // build the chat-completion payload
        Map<String,Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a calm, empathetic assistant."),
                        Map.of("role", "user",   "content", prompt.getText())
                )
        );

        Mono<OpenRouterResponse> resp = client
                .post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OpenRouterResponse.class)
                .timeout(Duration.ofSeconds(10));

        OpenRouterResponse or = resp.block();
        if (or == null
                || or.choices == null
                || or.choices.isEmpty()
                || or.choices.get(0).message == null) {
            throw new IllegalStateException("No response from OpenRouter");
        }

        return or.choices.get(0).message.content;
    }

    // DTOs matching OpenRouter’s response schema
    public static class OpenRouterResponse {
        public List<Choice> choices;
        public static class Choice {
            public Message message;
        }
        public static class Message {
            public String role;
            public String content;
        }
    }
}