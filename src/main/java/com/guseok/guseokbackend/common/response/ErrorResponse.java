package com.guseok.guseokbackend.common.response;

public record ErrorResponse(
    String code,
    String message
) {
}