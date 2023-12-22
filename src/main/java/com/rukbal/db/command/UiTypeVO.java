package com.rukbal.db.command;

import com.rukbal.db.domain.BaseRecord;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.rukbal.db.utils.CommonConst.SIZE_L;
import static com.rukbal.db.utils.CommonConst.SIZE_S;

@Schema(description="UI Component")
@Serdeable
public record UiTypeVO(

    @Schema(description="ID")
    @NotNull
    Short id,

    @Schema(description="Name of UI Component")
    @NotBlank
    @Size(min=3, max=15)
    String name,

    @Schema(description="Description of UI Component")
    @Nullable
    @Size(max = SIZE_L)
    String description,
    
    @Schema(description="Pattern / Regular Expression")
    @Nullable
    @Size(max = SIZE_S)
    String pattern,

    @Schema(description = "Version number")
    @Nullable
    Short version
) implements BaseRecord<Short> {

}