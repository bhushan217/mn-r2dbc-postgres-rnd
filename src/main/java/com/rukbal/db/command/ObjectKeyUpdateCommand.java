package com.rukbal.db.command;

import com.rukbal.db.dto.ObjectKey;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record ObjectKeyUpdateCommand (
    @NotNull
    Long id,

    @NotBlank
    String KeyName,

    @NotBlank
    String uiType
) {

}