package com.rukbal.db.error;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record AppError(String status, String message) {
}
