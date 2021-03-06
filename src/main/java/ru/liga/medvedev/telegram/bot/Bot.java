package ru.liga.medvedev.telegram.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.medvedev.telegram.bot.command.HelpCommand;
import ru.liga.medvedev.telegram.bot.command.StartCommand;
import ru.liga.medvedev.telegram.bot.command.StopCommand;
import ru.liga.medvedev.telegram.bot.nonCommand.NonCommand;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class Bot extends TelegramLongPollingCommandBot implements AnswerMessage {
    private final String BOT_TOKEN;
    private final String BOT_NAME;
    private final NonCommand nonCommand;

    public Bot(String BOT_TOKEN, String BOT_NAME) throws TelegramApiException {
        super();
        Logger logger = LoggerFactory.getLogger(Bot.class);
        logger.debug("Super constructor was worked");
        this.BOT_TOKEN = BOT_TOKEN;
        this.BOT_NAME = BOT_NAME;
        this.nonCommand = new NonCommand();

        register(new StartCommand("start", "Старт"));
        HelpCommand helpCommand = new HelpCommand("help", "Помощь");
        register(helpCommand);

        register(new StopCommand("stop", "стоп"));

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(String.valueOf(message.getChatId()));
            commandUnknownMessage.setText("Команда '" + message.getText() + "' не известна боту.\nДоступные команды\n/help\n/start\n");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Long chatId = update.getMessage().getChatId();
        Map<byte[], Boolean> outMapMessage = nonCommand.nonCommandExecute(update.getMessage().getText());
        for (Map.Entry<byte[], Boolean> entry : outMapMessage.entrySet()
        ) {
            answer(entry.getKey(), chatId, entry.getValue());
        }
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void answer(byte[] message, Long chatId, boolean typeAnswer) {
        if (typeAnswer) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            String text;
            text = new String(message, StandardCharsets.UTF_8);
            sendMessage.setText(text);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(chatId));
            try (InputStream inputStream = new ByteArrayInputStream(message)) {
                sendPhoto.setPhoto(new InputFile(inputStream, "Rates predict"));
                execute(sendPhoto);
            } catch (IOException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}