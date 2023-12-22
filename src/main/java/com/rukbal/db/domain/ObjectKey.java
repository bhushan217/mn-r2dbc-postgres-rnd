package com.rukbal.db.domain;

import io.micronaut.core.annotation.Indexed;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.data.annotation.sql.JoinColumn;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.rukbal.db.utils.CommonConst.*;
import static io.micronaut.data.annotation.GeneratedValue.Type.SEQUENCE;
import static io.micronaut.data.annotation.Relation.Kind.ONE_TO_ONE;

/**
 *
 */
@Serdeable
@MappedEntity("object_key")
@Index(name = "object_key_UK", columns = {"key_name"}, unique = true)
public record ObjectKey(
        @Id @Nullable
        @GeneratedValue(value = SEQUENCE, ref = "object_key_seq")
        Integer id,

        @NotBlank @NonNull
        @Size(max = SIZE_S)
        String keyName,

        @NotBlank @NonNull
        @Size(max = SIZE_M)
        String label,

        @Size(max = SIZE_L)
        String description,

//        @Nullable
        @NonNull @NotBlank
        @Indexed(value = String.class)
        @Size(max = SIZE_XS)
        @Relation(ONE_TO_ONE)
        @JoinColumn(name = "ui_type_id")
        UiType uiType,

        @Version
//        @AutoPopulated
        Integer version
) implements BaseRecord<Integer> {
}
