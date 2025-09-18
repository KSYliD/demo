package com.example.demo.controller;

import com.example.demo.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramController {
    private final TelegramService telegramService;
    @PostMapping("/webhook")
    public ResponseEntity<?> onUpdate(@RequestBody Map<String, Object> update) {
        System.out.println("Received update: " + update);
        telegramService.handleUpdate(update);
        return ResponseEntity.ok().build();
    }

}
