package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    /** Создание HashMap для сохранения информации о фильмах. */
    private final Map<Long, Film> films = new HashMap<>();

    /**
     * Возвращает коллекцию всех фильмов.
     * <p>
     * Метод можно переопределять для изменения поведения получения
     * списка фильмов.
     *
     * @return коллекция фильмов
     */
    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    public Optional<Film> findById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    /**
     * Добавляет новый фильм в коллекцию.
     *
     * @param film фильм для добавления
     */
    @Override
    public Film add(final Film film) {
        checkFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    /**
     * Обновляет информацию о существующем фильме.
     *
     * @param newFilm фильм с обновлённой информацией;
     *                должен содержать существующий id
     */
    @Override
    public Film update(final Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Не указан id фильма");
        }

        checkFilm(newFilm);

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        }
        throw new NotFoundException(
                String.format("Фильм с id = %d не найден", newFilm.getId())
        );
    }

    /**
     * Проверяет корректность данных фильма, в частности, дату релиза.
     * <p>
     * Если дата релиза фильма раньше 28 декабря 1895 года,
     * выбрасывает ValidationException с соответствующим сообщением.
     */
    public void checkFilm(final Film film) {
        LocalDate birthdayFilm = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(birthdayFilm)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    /**
     * Генерирует следующий уникальный идентификатор для фильма.
     *
     * @return следующий id
     */
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
