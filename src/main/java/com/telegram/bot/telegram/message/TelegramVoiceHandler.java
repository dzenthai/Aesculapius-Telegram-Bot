package com.telegram.bot.telegram.message;

import com.telegram.bot.openai.service.ChatGptService;
import com.telegram.bot.openai.service.TranscribeVoiceToTextService;
import com.telegram.bot.telegram.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Service
@RequiredArgsConstructor
public class TelegramVoiceHandler {

    private final ChatGptService gptService;

    private final FileService telegramFileService;

    private final TranscribeVoiceToTextService transcribeVoiceToTextService;

    public SendMessage processVoice(Message message) {
        var chatId = message.getChatId();
        var voice = message.getVoice();

        var fileId = voice.getFileId();
        var file = telegramFileService.getFile(fileId);
        var text = transcribeVoiceToTextService.transcribe(file);

        var gptGeneratedText = gptService.getResponseChatForUser(chatId, text);
        return new SendMessage(chatId.toString(), gptGeneratedText);
    }
}
