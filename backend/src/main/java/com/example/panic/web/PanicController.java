package com.example.panic.web;

import com.example.panic.service.PanicInterventionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PanicController {
    private final PanicInterventionService svc;

    public PanicController(PanicInterventionService svc) {
        this.svc = svc;
    }

    @PostMapping("/panic-intervention")
    public ResponseEntity<PanicResponse> handle(@RequestBody PanicRequest req) {
        String result = svc.detectAndRespond(
                req.sms,
                req.email,
                req.wearables,
                req.mood,
                req.heartRate,
                req.historyCount
        );
        return ResponseEntity.ok(new PanicResponse(result));
    }
}