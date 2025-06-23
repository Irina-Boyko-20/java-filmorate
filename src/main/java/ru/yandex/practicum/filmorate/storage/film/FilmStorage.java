package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

/**
 * Интерфейс для хранилища фильмов.
 * <p>
 * Определяет базовые операции для добавления, обновления и получения коллекции фильмов.
 */
interface FilmStorage {

    /**
     * Возвращает коллекцию всех фильмов, содержащихся в хранилище.
     */
    Collection<Film> findAll();

    /**
     * Добавляет новый фильм в хранилище.
     */
    Film add(final Film film);

    /**
     * Обновляет существующий фильм в хранилище.
     */
    Film update(final Film newFilm);
}
