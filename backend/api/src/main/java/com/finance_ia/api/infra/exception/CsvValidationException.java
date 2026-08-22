package com.finance_ia.api.infra.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class CsvValidationException extends RuntimeException {

    private final List<ErrorDetail> errors;

    public CsvValidationException(List<ErrorDetail> errors) {
        super("El archivo CSV contiene datos inválidos.");
        this.errors = List.copyOf(errors);
    }
}
