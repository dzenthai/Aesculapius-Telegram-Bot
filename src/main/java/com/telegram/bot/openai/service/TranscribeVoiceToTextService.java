package com.telegram.bot.openai.service;

import com.telegram.bot.openai.api.TranscriptionRequest;
import com.telegram.bot.openai.api.client.OpenAIClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@AllArgsConstructor
public class TranscribeVoiceToTextService {

    private final OpenAIClient openAIClient;

    public String transcribe(File audioFile) {
        var response = openAIClient.createTranscription(TranscriptionRequest.builder()
                        .audioFile(audioFile)
                        .model("whisper-1")
                .build());
        return response.text();
    }

}
