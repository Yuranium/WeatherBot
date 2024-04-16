package ru.weather.bot.weatherbot.models;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BotModel
{
    public static List<List<InlineKeyboardButton>> getRowsForScreenButton()
    {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        var ruLang = new InlineKeyboardButton();
        ruLang.setText("Russian");
        ruLang.setCallbackData("RuLang");

        var enLang = new InlineKeyboardButton();
        enLang.setText("English");
        enLang.setCallbackData("EnLang");

        var cnLang = new InlineKeyboardButton();
        cnLang.setText("Chinese");
        cnLang.setCallbackData("CnLang");

        var deLang = new InlineKeyboardButton();
        deLang.setText("German");
        deLang.setCallbackData("DeLang");

        row1.add(ruLang);
        row1.add(enLang);
        row2.add(cnLang);
        row2.add(deLang);

        rows.add(row1);
        rows.add(row2);
        return rows;
    }
}