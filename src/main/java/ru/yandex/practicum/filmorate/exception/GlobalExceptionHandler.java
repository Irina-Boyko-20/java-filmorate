package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для контроллеров.
 * <p>
 * Обрабатывает исключения, связанные с неправильными аргументами методов,
 * отсутствием ресурсов, условиями, которые не были выполнены, и валидацией.
 * <p>
 * Все обработчики возвращают структурированный ответ с информацией об ошибке,
 * временем и соответствующим HTTP статусом.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {
    /**
     * Константы для статусов HTTP 404 (Bad BAD_REQUEST),
     * 400 (NOT_FOUND), 500 (SERVER_ERROR).
     */
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

    /**
     * Обрабатывает исключения, возникающие при неправильных аргументах метода.
     *
     * @param ex исключение MethodArgumentNotValidException
     * @return ResponseEntity с информацией об ошибке и статусом 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleExceptions(
            final MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_BAD_REQUEST);

        // Собираем все сообщения ошибок в список
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errorMessages", errors);
        log.warn(String.valueOf(errors));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения, связанные с отсутствием ресурса.
     *
     * @param ex исключение NotFoundException
     * @return ResponseEntity с информацией об ошибке и статусом 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(
            final NotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_NOT_FOUND);
        body.put("error", ex.getMessage());

        log.warn(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключения условий, которые не были выполнены.
     *
     * @param ex исключение ConditionsNotMetException
     * @return ResponseEntity с информацией об ошибке и статусом 400
     */
    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<Map<String, Object>> handleConditionsNotMetException(
            final ConditionsNotMetException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_BAD_REQUEST);
        body.put("error", ex.getMessage());

        log.warn(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает пользовательское исключение ValidationException,
     * связанное с ошибками валидации данных.
     *
     * @param ex экземпляр ValidationException
     * @return ResponseEntity с информацией об ошибках и статусом 400
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> exception(
            final ValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HTTP_STATUS_BAD_REQUEST);
        body.put("errorMessages", ex.getErrors());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
