package ru.weather.bot.weatherbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotCommand
{
    START("/start"), HELP("/help"), LANG("/lang"), MAP("/map"), DEFAULT("");
    private final String command;
}