package com.telegram.bot.openai.api;

import lombok.Builder;

import java.util.List;


@Builder
public record GptHistory(
        List<Message> chatMessages
) {
}
