package ru.weather.bot.weatherbot.service.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.weather.bot.weatherbot.json.WeatherMapper;
import ru.weather.bot.weatherbot.models.BotModel;

import java.util.Deque;

@Component
public class DetailedWeather implements CallBackHandler
{
    private final Deque<BotApiMethod<?>> stackMessages;
    private final WeatherMapper weatherMapper;

    @Autowired
    public DetailedWeather(Deque<BotApiMethod<?>> stackMessages, WeatherMapper weatherMapper)
    {
        this.stackMessages = stackMessages;
        this.weatherMapper = weatherMapper;
    }

    @Override
    public BotApiMethod<?> processCallBack(Update update)
    {
        String cityName = weatherMapper.getReceiveData().getWeatherConfig().getCityName();
        stackMessages.push(stackMessages.peek());
        return EditMessageText.builder()
                .chatId(update.getMessage().getChatId())
                .messageId(update.getMessage().getMessageId())
                .text(weatherMapper.detailedWeather(cityName))
                .replyMarkup(new InlineKeyboardMarkup(BotModel.getBackButton()))
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public String getCallBackData()
    {
        return "DetailedWeather";
    }
}