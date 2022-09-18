package com.execute.protocol.auth.configs.converters;


import com.execute.protocol.core.exeptions.DateConvertException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.apache.commons.lang3.time.DateUtils.parseDateStrictly;


/**
 * Класс конвертации формат даты в yyyy-MM-dd
 */
public class JavaDateConverter {
    // Шаблоны форматов из которых мы будем формировать формат yyyy-MM-dd
    private static final String[] formats = {
            "yyyy.MM.dd",
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "yyyy-M-dd",
            "yyyy/M/dd",
            "dd.M.yyyy",
            "dd-M-yyyy",
            "dd/M/yyyy",
            "dd.MM.yyyy",
            "dd-MM-yyyy",
            "dd/MM/yyyy"
    };

    /**
     * Метод непосредственно проводящий конвертацию формата даты
     * @param date
     * @return LocalDate
     * */
    public static LocalDate parserToLocalDate(String date) {
        try {
            Date parseDate = parseDateStrictly(date, formats);
            DateFormat finalFormat = new SimpleDateFormat("yyyy-MM-dd");
            return LocalDate.parse(finalFormat.format(parseDate));
        } catch (ParseException e) {
            throw new DateConvertException("Ошибка при конвертации формата даты в формат yyyy-MM-dd");
        }
    }
}
