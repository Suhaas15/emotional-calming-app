package com.example.panic.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class SonarServiceMock implements SonarService {
    @Override
    public double analyze(String sms, String email, Map<String, Object> wearableData) {
        return 0.75;
    }
}