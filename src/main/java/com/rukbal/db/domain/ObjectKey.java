package com.rukbal.db.domain;

import io.micronaut.core.annotation.Indexed;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.data.annotation.sql.JoinColumn;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 */
@Serdeable
@MappedEntity("object_key")
public record ObjectKey(
        @Id @Nullable
        @GeneratedValue(value = GeneratedValue.Type.SEQUENCE, ref = "object_key_seq")
        Integer id,

        @NotBlank @NonNull
        @Size(max = 63)
        @Index(name = "object_key_UK", columns = {"key_name"}, unique = true)
        String keyName,

//        @Nullable
        @NonNull @NotBlank
        @Indexed(value = String.class)
        @Size(max = 15)
        @Relation(Relation.Kind.ONE_TO_ONE)
        @JoinColumn(name = "ui_type_id")
        UiType uiType
) {}
