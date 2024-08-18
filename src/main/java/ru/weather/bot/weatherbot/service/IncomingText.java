package ru.weather.bot.weatherbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.weather.bot.weatherbot.Observer;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.json.WeatherMapper;
import ru.weather.bot.weatherbot.models.BotModel;
import ru.weather.bot.weatherbot.models.Messages;
import ru.weather.bot.weatherbot.service.commands.CommandDispatcher;

import java.util.Deque;

@Service
public class IncomingText implements IncomingData, Observer
{
    private final Deque<BotApiMethod<?>> stackMessages;
    private final CommandDispatcher dispatcher;
    private final WeatherMapper weatherMapper;
    private BotLanguage language;

    @Autowired
    public IncomingText(Deque<BotApiMethod<?>> stackMessages, CommandDispatcher dispatcher, WeatherMapper weatherMapper)
    {
        this.stackMessages = stackMessages;
        this.dispatcher = dispatcher;
        this.weatherMapper = weatherMapper;
    }

    @Override
    public BotApiMethod<?> sendler(Update update)
    {
        stackMessages.clear();
        String text = update.getMessage().getText();
        String input_error = switch (language)
        {
            case RUSSIAN -> Messages.RU_CITY_INPUT_ERROR;
            case ENGLISH -> Messages.EN_CITY_INPUT_ERROR;
            case CHINESE -> Messages.CN_CITY_INPUT_ERROR;
            case GERMAN -> Messages.DE_CITY_INPUT_ERROR;
        };
        if (!text.equals(BotCommand.START.getCommand()) && dispatcher.getMessage() == null)
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(Messages.EN_UNSUCCESSFUL_RELOAD_EVENT_HANDLING)
                    .parseMode(ParseMode.HTML)
                    .build();
        if (dispatcher.isCommand(text))
            return dispatcher.currentCommand(update);
        else
        {
            if (text.matches("^.*\\s\\d{1,2}$"))
               return weatherForecast(update, input_error);
            else return currentWeather(update, input_error);
        }
    }

    private BotApiMethod<?> constraint(Update update)
    {
        if (!update.getMessage().getText().equals(BotCommand.START.getCommand()) && dispatcher.getMessage() == null)
        {
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(Messages.EN_UNSUCCESSFUL_RELOAD_EVENT_HANDLING)
                    .parseMode(ParseMode.HTML)
                    .build();
        }
        if (dispatcher.getMessage().equals(BotCommand.MAP.getCommand()))
    }

    public BotApiMethod<?> weatherForecast(Update update, String error)
    {
        String text = update.getMessage().getText();
        String weather = weatherMapper.weatherForecastDispatch(text.toLowerCase());
        if (weather == null || weather.isEmpty())
           return SendMessage.builder()
                   .chatId(update.getMessage().getChatId())
                   .text(error)
                   .parseMode(ParseMode.HTML)
                   .build();
        else
        {
            String[] city_days = weatherMapper.getProcessingData().splitSpace(text.toLowerCase());
            weatherMapper.getReceiveData().getWeatherConfig().setQuantityDays(Integer.parseInt(city_days[1]));
            weatherMapper.getReceiveData().getWeatherConfig().setCityName(city_days[0]);
            SendMessage message = SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(weather)
                    .replyMarkup(new InlineKeyboardMarkup(BotModel.getButtonsForecastWeather(weatherMapper.getReceiveData()
                            .getWeatherConfig().getQuantityDays())))
                    .parseMode(ParseMode.HTML)
                    .build();
            stackMessages.push(message);
            return message;
        }
    }

    public BotApiMethod<?> currentWeather(Update update, String error)
    {
        String text = update.getMessage().getText();
        String weather = weatherMapper.weatherDispatch(text.toLowerCase());
        if (weather == null || weather.isEmpty())
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(error)
                    .parseMode(ParseMode.HTML)
                    .build();
        else
        {
            SendMessage message = SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(weather)
                    .replyMarkup(new InlineKeyboardMarkup(BotModel.getButtonForDetailedWeather(language, "DetailedWeather")))
                    .parseMode(ParseMode.HTML)
                    .build();
            stackMessages.push(message);
            weatherMapper.getReceiveData().getWeatherConfig().setCityName(text.toLowerCase());
            return message;
        }
    }

    public

    @Override
    public void update(BotLanguage language)
    {
        this.language = language;
    }
}