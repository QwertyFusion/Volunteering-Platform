package com.example.volunteer_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class DateFormatterConfig {

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            // Custom date format: dd-MM-yyyy
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String print(LocalDate object, Locale locale) {
                return object != null ? formatter.format(object) : null;
            }

            @Override
            public LocalDate parse(String text, Locale locale) {
                return text != null && !text.isEmpty() ? LocalDate.parse(text, formatter) : null;
            }
        };
    }
}
