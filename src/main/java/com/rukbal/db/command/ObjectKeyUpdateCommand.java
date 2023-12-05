package com.rukbal.db.command;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record ObjectKeyUpdateCommand (
    @NotNull
    Integer id,

    @NotBlank
    String keyName,

    @NotBlank
    String uiType
) {

}