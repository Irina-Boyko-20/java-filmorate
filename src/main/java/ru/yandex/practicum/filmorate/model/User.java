package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * Модель пользователя с валидацией.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    /**
     * Идентификатор пользователя.
     */
    Long id;
    /**
     * Электронная почта пользователя.
     */
    @NotBlank(message = "Электронная почта не указана")
    @Email(message = "Некорректный формат электронной почты")
    String email;
    /**
     * Логин пользователя.
     */
    @NotBlank(message = "Логин не указан")
    String login;
    /**
     * Имя для отображения.
     */
    String name;
    /**
     * Дата рождения.
     */
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}
