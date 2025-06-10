package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * Модель фильма с валидацией и переопределением методов equals и hashCode.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    /**
     * Константа максимального количества символов.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    /**
     * Идентификатор фильма.
     */
    Long id;
    /**
     * Название фильма.
     */
    @NotBlank(message = "Название фильма не указано")
    String name;
    /**
     * Описание фильма.
     */
    @Size(max = MAX_DESCRIPTION_LENGTH,
            message = "Описание фильма превышает 200 символов")
    @NotBlank(message = "Описание фильма не указано")
    String description;
    /**
     * Дата релиза.
     */
    @NotNull(message = "Дата релиза обязательна")
    LocalDate releaseDate;
    /**
     * Продолжительность фильма в минутах.
     */
    @Positive(message = "Продолжительность фильма должна быть более 1 минуты")
    Integer duration;
}
