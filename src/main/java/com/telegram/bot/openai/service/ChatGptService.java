package com.telegram.bot.openai.service;

import com.telegram.bot.openai.api.GptHistory;
import com.telegram.bot.openai.api.GptRequest;
import com.telegram.bot.openai.api.Message;
import com.telegram.bot.openai.api.client.OpenAIClient;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final OpenAIClient openAIClient;

    private final ChatGptHistoryService chatGptHistoryService;

    private static final String SYSTEM_MESSAGE = """
        Ты - Telegram бот по имени Асклепий, и твоя задача – помогать пользователям с вопросами, связанными со здоровьем человека.

        Отвечай на медицинские вопросы четко и точно, опираясь на проверенные знания.
        
        Если вопрос сложен или требует медицинской консультации, предложи обратиться к врачу.

        Если вопрос не связан со здоровьем, вежливо объясни, что ты можешь отвечать только на вопросы по здоровью, и избегай обсуждения других тем.

        Соблюдай правила Telegram: не распространяй дезинформацию, уважай личные данные пользователей.
        
        Используй текстовое форматирование (жирный, курсив) для улучшения восприятия информации.

        Всегда поддерживай уважительный и профессиональный тон, независимо от контекста вопроса.
        """;

    @Nonnull
    public String getResponseChatForUser(
            Long userId,
            String userTextInput
    ) {
        chatGptHistoryService.createHistoryIfNotExist(userId);

        List<Message> messages = new ArrayList<>(List.of(
                Message.builder()
                        .role("system")
                        .content(SYSTEM_MESSAGE)
                        .build()
        ));

        var history = chatGptHistoryService.getUserHistory(userId).orElseGet(() -> new GptHistory(new ArrayList<>()));
        messages.addAll(history.chatMessages());

        Message userMessage = Message.builder()
                .content(userTextInput)
                .role("user")
                .build();
        chatGptHistoryService.addUserMessageToHistory(userId, userMessage);

        messages.add(userMessage);

        var request = GptRequest.builder()
                .model("gpt-4o")
                .messages(messages)
                .build();

        var response = openAIClient.createChatCompletion(request);

        var messageFromGpt = response.choices().getFirst().message();

        chatGptHistoryService.addBotMessageToHistory(userId, messageFromGpt);

        return messageFromGpt.content();
    }
}
