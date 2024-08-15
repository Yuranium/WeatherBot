package ru.weather.bot.weatherbot.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@PropertySource("application.properties")
public class BotConfig
{
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private Integer messageId;

    @Bean
    public Deque<BotApiMethod<?>> stack()
    {
        return new ArrayDeque<>();
    }
}