package ru.liga.medvedev;

import ru.liga.medvedev.domain.Rate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CreateRandomDataListEmulLoad {
    public static final Random RANDOM = new Random();
    public static final String FILE_HEADER = "nominal;data;curs;cdx";
    public static final String CURRENCY = "USD";

    public static List<List<String>> emulationListRandomDataFromLocalFile() {
        List<List<String>> listRates = new ArrayList(Arrays.asList(new ArrayList(Arrays.asList(FILE_HEADER.split(";")))));
        for (int i = 1; i < RANDOM.nextInt(2000 - 1000) + 1000; i++) {
            long minDay = LocalDate.of(2018, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2022, 2, 28).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            String[] strings = {
                    String.valueOf((RANDOM.nextInt(100 - 1) + 1)),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.ofEpochDay(randomDay)),
                    String.valueOf(Math.abs(((RANDOM.nextDouble() * 100) - 50))),
                    "валюта"};
            listRates.add(Arrays.asList(strings));
        }
        System.out.println(listRates);
        return listRates;
    }

    public static List<Rate> emulationRateStatistic(){
        List<Rate> listRate = new ArrayList<>();
        for (int i = 1; i < RANDOM.nextInt(2000 - 1000) + 1000; i++) {
            long minDay = LocalDate.of(2018, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2022, 2, 28).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            listRate.add(new Rate(CURRENCY, LocalDate.ofEpochDay(randomDay), Math.abs(((RANDOM.nextDouble() * 100) - 50))));
        }
        return listRate;
    }
}
