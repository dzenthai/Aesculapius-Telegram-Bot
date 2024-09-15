package com.telegram.bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface CommandHandler {

    BotApiMethod<?> processCommand(Message update);

    Commands getSupportedCommand();
}
