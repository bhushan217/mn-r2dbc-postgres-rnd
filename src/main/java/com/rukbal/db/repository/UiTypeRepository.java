package com.rukbal.db.repository;

import com.rukbal.db.domain.UiType;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import io.micronaut.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface UiTypeRepository extends ReactorPageableRepository<UiType, Short> {

    @Override
    @NonNull
    Flux<UiType> findAll();

    Mono<UiType> findByName(String name);

    @NonNull
    @Transactional
    default Mono<UiType> saveElseThrowException(@NonNull UiType uiType) {
        return save(uiType)
                .onErrorMap(Exception.class, e -> {
                    if (e.getMessage().contains("_UK")) {
                        return new DataAccessException("Record " + uiType.name() + " already exists");
                    }
                    return new RuntimeException(e.getMessage());
                });
    }
}
