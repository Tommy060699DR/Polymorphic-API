package com.allobank.idr_rate_aggregator.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtils() {
        // Utility class - tidak perlu di-instantiate
    }

    /**
     * Parse string tanggal ke LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, ISO_FORMATTER);
    }

    /**
     * Format LocalDate ke string
     */
    public static String formatDate(LocalDate date) {
        return date.format(DISPLAY_FORMATTER);
    }

    /**
     * Mendapatkan tanggal hari ini dalam format string
     */
    public static String getTodayString() {
        return LocalDate.now().format(DISPLAY_FORMATTER);
    }
}
