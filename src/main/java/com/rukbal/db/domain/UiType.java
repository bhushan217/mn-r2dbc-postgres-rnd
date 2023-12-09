package com.rukbal.db.domain;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@MappedEntity("ui_type")
@Serdeable
public record UiType(

    @Id @Nullable
    @GeneratedValue(value = GeneratedValue.Type.SEQUENCE)
    Short id,

    @NonNull @NotBlank
    @Size(min = 5)
    @Index(name = "ui_key_UK", columns = {"name"}, unique = true)
    String name,

    @Nullable
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "uiType"/*, cascade = {Relation.Cascade.UPDATE}*/)
    Set<ObjectKey> objectKeys
) implements BaseRecord<Short> {
}
