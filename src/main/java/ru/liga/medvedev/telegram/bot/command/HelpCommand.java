package ru.liga.medvedev.telegram.bot.command;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class HelpCommand extends ServiceCommand {

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), "Ведите входные данные для прогноза курса валют, в формате: 'курс валюта период алгоритм'.\n\n" +
                "Пример строки:\n " +
                "*rate TRY -date 22.02.2030 -alg moon*");
    }
}