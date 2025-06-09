package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Модель пользователя с валидацией.
 */
@Data
@Builder
public class User {
    /**
     * Идентификатор пользователя.
     */
    private Long id;
    /**
     * Электронная почта пользователя.
     */
    @NotBlank(message = "Электронная почта не указана")
    @Email(message = "Некорректный формат электронной почты")
    private String email;
    /**
     * Логин пользователя.
     */
    @NotBlank(message = "Логин не указан")
    private String login;
    /**
     * Имя для отображения.
     */
    private String name;
    /**
     * Дата рождения.
     */
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
