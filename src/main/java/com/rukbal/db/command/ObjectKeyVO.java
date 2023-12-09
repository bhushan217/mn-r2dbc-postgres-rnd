package com.rukbal.db.command;

import com.rukbal.db.domain.BaseRecord;
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
        Short uiTypeId
) implements BaseRecord<Integer> {

}