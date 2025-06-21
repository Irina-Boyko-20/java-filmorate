package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

import java.util.List;

/**
 * Исключение, сигнализирующее о несоответствии условий или
 * валидационных ошибок.
 * <p>
 * Это исключение содержит список ошибок, что позволяет передавать
 * несколько сообщений об ошибках одновременно.
 */
@Getter
public class ValidationException extends RuntimeException {

    /**
     * Список ошибок, связанных с валидацией.
     */
    private final List<String> errors;

    /**
     * Конструктор исключения с сообщением.
     *
     * @param message описание причины исключения
     */
    public ValidationException(final String message) {
        super(message);
        this.errors = List.of(message);
    }
}
