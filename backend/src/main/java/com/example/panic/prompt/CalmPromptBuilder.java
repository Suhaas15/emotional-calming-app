package com.example.panic.prompt;

import java.util.HashMap;
import java.util.Map;

public class CalmPromptBuilder {
    private final StringBuilder sb = new StringBuilder();
    private final Map<String,Object> ctx = new HashMap<>();

    public CalmPromptBuilder withUserMood(String mood) {
        sb.append("Mood=").append(mood).append("; ");
        ctx.put("mood", mood);
        return this;
    }

    public CalmPromptBuilder withHeartRate(int hr) {
        sb.append("HR=").append(hr).append("bpm; ");
        ctx.put("heartRate", hr);
        return this;
    }

    public CalmPromptBuilder withSonarScore(double score) {
        sb.append("SonarScore=").append(score).append("; ");
        ctx.put("sonarScore", score);
        return this;
    }

    public CalmPromptBuilder withHistoryCount(int count) {
        sb.append("HistoryCount=").append(count).append("; ");
        ctx.put("historyCount", count);
        return this;
    }

    public CalmPrompt build() {
        return new CalmPrompt(sb.toString().trim(), ctx);
    }
}