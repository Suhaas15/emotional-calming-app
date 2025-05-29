package com.example.panic.web;

import jakarta.validation.constraints.*;
import java.util.Map;

public class PanicRequest {
    @NotBlank(message = "sms must not be blank")
    public String sms;

    @Email(message = "email must be a valid email")
    @NotBlank(message = "email must not be blank")
    public String email;

    @NotNull(message = "wearables must be provided")
    @Size(min = 1, message = "wearables must contain at least one entry")
    public Map<String,Object> wearables;

    @NotBlank(message = "mood must not be blank")
    public String mood;

    @Min(value = 30, message = "heartRate must be at least 30")
    @Max(value = 200, message = "heartRate must be at most 200")
    public int heartRate;

    @Min(value = 0, message = "historyCount must be non-negative")
    public int historyCount;
}