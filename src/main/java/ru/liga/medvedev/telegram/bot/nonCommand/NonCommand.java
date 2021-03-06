package ru.liga.medvedev.telegram.bot.nonCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.medvedev.SpringConfiguration;
import ru.liga.medvedev.domain.Command;
import ru.liga.medvedev.domain.Rate;
import ru.liga.medvedev.domain.enums.RateOutTypes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonCommand {
    private final Logger logger = LoggerFactory.getLogger(NonCommand.class);

    public Map<byte[], Boolean> nonCommandExecute(String messageText) {
        logger.info("Строка команды от бота\n {}", messageText);

        Command command = SpringConfiguration.COMMAND_CONTROLLER.getCommand(messageText);
        logger.info("Обработанная команда\n {}", command);

        Map<byte[], Boolean> outMapMessage = new HashMap<>();
        List<List<Rate>> listRates = new ArrayList<>();

        if (command.getErrorMessage() != null) {
            outMapMessage.put(command.getErrorMessage().getBytes(StandardCharsets.UTF_8), true);
        } else {
            for (String currency : command.getListCurrency()
            ) {
                List<Rate> listRate = SpringConfiguration.ALGORITHMS_RATE_CONTROLLER.generateStatisticRateCurrency(
                        SpringConfiguration.RATE_DATA_MAPPER.mapRate(
                                SpringConfiguration.DATA_REPOSITORY_CONTROLLER.getRateDataRepository(currency), currency), command);
                listRates.add(listRate);
            }
            outMapMessage.put(SpringConfiguration.OUT_RATE_STATISTIC_CONTROLLER.outRateStatistic(command, listRates),
                    !command.getOutputType().equals(RateOutTypes.GRAPH.name()));
        }
        return outMapMessage;
    }
}