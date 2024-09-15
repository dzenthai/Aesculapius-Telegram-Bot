package com.telegram.bot.openai.api;

import lombok.Builder;

import java.io.File;


@Builder
public record TranscriptionRequest(
        File audioFile,
        String model
) {}
