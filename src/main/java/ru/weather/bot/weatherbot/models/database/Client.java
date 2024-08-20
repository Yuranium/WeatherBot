package ru.weather.bot.weatherbot.models.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.weather.bot.weatherbot.enums.ClientRole;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "telegram_clients")
public class Client
{
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_registration", nullable = false)
    private Timestamp dateOfRegistration;

    @Column(name = "client_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ClientRole role;

    @Column(name = "username", nullable = false)
    private String userName;
}