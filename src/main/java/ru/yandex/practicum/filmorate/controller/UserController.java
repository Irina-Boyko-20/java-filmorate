package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@Validated
@RequestMapping("/users")
@Slf4j
public class UserController {
    /**
     * Хранение пользователей в памяти.
     */
    private final Map<Long, User> users = new HashMap<>();

    /**
     * Получение всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    /**
     * Добавление нового пользователя.
     *
     * @param user пользователь для добавления
     * @return добавленный пользователь с присвоенным id
     */
    @PostMapping
    public User add(@Valid @RequestBody final User user) {
        checkName(user);

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь с id = {} успешно добавлен", user.getId());
        return user;
    }

    /**
     * Генерация следующего уникального id.
     *
     * @return следующий id
     */
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param newUser пользователь с обновленными данными
     * @return обновленный пользователь
     */
    @PutMapping
    public User update(@Valid @RequestBody final User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Не указан id пользователя");
        }

        checkName(newUser);

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("пользователь с id={} успешно обновлён", newUser.getId());
            return oldUser;
        }
        throw new NotFoundException(
                String.format("Пользователь с id=%d не найден", newUser.getId())
        );
    }

    /**
     * Проверка и установка имени пользователя, если оно отсутствует.
     *
     * @param user пользователь для проверки
     */
    public void checkName(@RequestBody final User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
