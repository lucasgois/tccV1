package com.github.lucasgois.tcc.servidor.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(@NotNull final Exception ex) {
        log.error("handleException", ex);

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(@NotNull final MethodArgumentNotValidException ex) {
        final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("message", "Validation errors");

        final List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .toList();

        body.put("errors", errors);

        return new ResponseEntity<>(body, status);
    }
}
