package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    /** Создание HashMap для сохранения информации о пользователях. */
    private final Map<Long, User> users = new HashMap<>();

    /**
     * Получение всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    public User findById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь id = %d не найден", userId));
        }

        return users.get(userId);
    }

    /**
     * Добавление нового пользователя.
     *
     * @param user пользователь для добавления
     * @return добавленный пользователь с присвоенным id
     */
    @Override
    public User add(final User user) {
        checkName(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param newUser пользователь с обновленными данными
     * @return обновленный пользователь
     */
    @Override
    public User update(final User newUser) {
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
            return oldUser;
        }
        throw new NotFoundException(
                String.format("Пользователь id=%d не найден", newUser.getId())
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
}
