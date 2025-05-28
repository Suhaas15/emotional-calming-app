package com.example.panic.service;

import com.example.panic.prompt.CalmPrompt;

/**
 * Returns a calming response based on a prompt.
 */
public interface LLMService {
    String getCalmingResponse(CalmPrompt prompt);
}