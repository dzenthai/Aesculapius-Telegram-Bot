package com.telegram.bot.openai.service;

import com.telegram.bot.openai.api.GptHistory;
import com.telegram.bot.openai.api.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGptHistoryService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String HISTORY_KEY_PREFIX = "chatHistory:";

    private static final Duration HISTORY_TTL = Duration.ofDays(1);

    private String getHistoryKey(Long userId) {
        return HISTORY_KEY_PREFIX + userId;
    }

    public Optional<GptHistory> getUserHistory(Long userId) {
        log.debug("Chat Gpt History Service | Fetching history for user: {}", userId);
        var chatHistory = (GptHistory) redisTemplate.opsForValue().get(getHistoryKey(userId));
        log.debug("Chat Gpt History Service | Fetched history: {}", chatHistory);
        return Optional.ofNullable(chatHistory);
    }

    public void createHistory(Long userId) {
        log.debug("Chat Gpt History Service | Creating history for user: {}", userId);
        redisTemplate.opsForValue().set(getHistoryKey(userId), new GptHistory(new ArrayList<>()));
        redisTemplate.expire(getHistoryKey(userId), HISTORY_TTL);
        log.debug("Chat Gpt History Service | History created for user: {}", userId);
    }

    public void clearHistory(Long userId) {
        log.debug("Chat Gpt History Service | Clearing history for user: {}", userId);
        redisTemplate.delete(getHistoryKey(userId));
        log.debug("Chat Gpt History Service | History cleared for user: {}", userId);
    }

    public void addUserMessageToHistory(Long userId, Message message) {
        log.debug("Chat Gpt History Service | Adding user message to history for user: {}", userId);
        var chatHistory = (GptHistory) redisTemplate.opsForValue().get(getHistoryKey(userId));
        if (chatHistory == null) {
            throw new IllegalStateException("History not exists for user: %s".formatted(userId));
        }
        chatHistory.chatMessages().add(message);
        redisTemplate.opsForValue().set(getHistoryKey(userId), chatHistory);
        redisTemplate.expire(getHistoryKey(userId), HISTORY_TTL);
        log.debug("Chat Gpt History Service | User message added to history for user: {}", userId);
    }

    public void addBotMessageToHistory(Long userId, Message message) {
        log.debug("Chat Gpt History Service | Adding bot message to history for user: {}", userId);
        var chatHistory = (GptHistory) redisTemplate.opsForValue().get(getHistoryKey(userId));
        if (chatHistory == null) {
            throw new IllegalStateException("History not exists for user = %s".formatted(userId));
        }
        chatHistory.chatMessages().add(message);
        redisTemplate.opsForValue().set(getHistoryKey(userId), chatHistory);
        redisTemplate.expire(getHistoryKey(userId), HISTORY_TTL);
        log.debug("Chat Gpt History Service | Bot message added to history for user: {}", userId);
    }

    public void createHistoryIfNotExist(Long userId) {
        log.debug("Chat Gpt History Service | Checking if history exists for user: {}", userId);
        if (redisTemplate.opsForValue().get(getHistoryKey(userId)) == null) {
            createHistory(userId);
            log.debug("Chat Gpt History Service | History created for user (if not exist): {}", userId);
        }
    }
}
