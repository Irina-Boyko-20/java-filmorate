package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер для работы с фильмами.
 * <p>
 * Этот класс предназначен для обработки HTTP-запросов, связанных с фильмами.
 * <p>
 * Если вы планируете расширять этот класс, убедитесь, что переопределение
 * методов происходит безопасно, и ознакомьтесь с документацией по наследованию.
 */
@RestController
@Validated
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    /**
     * Возвращает коллекцию всех фильмов.
     * <p>
     * Метод можно переопределять для изменения поведения получения
     * списка фильмов.
     *
     * @return коллекция фильмов
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        log.debug("Получен список фильмов, количество = : {}", filmService.findAll().size());
        return filmService.findAll();
    }

    /**
     * Добавляет новый фильм в коллекцию.
     *
     * @param film фильм для добавления
     * @return добавленный фильм с присвоенным id и сохранёнными
     * данными
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film add(@Valid @RequestBody final Film film) {
        log.debug("Добавлен фильм с id = {}", film.getId());
        return filmService.add(film);
    }

    /**
     * Обновляет информацию о существующем фильме.
     *
     * @param newFilm фильм с обновлённой информацией
     * @return обновлённый фильм
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Valid @RequestBody final Film newFilm) {
        log.debug("Обновлён фильм с id = {}", newFilm.getId());
        return filmService.update(newFilm);
    }

    /**
     * Добавляет лайк к фильму от пользователя.
     *
     * @param id идентификатор фильма
     * @param userId идентификатор пользователя, который ставит лайк
     * @return сообщение об успешном добавлении лайка
     */
    @PutMapping({"/{id}/like/{userId}"})
    @ResponseStatus(HttpStatus.OK)
    public void like(@PathVariable Long id, @PathVariable Long userId) {
        filmService.like(id, userId);
        log.debug("Пользователь id = {} лайкнул фильм id = {}", userId, id);
    }

    /**
     * Удаляет лайк с фильма от пользователя.
     *
     * @param id идентификатор фильма
     * @param userId идентификатор пользователя, который удаляет лайк
     * @return сообщение об успешном удалении лайка (дизлайке)
     */
    @DeleteMapping({"/{id}/like/{userId}"})
    @ResponseStatus(HttpStatus.OK)
    public void disLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.disLike(id, userId);
        log.debug("Пользователь id = {} дизлайкнул фильм id = {}", userId, id);
    }

    /**
     * Возвращает список популярных фильмов.
     *
     * @param count количество фильмов для отображения; если не указано,
     * может использоваться значение по умолчанию
     * @return список популярных фильмов, отсортированных по количеству лайков
     */
    @GetMapping({"/popular"})
    @ResponseStatus(HttpStatus.OK)
    public List<Film> popularFilms(@RequestParam("count") Integer count) {
        log.debug("Получен список из первых {} фильмов по количеству лайков", count);
        return filmService.popularFilms(count);
    }
}
