package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение, сигнализирующее о несоответствии условий.
 */
public class ConditionsNotMetException extends RuntimeException {
    /**
     * Конструктор исключения с сообщением.
     *
     * @param message описание причины исключения
     */
    public ConditionsNotMetException(final String message) {
        super(message);
    }
}
