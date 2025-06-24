package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

/**
 * Интерфейс для хранения и управления данными пользователей.
 * <p>
 * Определяет основные операции для работы с коллекцией пользователей,
 * включая получение всех пользователей, добавление нового пользователя
 * и обновление существующего.
 * </p>
 */
public interface UserStorage {

    /**
     * Возвращает коллекцию всех пользователей.
     */
    Collection<User> findAll();

    /**
     * Добавляет нового пользователя в хранилище.
     */
    User add(final User user);

    /**
     * Обновляет данные существующего пользователя.
     */
    User update(final User newUser);
}
