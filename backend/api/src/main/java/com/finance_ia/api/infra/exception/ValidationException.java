package com.finance_ia.api.infra.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private final List<ErrorDetail> errors;

    public ValidationException(List<ErrorDetail> errors) {
        super("Se encontraron errores de validación.");
        this.errors = List.copyOf(errors);
    }

}