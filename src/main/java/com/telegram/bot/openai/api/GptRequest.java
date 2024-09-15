package com.telegram.bot.openai.api;

import lombok.Builder;

import java.util.List;


@Builder
public record GptRequest(
        String model,
        List<Message> messages
) {
}
