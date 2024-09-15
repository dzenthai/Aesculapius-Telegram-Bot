package com.telegram.bot.telegram.message;

import com.telegram.bot.command.service.CommandsDispatcher;
import com.telegram.bot.telegram.service.TelegramAsyncMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramUpdateMessageHandler {

    private final CommandsDispatcher telegramCommandsDispatcher;

    private final TelegramAsyncMessageSender telegramAsyncMessageSender;

    private final TelegramTextHandler telegramTextHandler;

    private final TelegramVoiceHandler telegramVoiceHandler;

    public BotApiMethod<?> handleMessage(Message message) {
        log.info("Telegram Update Message Handler | Start message processing: message: {}", message);
        if (telegramCommandsDispatcher.isCommand(message)) {
            return telegramCommandsDispatcher.processCommand(message);
        }
        var chatId = message.getChatId().toString();

        if (message.hasVoice() || message.hasText()) {
            telegramAsyncMessageSender.sendMessageAsync(
                    chatId,
                    () -> handleMessageAsync(message),
                    (throwable) -> getErrorMessage(throwable, chatId)
            );
        }
        return null;
    }

    private SendMessage handleMessageAsync(Message message) {
        SendMessage result = message.hasVoice()
                ? telegramVoiceHandler.processVoice(message)
                : telegramTextHandler.processTextMessage(message);

        result.setParseMode(ParseMode.MARKDOWNV2);
        return result;
    }

    private SendMessage getErrorMessage(Throwable throwable, String chatId) {
        log.error("Telegram Update Message Handler | Произошла ошибка, chatId: {}", chatId, throwable);
        return SendMessage.builder()
                .chatId(chatId)
                .text("Произошла ошибка, попробуйте позже")
                .build();
    }

}
