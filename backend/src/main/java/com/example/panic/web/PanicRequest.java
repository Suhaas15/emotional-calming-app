package com.example.panic.web;

import java.util.Map;

public class PanicRequest {
    public String sms;
    public String email;
    public Map<String,Object> wearables;
    public String mood;
    public int heartRate;
    public int historyCount;
}