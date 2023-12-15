package com.rukbal.db.domain;

import com.rukbal.db.utils.CommonConst;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

import static com.rukbal.db.utils.CommonConst.*;

@MappedEntity("ui_type")
@Serdeable
public record UiType(

    @Id @Nullable
    @GeneratedValue(value = GeneratedValue.Type.SEQUENCE)
    Short id,

    @NonNull @NotBlank
    @Size(min = SIZE_XS)
    @Index(name = "ui_key_UK", columns = {"name"}, unique = true)
    String name,
    @Nullable
    @Size(max = SIZE_L)
    String description,
    @Nullable
    @Size(max = SIZE_S)
    String pattern,

    @Nullable
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "uiType"/*, cascade = {Relation.Cascade.UPDATE}*/)
    Set<ObjectKey> objectKeys
) implements BaseRecord<Short> {
}
