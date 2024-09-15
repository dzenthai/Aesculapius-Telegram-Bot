package com.telegram.bot.openai.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record GptResponse(
    @JsonProperty("choices") List<Choice> choices
){}
