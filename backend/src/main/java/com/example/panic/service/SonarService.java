package com.example.panic.service;

import java.util.Map;

/**
 * Returns a panic likelihood score between 0.0–1.0
 */
public interface SonarService {
    double analyze(String sms, String email, Map<String,Object> wearableData);
}