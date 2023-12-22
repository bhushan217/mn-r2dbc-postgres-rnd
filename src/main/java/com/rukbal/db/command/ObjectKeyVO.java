package com.rukbal.db.command;

import com.rukbal.db.domain.BaseRecord;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.rukbal.db.utils.CommonConst.*;

@Schema(name = "ObjectKeyVO", description = "Object Key")
@Serdeable
public record ObjectKeyVO(
        @Nullable
        Integer id,

        @NotBlank @NonNull
        @Size(max = SIZE_S)
        String keyName,

        @NotBlank @NonNull
        @Size(max = SIZE_M)
        String label,

        @Size(max = SIZE_L)
        String description,

        @NotNull
        Short uiTypeId,

        @Nullable
        Integer version
) implements BaseRecord<Integer> {

}