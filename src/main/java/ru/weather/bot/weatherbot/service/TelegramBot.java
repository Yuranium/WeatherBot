package ru.weather.bot.weatherbot.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.weather.bot.weatherbot.config.BotConfig;
import ru.weather.bot.weatherbot.models.BotModel;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot
{
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig)
    {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
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
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (message)
            {
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/lang":
                    break;
                default:
                    defaultCommand(chatId);
            }
        } else if (update.hasCallbackQuery())
        {
            String newMessage;
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            EditMessageText text = new EditMessageText();
            text.setChatId(chatId);
            text.setMessageId(messageId);
            switch (callbackData)
            {
                case "RuLang":
                    newMessage = "\uD83D\uDC4C Хорошо! Теперь дальнейшее общение будет на русском языке.\n\n" +
                            "⚙ Чтобы изменить язык общения, выберите нужный с помощью команды: /lang";
                    executeMessage(newMessage, text);
                    break;
                case "EnLang":
                    newMessage = "\uD83D\uDC4C Good! Now further communication will be in English.\n\n" +
                            "⚙ To change the language of communication, select the desired language with the command: /lang";
                    executeMessage(newMessage, text);
                    break;
                case "CnLang":
                    newMessage = "\uD83D\uDC4C 很好！现在，我们将用德语进行进一步交流。\n\n⚙ 要更改通信语言，请使用 /lang 命令选择所需的语言。";
                    executeMessage(newMessage, text);
                    break;
                case "DeLang":
                    newMessage = "\uD83D\uDC4C Sehr gut! Nun wird die weitere Kommunikation auf Deutsch erfolgen.\n\n" +
                            "⚙ Um die Sprache der Kommunikation zu ändern, wählen Sie die gewünschte Sprache mit dem Befehl: /lang";
                    executeMessage(newMessage, text);
                    break;
                default:
                    defaultCommand(chatId);
            }
        }
    }

    private void executeMessage(String text, @NotNull EditMessageText messageText)
    {
        messageText.setText(text);
        try
        {
            execute(messageText);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void startCommand(long chatId, String name)
    {
        String message = "Hi, " + name + ", glad to meet you \uD83D\uDC4B\n\n" +
                "❗✋ Before you work with me further, choose the right language to communicate in. ✋❗";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = BotModel.getRowsForScreenButton();
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        try
        {
            execute(sendMessage);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void defaultCommand(long chatId)
    {
        String message = "Sorry, this command is not supported at the moment";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try
        {
            execute(sendMessage);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }
}