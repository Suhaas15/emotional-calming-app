package com.example.panic.service;

import java.util.Map;

/**
 * A dummy implementation that always returns 0.75
 */
public class SonarServiceMock implements SonarService {
    @Override
    public double analyze(String sms, String email, Map<String,Object> wearableData) {
        return 0.75;
    }
}