package com.finance_ia.api.infra.exception;

public record ErrorDetail(
        String field,
        String message,
        Integer row
) {
}
