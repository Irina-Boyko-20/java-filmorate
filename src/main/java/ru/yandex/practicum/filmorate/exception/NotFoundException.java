package ru.yandex.practicum.filmorate.exception;

/**
 * Исключение, сигнализирующее о том, что запрашиваемый объект не найден.
 */
public class NotFoundException extends RuntimeException {
    /**
     * Конструктор исключения с сообщением.
     *
     * @param message описание причины исключения
     */
    public NotFoundException(final String message) {
        super(message);
    }
}
