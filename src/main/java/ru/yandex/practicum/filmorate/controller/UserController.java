package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@Validated
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Получение всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        return userService.findAll();
    }

    /**
     * Добавление нового пользователя.
     *
     * @param user пользователь для добавления
     * @return добавленный пользователь с присвоенным id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User add(@Valid @RequestBody final User user) {
        log.debug("Добавлен новый пользователь: {}", user);
        return userService.add(user);
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param newUser пользователь с обновленными данными
     * @return обновленный пользователь
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody final User newUser) {
        log.debug("Обновлен пользователь: {}", newUser);
        return userService.update(newUser);
    }

    /**
     * Добавляет пользователя с идентификатором {@code friendId}
     * в список друзей пользователя с идентификатором {@code id}.
     *
     * @param id идентификатор пользователя, которому добавляют друга
     * @param friendId идентификатор пользователя, который добавляется в друзья
     * @return обновлённый список друзей пользователя с {@code id}
     */
    @PutMapping({"/{id}/friends/{friendId}"})
    @ResponseStatus(HttpStatus.OK)
    public List<User> addFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        log.debug("Пользователь c id = {} добавил в друзья пользователя с id = {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    /**
     * Удаляет пользователя с идентификатором {@code friendId}
     * из списка друзей пользователя с идентификатором {@code id}.
     *
     * @param id идентификатор пользователя, у которого удаляют друга
     * @param friendId идентификатор пользователя, который удаляется из друзей
     * @return {@code true}, если удаление прошло успешно, иначе {@code false}
     */
    @DeleteMapping({"/{id}/friends/{friendId}"})
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        log.debug("Пользователь c id = {} удалил из друзей пользователя с id = {}", id, friendId);
        return userService.deleteFriend(id, friendId);
    }

    /**
     * Получает список друзей пользователя с идентификатором {@code id}.
     *
     * @param id идентификатор пользователя
     * @return список друзей пользователя
     */
    @GetMapping({"/{id}/friends"})
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable final Long id) {
        log.debug("Получен список пользователей, являющимися друзьями пользователя с id = {}", id);
        return userService.getFriends(id);
    }

    /**
     * Получает список общих друзей (пересечение списков друзей)
     * пользователей с идентификаторами {@code id} и {@code otherId}.
     *
     * @param id идентификатор первого пользователя
     * @param otherId идентификатор второго пользователя
     * @return список общих друзей двух пользователей
     */
    @GetMapping({"/{id}/friends/common/{otherId}"})
    @ResponseStatus(HttpStatus.OK)
    public List<User> mutualFriends(@PathVariable final Long id, @PathVariable final Long otherId) {
        log.debug("Получен список друзей пользователя с id = {}, общих с пользователем с id = {}", id, otherId);
        return userService.mutualFriends(id, otherId);
    }
}
