package ru.weather.bot.weatherbot.models.database;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.enums.ClientRole;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService
{
    private final ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository repository)
    {
        this.repository = repository;
    }

    @Transactional
    public Optional<Client> findById(Long id)
    {
        return repository.findById(id);
    }

    @Transactional
    public List<Client> findAll()
    {
        return (List<Client>) repository.findAll();
    }

    @Transactional
    public void registeredClient(Update update)
    {
        Optional<Client> client = findById(update.getMessage().getChatId());
        if (client.isEmpty())
            saveClient(update.getMessage());
    }

    @Transactional
    public void saveClient(Message message)
    {
        Client client = new Client()
            .setChatId(message.getChatId())
            .setFirstName(message.getChat().getFirstName())
            .setLastName(message.getChat().getLastName())
            .setDateOfRegistration(new Date())
            .setRole(ClientRole.CLIENT)
            .setUserName(message.getChat().getUserName());
        repository.save(client);

    }

    public String sendCommandMessage(String message)
    {
        return message.substring(message.indexOf(" ") + 1);
    }
}