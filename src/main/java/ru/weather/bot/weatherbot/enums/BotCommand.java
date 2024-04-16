package ru.weather.bot.weatherbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Deprecated
@Getter
@AllArgsConstructor
public enum BotCommand
{
    START("/start"), LANG("/lang");

    private final String command;
}