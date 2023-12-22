package com.rukbal.db.domain;

public interface BaseRecord<I> {

    I id();

    I version();
}
