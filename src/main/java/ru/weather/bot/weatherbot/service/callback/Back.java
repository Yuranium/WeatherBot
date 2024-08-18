package ru.weather.bot.weatherbot.service.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.weather.bot.weatherbot.Observer;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;

import java.util.Deque;

@Component
public class Back implements CallBackHandler, Observer
{
    private final Deque<BotApiMethod<?>> stackMessages;
    private BotLanguage language;

    @Autowired
    public Back(Deque<BotApiMethod<?>> stackMessages)
    {
        this.stackMessages = stackMessages;
    }

    @Override
    public BotApiMethod<?> processCallBack(Update update)
    {
        EditMessageText editText = EditMessageText.builder()
                .chatId(update.getMessage().getChatId())
                .messageId(update.getMessage().getMessageId())
                .parseMode(ParseMode.HTML)
                .build();
        if (stackMessages.isEmpty())
        {
            editText.setText(switch (language)
            {
                case RUSSIAN -> Messages.RU_UNSUCCESSFUL_EVENT_HANDLING;
                case ENGLISH -> Messages.EN_UNSUCCESSFUL_EVENT_HANDLING;
                case CHINESE -> Messages.CN_UNSUCCESSFUL_EVENT_HANDLING;
                case GERMAN -> Messages.DE_UNSUCCESSFUL_EVENT_HANDLING;
            });
            return editText;
        }
        if (stackMessages.peek() instanceof SendMessage)
        {
            SendMessage send = (SendMessage) stackMessages.pop();
            editText.setReplyMarkup((InlineKeyboardMarkup) send.getReplyMarkup());
            editText.setText(send.getText());
            return editText;
        } else if (stackMessages.peek() instanceof EditMessageText)
        {
            EditMessageText send = (EditMessageText) stackMessages.pop();
            editText.setReplyMarkup(send.getReplyMarkup());
            editText.setText(send.getText());
            return editText;
        }
        else throw new RuntimeException("This action does not correspond to SendMessage and EditMessageText classes");
    }

    @Override
    public String getCallBackData()
    {
        return "Back";
    }

    @Override
    public void update(BotLanguage language)
    {
        this.language = language;
    }
}