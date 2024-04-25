package ru.weather.bot.weatherbot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@FunctionalInterface
public interface EditMessage
{
    List<List<InlineKeyboardButton>> getButton();
}