package com.example.panic.web;

import com.example.panic.service.PanicInterventionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PanicController {
    private final PanicInterventionService svc;

    public PanicController(PanicInterventionService svc) {
        this.svc = svc;
    }

    @PostMapping("/panic-intervention")
    public ResponseEntity<PanicResponse> handle(
            @Valid @RequestBody PanicRequest req,
            BindingResult br
    ) {
        if (br.hasErrors()) {
            List<String> errors = br.getFieldErrors().stream()
                    .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                    .toList();
            return ResponseEntity
                    .badRequest()
                    .body(new PanicResponse("Invalid request: " + String.join("; ", errors)));
        }

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