package com.telegram.bot.command.handler;

import com.telegram.bot.command.CommandHandler;
import com.telegram.bot.command.Commands;
import com.telegram.bot.openai.service.ChatGptHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
@RequiredArgsConstructor
public class ClearChatHistoryCommandHandler implements CommandHandler {

    private final ChatGptHistoryService chatGptHistoryService;

    @Override
    public BotApiMethod<?> processCommand(Message message) {
        chatGptHistoryService.clearHistory(message.getChatId());
        String clearHistoryMessage = "Контекст успешно очищен!";
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(clearHistoryMessage)
                .build();
    }

    @Override
    public Commands getSupportedCommand() {
        return Commands.CLEAR_COMMAND;
    }
}
