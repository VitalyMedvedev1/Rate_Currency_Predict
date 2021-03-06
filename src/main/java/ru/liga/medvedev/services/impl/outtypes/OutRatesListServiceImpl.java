package ru.liga.medvedev.services.impl.outtypes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.medvedev.controller.OutRateStatistic;
import ru.liga.medvedev.domain.Command;
import ru.liga.medvedev.domain.Rate;
import ru.liga.medvedev.domain.StaticParams;
import ru.liga.medvedev.domain.enums.RatePeriods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("OutDataRateListService")
public class OutRatesListServiceImpl extends OutRateStatisticService implements OutRateStatistic {

    @Override
    public byte[] outRateStatistic(Command command, List<List<Rate>> listRates) {
        log.debug("Формирование списка ответа статистики\n {} \nна период - {}", listRates, command.getPeriod());
        byte[] outMessageByte;
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            for (List<Rate> listRate : listRates) {
                outStream.write(("Статистика по валюте - " + listRate.get(StaticParams.HEADER_INDEX).getCurrency() + "\n").getBytes(StandardCharsets.UTF_8));
                outStream.write(listRate.stream()
                        .skip(command.getPeriod().toUpperCase().equals(RatePeriods.DATE.name()) ? 0 : 1)
                        .limit(OutRatesStatisticLength(command))
                        .map(Rate::toString)
                        .collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8));
                outStream.write("\n\n\n".getBytes(StandardCharsets.UTF_8));
            }
            outMessageByte = outStream.toByteArray();
        } catch (IOException e) {
            log.error("\"Ошибка сохранения граффика статистики! {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return outMessageByte;
    }
}