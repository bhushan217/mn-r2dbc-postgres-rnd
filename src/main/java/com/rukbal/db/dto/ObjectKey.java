package com.rukbal.db.dto;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

/**
 *
 */
@Serdeable
@MappedEntity("object_key")
public class ObjectKey {
    @Id
//    @GeneratedValue(GeneratedValue.Type.AUTO)
    Long id;

    @NotBlank
    String keyName;

    String uiType;

    public ObjectKey(Long id, String keyName, String uiType) {
        this.id = id;
        this.keyName = keyName;
        this.uiType = uiType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectKey that)) return false;
        return Objects.equals(getId(), that.getId()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ObjectKeyDTO{" +
                "id=" + id +
                ", keyName='" + keyName + '\'' +
                ", uiType='" + uiType + '\'' +
                '}';
    }
}
