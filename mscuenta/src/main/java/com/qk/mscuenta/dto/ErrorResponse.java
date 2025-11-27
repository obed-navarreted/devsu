package com.qk.mscuenta.dto;


public record ErrorResponse(
        String path,
        String method,
        String timestamp,
        String error,
        Object details) {
}
