package com.rukbal.db.dto;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

/**
 *
 */
@Serdeable
@MappedEntity("object_key")
public record ObjectKey(
        @Id
//        @AutoPopulated
        @GeneratedValue(value = GeneratedValue.Type.SEQUENCE, ref = "object_key_seq")
//                @ColumnTransformer("")
        Integer id,

        @NotBlank
        String keyName,

        String uiType

) {}
