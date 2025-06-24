package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * Сервисный класс для управления фильмами и их лайками.
 * <p>
 * Предоставляет методы для добавления, обновления, получения фильмов,
 * а также для управления лайками пользователей к фильмам.
 * <p>
 * Использует {@link InMemoryFilmStorage} для хранения данных о фильмах
 * и внутреннюю структуру данных для хранения лайков.
 */
@Service
@RequiredArgsConstructor
public class FilmService {
    private final Map<Long, Set<Long>> likesByFilm = new HashMap<>();
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    /**
     * Возвращает коллекцию всех фильмов.
     *
     * @return коллекция фильмов
     */
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    /**
     * Добавляет новый фильм в коллекцию.
     *
     * @param film фильм для добавления
     * @return добавленный фильм с присвоенным идентификатором
     */
    public Film add(Film film) {
        return filmStorage.add(film);
    }

    /**
     * Обновляет информацию о существующем фильме.
     *
     * @param newFilm фильм с обновлённой информацией
     * @return обновлённый фильм
     */
    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    /**
     * Добавляет лайк от пользователя к фильму.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя, ставящего лайк
     * @throws NotFoundException если фильм с указанным идентификатором не найден
     * @throws ConditionsNotMetException если пользователь уже поставил лайк этому фильму
     */
    public void like(Long filmId, Long userId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new NotFoundException(String.format("Фильм id = %d не найден", filmId));
        }

        User userVerification = userStorage.findById(userId);
        if (userVerification == null) {
            throw new NotFoundException(String.format("Пользователь id = %d не найден", userId));
        }

        Set<Long> likes = likesByFilm.computeIfAbsent(filmId, k -> new HashSet<>());

        if (likes.contains(userId)) {
            throw new ConditionsNotMetException(
                    String.format(
                            "Пользователь id = %d уже поставил лайк фильму id = %d",
                            userId,
                            filmId
                    )
            );
        }

        likes.add(userId);
    }

    /**
     * Удаляет лайк пользователя с фильма.
     * <p>
     * Если после удаления лайков у фильма не остаётся, запись о лайках для фильма удаляется.
     *
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя, удаляющего лайк
     */
    public void disLike(Long filmId, Long userId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new NotFoundException(String.format("Фильм id = %d не найден", filmId));
        }

        User userVerification = userStorage.findById(userId);
        if (userVerification == null) {
            throw new NotFoundException(String.format("Пользователь id = %d не найден", userId));
        }

        Set<Long> likes = likesByFilm.get(filmId);
        if (likes != null && likes.remove(userId)) {
            if (likes.isEmpty()) {
                likesByFilm.remove(filmId);
            }
        }
    }

    /**
     * Возвращает список популярных фильмов, отсортированных по количеству лайков.
     *
     * @param count количество фильмов для возврата;
     * если null или меньше либо равно нулю, используется значение 10
     * @return список популярных фильмов
     */
    public List<Film> popularFilms(Integer count) {
        int limit = (count == null || count <= 0) ? 10 : count;

        return likesByFilm.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .limit(limit)
                .map(entry -> {
                    Long filmId = entry.getKey();
                    return filmStorage.findById(filmId).orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
