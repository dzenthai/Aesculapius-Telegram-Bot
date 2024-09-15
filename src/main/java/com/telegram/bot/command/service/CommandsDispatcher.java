package com.telegram.bot.command.service;

import com.telegram.bot.command.CommandHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommandsDispatcher {

    private final List<CommandHandler> telegramCommandHandlerList;

    public BotApiMethod<?> processCommand(Message message) {
        if (!isCommand(message)) {
            throw new IllegalArgumentException("Not a command passed");
        }
        var text = message.getText();
        var suitedHandler = telegramCommandHandlerList.stream()
                .filter(it -> it.getSupportedCommand().getCommandValue().equals(text))
                .findAny();
        if (suitedHandler.isEmpty()) {
            return SendMessage.builder()
                    .chatId(message.getChatId())
                    .text("Not supported command: command=%s".formatted(text))
                    .build();
        }
        return suitedHandler.orElseThrow().processCommand(message);
    }

    public boolean isCommand(Message message) {
        return message.hasText()
                && message.getText().startsWith("/");
    }
}
