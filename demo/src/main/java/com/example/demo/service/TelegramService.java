package com.example.demo.service;

import com.example.demo.model.UserProfile;
import com.example.demo.repository.UserProfileRepository;
import com.example.demo.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final UserProfileRepository userProfileRepository;
    private final BranchRepository branchRepository;
    private final FeedbackService feedbackService;

    private final Map<Long, PendingProfile> pending = new ConcurrentHashMap<>();

    private final List<String> ROLES = List.of("mechanic", "electrician", "manager");
    private final RestTemplate rt = new RestTemplate();

    public void handleUpdate(Map<String,Object> update) {
        Map<String,Object> message = (Map<String,Object>) update.get("message");
        if (message == null) return;

        Map<String,Object> from = (Map<String,Object>) message.get("from");
        Map<String,Object> chat = (Map<String,Object>) message.get("chat");
        Long telegramUserId = ((Number) from.get("id")).longValue();
        Long chatId = ((Number) chat.get("id")).longValue();
        String text = (String) message.get("text");
        if (text == null) return;

        Optional<UserProfile> opt = userProfileRepository.findByTelegramUserId(telegramUserId);
        if (opt.isEmpty()) {
            handleRegistrationFlow(chatId, telegramUserId, text);
            return;
        }

        UserProfile profile = opt.get();
        feedbackService.handleNewFeedback(profile, text);
        sendMessage(chatId, "Дякуємо, ваш відгук отримано (анонімно).");
    }

    private void handleRegistrationFlow(Long chatId, Long telegramUserId, String text) {
        PendingProfile p = pending.get(telegramUserId);
        if (p == null) {
            sendRoleKeyboard(chatId, "Вітаємо! Оберіть вашу посаду:");
            pending.put(telegramUserId, new PendingProfile());
            return;
        }
        if (p.role == null) {
            String role = text.trim().toLowerCase();
            if (ROLES.contains(role)) {
                p.role = role;
                sendMessage(chatId, "Добре. Тепер введіть назву філії (наприклад: Київ-центр):");
            } else {
                sendRoleKeyboard(chatId, "Невідома посада. Оберіть з кнопок:");
            }
            return;
        }
        if (p.branchName == null) {
            String branchName = text.trim();
            var branch = branchRepository.findByName(branchName)
                    .orElseGet(() -> branchRepository.save(com.example.demo.model.Branch.builder().name(branchName).build()));
            var profile = com.example.demo.model.UserProfile.builder()
                    .telegramUserId(telegramUserId)
                    .role(p.role)
                    .branch(branch)
                    .build();
            userProfileRepository.save(profile);
            pending.remove(telegramUserId);
            sendMessage(chatId, "Готово — профіль збережено. Тепер можете надсилати відгуки.");
        }
    }

    private void sendRoleKeyboard(Long chatId, String text) {
        Map<String,Object> replyMarkup = Map.of(
                "keyboard", List.of(
                        List.of(Map.of("text","mechanic"), Map.of("text","electrician")),
                        List.of(Map.of("text","manager"))
                ),
                "one_time_keyboard", true,
                "resize_keyboard", true
        );
        sendMessage(chatId, text, replyMarkup);
    }

    private void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    private void sendMessage(Long chatId, String text, Map<String,Object> replyMarkup) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        Map<String,Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", text);
        if (replyMarkup != null) body.put("reply_markup", replyMarkup);
        rt.postForObject(url, body, String.class);
    }

    private static class PendingProfile {
        String role;
        String branchName;
    }
}
