package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Основной класс запуска приложения Filmorate.
 */
@SpringBootApplication
public class FilmorateApplication {
    /**
     * Главный метод запуска приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }
}
