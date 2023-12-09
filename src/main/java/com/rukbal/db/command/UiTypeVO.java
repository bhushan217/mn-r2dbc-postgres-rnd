package com.rukbal.db.command;

import com.rukbal.db.domain.BaseRecord;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Serdeable
public record UiTypeVO(
    @NotNull
    Short id,

    @NotBlank
    @Size(min=3, max=15)
    String name
) implements BaseRecord<Short> {

}