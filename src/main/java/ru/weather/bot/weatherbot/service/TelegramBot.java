package ru.weather.bot.weatherbot.service;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.weather.bot.weatherbot.config.BotConfig;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.json.ProcessingData;
import ru.weather.bot.weatherbot.json.ReceiveData;
import ru.weather.bot.weatherbot.json.WeatherMapper;
import ru.weather.bot.weatherbot.models.BotModel;
import ru.weather.bot.weatherbot.models.Messages;

import java.io.File;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

import static ru.weather.bot.weatherbot.service.MessageSend.createMessage;

@Service
public class TelegramBot extends TelegramLongPollingBot
{
    private final BotConfig botConfig;
    private final WeatherMapper weatherMapper;
    private final Deque<BotApiMethod<?>> stackMessages;
    private final ReceiveData receiveData;
    private final ProcessingData processingData;
    private BotLanguage botLanguage;
    private BotCommand botCommand;

    @Autowired
    public TelegramBot(BotConfig botConfig, WeatherMapper weatherMapper, Deque<BotApiMethod<?>> stackMessages,
                       ReceiveData receiveData, ProcessingData processingData) throws TelegramApiException
    {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.weatherMapper = weatherMapper;
        this.stackMessages = stackMessages;
        this.receiveData = receiveData;
        this.processingData = processingData;
        botLanguage = BotLanguage.ENGLISH;
        execute(new SetMyCommands(BotModel.commandListForBotMenu(), new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername()
    {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            stackMessages.clear();
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setChatId(chatId);
            botConfig.setMessageId(update.getMessage().getMessageId() + 1);
            if (botCommand == null && !message.equals(BotCommand.START.getCommand()))
            {
                executeMessage(createMessage(chatId, Messages.EN_UNSUCCESSFUL_RELOAD_EVENT_HANDLING), null);
                return;
            }

            switch (message)
            {
                case "/start":
                    botCommand = BotCommand.START;
                    botCommand.startCommand(this, chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    botCommand = BotCommand.HELP;
                    botCommand.helpCommand(this, chatId, botLanguage);
                    break;
                case "/lang":
                    botCommand = BotCommand.LANG;
                    botCommand.langCommand(this, chatId, botLanguage);
                    break;
                case "/map":
                    botCommand = BotCommand.MAP;
                    botCommand.mapCommand(this, chatId, botLanguage);
                    break;
                default:
                    String input_error = switch (botLanguage)
                    {
                        case RUSSIAN -> Messages.RU_CITY_INPUT_ERROR;
                        case ENGLISH -> Messages.EN_CITY_INPUT_ERROR;
                        case CHINESE -> Messages.CN_CITY_INPUT_ERROR;
                        case GERMAN -> Messages.DE_CITY_INPUT_ERROR;
                    };
                    if (botCommand.getCommand().equals(BotCommand.MAP.getCommand()))
                    {
                        File photoMap = receiveData.fetchWeatherMap(message, botLanguage);
                        botCommand = BotCommand.DEFAULT;
                        if (photoMap == null)
                            executeMessage(createMessage(chatId, input_error), null);
                        else
                        {
                            try
                            {
                                SendPhoto sendPhoto = new SendPhoto();
                                sendPhoto.setChatId(chatId);
                                sendPhoto.setPhoto(new InputFile(photoMap));
                                execute(sendPhoto);
                                photoMap.delete();
                                executeMessage(createMessage(chatId, Messages.infoWeatherWithMap(botLanguage, processingData.convertCityNameCorrectly(message))), null);
                            } catch (TelegramApiException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        if (message.contains("/"))
                            botCommand.defaultCommand(this, chatId, botLanguage);
                        else
                        {
                            String weather;
                            if (message.matches("^.*\\s\\d{1,2}$"))
                            {
                                weather = weatherMapper.weatherForecastDispatch(message.toLowerCase(), botLanguage);
                                if (weather == null || weather.isEmpty())
                                    executeMessage(createMessage(chatId, input_error), null);
                                else
                                {
                                    String[] city_days = processingData.splitSpace(message.toLowerCase());
                                    receiveData.getWeatherConfig().setWeatherMessage(weather);
                                    receiveData.getWeatherConfig().setQuantityDays(Integer.parseInt(city_days[1]));
                                    receiveData.getWeatherConfig().setCityName(city_days[0]);
                                    sendMessage.setText(weather);
                                    stackMessages.push(sendMessage);
                                    executeMessage(sendMessage, () -> BotModel.getButtonsForecastWeather(receiveData.getWeatherConfig().getQuantityDays()));
                                }
                            }
                            else
                            {
                                weather = weatherMapper.weatherDispatch(message.toLowerCase(), botLanguage);
                                if (weather == null || weather.isEmpty())
                                    executeMessage(createMessage(chatId, input_error), null);
                                else
                                {
                                    sendMessage.setText(weather);
                                    stackMessages.push(sendMessage);
                                    receiveData.getWeatherConfig().setCityName(message.toLowerCase());
                                    executeMessage(sendMessage, () -> BotModel.getButtonForDetailedWeather(botLanguage, "DetailedWeather"));
                                }
                            }
                        }
                    }
            }
        } else if (update.hasCallbackQuery())
        {
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            EditMessageText text = new EditMessageText();
            text.setChatId(chatId);
            text.setMessageId(messageId);

            String cityName = receiveData.getWeatherConfig().getCityName();
            String unsuccessfulEvent = switch (botLanguage)
            {
                case RUSSIAN -> Messages.RU_UNSUCCESSFUL_EVENT_HANDLING;
                case ENGLISH -> Messages.EN_UNSUCCESSFUL_EVENT_HANDLING;
                case CHINESE -> Messages.CN_UNSUCCESSFUL_EVENT_HANDLING;
                case GERMAN -> Messages.DE_UNSUCCESSFUL_EVENT_HANDLING;
            };

            if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue()))
            {
                executeMessage(createMessage(chatId, unsuccessfulEvent), null);
                return;
            }
            switch (callbackData)
            {
                case "RuLang":
                    languageSwitching(BotLanguage.RUSSIAN, text);
                    break;
                case "EnLang":
                    languageSwitching(BotLanguage.ENGLISH, text);
                    break;
                case "CnLang":
                    languageSwitching(BotLanguage.CHINESE, text);
                    break;
                case "DeLang":
                    languageSwitching(BotLanguage.GERMAN, text);
                    break;
                case "DetailedWeather":
                    stackMessages.push(stackMessages.peek());
                    editMessage(weatherMapper.detailedWeather(cityName, botLanguage), text, BotModel::getBackButton);
                    return;
                case "DetailedWeatherForecast":
                    stackMessages.push(stackMessages.peek());
                    editMessage(weatherMapper.detailedWeatherForecast(cityName, receiveData.getWeatherConfig().getCurrentDay(), botLanguage),
                            text, BotModel::getBackButton);
                    return;
                case "WF_1":
                    stackMessages.push(stackMessages.peek());
                    receiveData.getWeatherConfig().setCurrentDay(1);
                    editMessage(weatherMapper.weatherForecastDay(cityName, 8 - 2, botLanguage), text, BotModel::buttonWF);
                    return;
                case "WF_2":
                    stackMessages.push(stackMessages.peek());
                    receiveData.getWeatherConfig().setCurrentDay(2);
                    editMessage(weatherMapper.weatherForecastDay(cityName, 2 * 8 - 2, botLanguage), text, BotModel::buttonWF);
                    return;
                case "WF_3":
                    stackMessages.push(stackMessages.peek());
                    receiveData.getWeatherConfig().setCurrentDay(3);
                    editMessage(weatherMapper.weatherForecastDay(cityName, 3 * 8 - 2, botLanguage), text, BotModel::buttonWF);
                    return;
                case "WF_4":
                    stackMessages.push(stackMessages.peek());
                    receiveData.getWeatherConfig().setCurrentDay(4);
                    editMessage(weatherMapper.weatherForecastDay(cityName, 4 * 8 - 2, botLanguage), text, BotModel::buttonWF);
                    return;
                case "WF_5":
                    stackMessages.push(stackMessages.peek());
                    receiveData.getWeatherConfig().setCurrentDay(5);
                    editMessage(weatherMapper.weatherForecastDay(cityName, 5 * 8 - 2, botLanguage), text, BotModel::buttonWF);
                    return;
                case "Back":
                    if (stackMessages.isEmpty() || stackMessages.size() < 0)
                    {
                        executeMessage(createMessage(chatId, unsuccessfulEvent), null);
                        return;
                    }
                    if (stackMessages.peek() instanceof SendMessage)
                    {
                        SendMessage send = (SendMessage) stackMessages.pop();
                        text.setReplyMarkup((InlineKeyboardMarkup) send.getReplyMarkup());
                        editMessage(send.getText(), text, null);
                        return;
                    } else if (stackMessages.peek() instanceof EditMessageText)
                    {
                        EditMessageText send = (EditMessageText) stackMessages.pop();
                        text.setReplyMarkup(send.getReplyMarkup());
                        editMessage(send.getText(), text, null);
                        return;
                    }
                default:
                    botCommand.defaultCommand(this, chatId, botLanguage);
            }
            if (botCommand != null && botCommand.getCommand().equals(BotCommand.START.getCommand()))
                botCommand.helpCommand(this, chatId, botLanguage);
        }
    }

    @SneakyThrows
    public void editMessage(String text, @NotNull EditMessageText messageText, Supplier<List<List<InlineKeyboardButton>>> message)
    {
        messageText.setText(text);
        messageText.setParseMode(ParseMode.HTML);
        if (message != null)
        {
            var markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = message.get();
            markup.setKeyboard(rows);
            messageText.setReplyMarkup(markup);
        }
        execute(messageText);
    }

    @SneakyThrows
    public void executeMessage(SendMessage sendMessage, Supplier<List<List<InlineKeyboardButton>>> message)
    {
        if (message != null)
        {
            var markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = message.get();
            markup.setKeyboard(rows);
            sendMessage.setReplyMarkup(markup);
        }
        execute(sendMessage);
    }

    private void languageSwitching(BotLanguage language, EditMessageText messageText)
    {
        if (botLanguage == language)
            editMessage(switch (language)
            {
                case RUSSIAN -> Messages.RU_THE_LANGUAGE_IS_ALREADY_THERE;
                case ENGLISH -> Messages.EN_THE_LANGUAGE_IS_ALREADY_THERE;
                case CHINESE -> Messages.CN_THE_LANGUAGE_IS_ALREADY_THERE;
                case GERMAN -> Messages.DE_THE_LANGUAGE_IS_ALREADY_THERE;
            }, messageText, null);
        else
        {
            editMessage(switch (language)
            {
                case RUSSIAN -> Messages.RU_FURTHER_COMMUNICATION;
                case ENGLISH -> Messages.EN_FURTHER_COMMUNICATION;
                case CHINESE -> Messages.CN_FURTHER_COMMUNICATION;
                case GERMAN -> Messages.DE_FURTHER_COMMUNICATION;
            }, messageText, null);
            botLanguage = language;
            BotModel.language = botLanguage;
        }
    }
}