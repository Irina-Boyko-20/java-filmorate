package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

/**
 * Сервис для управления пользователями и их друзьями.
 * <p>
 * Обеспечивает операции создания, обновления пользователей,
 * а также добавления, удаления и получения друзей.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    /**
     * Хранилище пользователей.
     */
    private final InMemoryUserStorage userStorage;

    /**
     * Возвращает коллекцию всех пользователей.
     *
     * @return коллекция всех пользователей
     */
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    /**
     * Добавляет нового пользователя в хранилище.
     *
     * @param user пользователь для добавления
     * @return добавленный пользователь с присвоенным идентификатором
     */
    public User add(final User user) {
        return userStorage.add(user);
    }

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param newUser пользователь с обновленными данными
     * @return обновлённый пользователь
     */
    public User update(final User newUser) {
        return userStorage.update(newUser);
    }

    /**
     * Добавляет пользователя с {@code friendId} в друзья пользователя с {@code id}.
     * <p>
     * При этом связь дружбы устанавливается взаимно.
     * </p>
     *
     * @param id идентификатор пользователя, который добавляет друга
     * @param friendId идентификатор пользователя, который добавляется в друзья
     * @return список из двух пользователей: пользователя и его нового друга
     */
    public List<User> addFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ConditionsNotMetException("Нельзя добавить в друзья самого себя");
        }

        try {
            User user = userStorage.findById(id);
            User friend = userStorage.findById(friendId);
            user.addFriend(friendId);
            friend.addFriend(id);
        } catch (NotFoundException exception) {
            if (exception.getMessage().contains(String.valueOf(id))) {
                System.out.printf("Пользователь с id = %d не найден%n", id);
            } else if (exception.getMessage().contains(String.valueOf(friendId))) {
                System.out.printf("Пользователь с id = %d не найден%n", friendId);
            }
        }

        return Arrays.asList(userStorage.findById(id), userStorage.findById(friendId));
    }

    /**
     * Удаляет пользователя с {@code friendId} из друзей пользователя с {@code id}.
     * <p>
     * Связь дружбы удаляется взаимно.
     * </p>
     *
     * @param id идентификатор пользователя, у которого удаляется друг
     * @param friendId идентификатор пользователя, который удаляется из друзей
     * @return {@code true}, если удаление прошло успешно, иначе {@code false}
     */
    public boolean deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь id = %d не найден%n", id));
        }
        if (friend == null) {
            throw new NotFoundException(String.format("Пользователь id = %d не найден%n", friendId));
        }

        Set<Long> friendsOfUser = user.getFriends();
        if (friendsOfUser == null || !friendsOfUser.contains(friendId)) {
            log.debug("Пользователь id = {} не является другом пользователя id = {}", id, friendId);
            return false;
        }

        friendsOfUser.remove(friendId);

        Set<Long> friendsOfFriend = friend.getFriends();
        friendsOfFriend.remove(id);

        return true;
    }

    /**
     * Возвращает список друзей пользователя с заданным идентификатором.
     *
     * @param id идентификатор пользователя
     * @return список друзей пользователя; пустой список, если друзей нет
     */
    public List<User> getFriends(Long id) {
        User user = userStorage.findById(id);
        Set<Long> friendsIds = user.getFriends();
        if (friendsIds == null || friendsIds.isEmpty()) {
            log.debug("Получен пустой список");
            return Collections.emptyList();
        }

        return friendsIds.stream()
                .map(this::findById)
                .toList();
    }

    /**
     * Возвращает список общих друзей двух пользователей.
     *
     * @param id идентификатор первого пользователя
     * @param otherId идентификатор второго пользователя
     * @return список общих друзей; пустой список,
     * если один из пользователей не найден или общих друзей нет
     */
    public List<User> mutualFriends(Long id, Long otherId) {
        if (findById(id) == null || findById(otherId) == null) {
            return Collections.emptyList();
        }

        return getFriends(id).stream()
                .filter(f -> getFriends(otherId).contains(f))
                .toList();
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     */
    public User findById(long id) {
        return userStorage.findById(id);
    }
}
