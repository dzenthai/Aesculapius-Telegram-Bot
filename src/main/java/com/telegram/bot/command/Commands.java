package com.telegram.bot.command;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Commands {
    START_COMMAND("/start"),
    CLEAR_COMMAND("/clear"),
    ABOUT_COMMAND("/info");

    private final String commandValue;

}
