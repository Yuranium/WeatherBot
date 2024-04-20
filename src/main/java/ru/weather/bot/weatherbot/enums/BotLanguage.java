package ru.weather.bot.weatherbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotLanguage
{
    RUSSIAN("ru"), ENGLISH("en"), CHINESE("zh_cn"), GERMAN("de");

    private final String region;
}