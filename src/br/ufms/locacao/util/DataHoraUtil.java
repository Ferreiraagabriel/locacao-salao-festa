package br.ufms.locacao.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataHoraUtil {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DataHoraUtil() { }

    public static LocalDateTime lerDataHora(String data, String hora) {
        try {
            LocalDate dia = LocalDate.parse(data.trim(), FORMATO_DATA);
            LocalTime horario = LocalTime.parse(hora.trim(), FORMATO_HORA);
            return LocalDateTime.of(dia, horario);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Informe a data no formato DD/MM/AAAA e a hora no formato HH:MM.");
        }
    }

    public static String formatar(LocalDateTime dataHora) {
        return dataHora == null ? "-" : dataHora.format(FORMATO_DATA_HORA);
    }

    public static String formatarData(LocalDate data) {
        return data.format(FORMATO_DATA);
    }
}
