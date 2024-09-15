package com.telegram.bot.openai.api.client;

import com.telegram.bot.openai.api.GptRequest;
import com.telegram.bot.openai.api.GptResponse;
import com.telegram.bot.openai.api.TranscriptionRequest;
import com.telegram.bot.openai.api.TranscriptionResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
public class OpenAIClient {

    private final String token;

    private final RestTemplate restTemplate;

    public GptResponse createChatCompletion(
            GptRequest request
    ) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.set("Content-type", "application/json");

        HttpEntity<GptRequest> httpEntity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<GptResponse> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, httpEntity, GptResponse.class
        );
        return responseEntity.getBody();
    }

    @SneakyThrows
    public TranscriptionResponse createTranscription(
            TranscriptionRequest request
    ) {
        String url = "https://api.openai.com/v1/audio/transcriptions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.set("Content-type", "multipart/form-data");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(request.audioFile()));
        body.add("model", request.model());

        var httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<TranscriptionResponse> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, httpEntity, TranscriptionResponse.class
        );
        return responseEntity.getBody();
    }
}
