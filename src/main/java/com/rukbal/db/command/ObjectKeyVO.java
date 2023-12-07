package com.rukbal.db.command;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record ObjectKeyVO(
    @Nullable
    Integer id,

    @NotBlank
    String keyName,

    @NotNull
    Integer uiTypeId
) {

}