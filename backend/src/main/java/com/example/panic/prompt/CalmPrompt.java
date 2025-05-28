package com.example.panic.prompt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CalmPrompt {
    private final String text;
    private final Map<String,Object> context;

    public CalmPrompt(String text, Map<String, Object> context) {
        this.text = text;
        this.context = Collections.unmodifiableMap(new HashMap<>(context));
    }

    public String getText() {
        return text;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public CalmPrompt prepend(String prefix) {
        return new CalmPrompt(prefix + text, context);
    }
}