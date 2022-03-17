package ru.liga.medvedev.telegram.bot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends ServiceCommand {

    private Logger logger = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        //logger.debug(String.format("Пользователь %s. Начато выполнение команды %s", this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), "Давайте начнём! Если Вам нужна помощь, нажмите /help");
        //logger.debug(String.format("Пользователь %s. Завершено выполнение команды %s", this.getCommandIdentifier()));
    }
}