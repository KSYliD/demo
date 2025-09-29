package com.example.demo.service.interfaces;

import java.util.Map;

public interface TelegramService {
    void handleUpdate(Map<String,Object> update);
}
