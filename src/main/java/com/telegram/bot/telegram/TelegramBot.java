package com.telegram.bot.telegram;

import com.telegram.bot.telegram.message.TelegramUpdateMessageHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramUpdateMessageHandler telegramUpdateMessageHandler;

    public TelegramBot(
            @Value("${bot.token}") String botToken,
            TelegramUpdateMessageHandler telegramUpdateMessageHandler
    ) {
        super(new DefaultBotOptions(), botToken);
        this.telegramUpdateMessageHandler = telegramUpdateMessageHandler;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        try {
            var method = processUpdate(update);
            if (method != null ) {
                sendApiMethod(method);
            }
        } catch (Exception e) {
            log.error("TelegramBot | Error while processing update: ", e);
            sendUserErrorMessage(update.getMessage().getChatId());
        }
    }

    private BotApiMethod<?> processUpdate(Update update) {
        return update.hasMessage()
                ? telegramUpdateMessageHandler.handleMessage(update.getMessage())
                : null;
    }

    @SneakyThrows
    private void sendUserErrorMessage(Long userId) {
        sendApiMethod(SendMessage.builder()
                .chatId(userId)
                .text("Извините, я не могу обработать ваш запрос. Пожалуйста," +
                        " убедитесь, что вы ввели текстовое сообщение с вашим вопросом или запросом," +
                        " и повторите попытку." +
                        " Также, если вы используете команды, убедитесь, что они верно введены." +
                        " Если у вас все еще возникают проблемы," +
                        " пожалуйста, обратитесь за помощью к администратору. Спасибо за понимание!")
                .build());
    }

    @Override
    public String getBotUsername() {
        return "My-test-bot";
    }
}
