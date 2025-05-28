package com.example.panic.service;

import com.example.panic.prompt.CalmPrompt;

/**
 * A dummy AI that echoes back the prompt text.
 */
public class LLMServiceMock implements LLMService {
    @Override
    public String getCalmingResponse(CalmPrompt prompt) {
        return "[MOCK LLM] Based on: " + prompt.getText();
    }
}