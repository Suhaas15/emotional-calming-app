package com.example.panic.service;

import com.example.panic.prompt.CalmPrompt;

/**
 * Wraps a primary LLMService with a fallback.
 */
public class LLMServiceProxy implements LLMService {
    private final LLMService primary;
    private final LLMService fallback;

    public LLMServiceProxy(LLMService primary, LLMService fallback) {
        this.primary  = primary;
        this.fallback = fallback;
    }

    @Override
    public String getCalmingResponse(CalmPrompt prompt) {
        try {
            return primary.getCalmingResponse(prompt);
        } catch (RuntimeException e) {
            return fallback.getCalmingResponse(prompt);
        }
    }
}