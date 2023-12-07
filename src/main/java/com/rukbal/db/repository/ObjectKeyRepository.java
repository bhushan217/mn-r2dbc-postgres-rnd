package com.rukbal.db.repository;

import com.rukbal.db.domain.ObjectKey;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import io.micronaut.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface ObjectKeyRepository extends ReactorPageableRepository<ObjectKey, Integer> {

    @Join(value = "uiType",alias = "ut")
    @NonNull
    Mono<Page<ObjectKey>> findAll(@NonNull Pageable pageable);

    @Join(value = "uiType",alias = "ut")
    @Override
    @NonNull
    Mono<ObjectKey> findById(@NonNull Integer id);

    Mono<ObjectKey> findByKeyName(@NonNull String keyName);

    @Join(value = "uiType",alias = "ut")
    @NonNull
    @Transactional
    default Mono<ObjectKey> saveElseThrowException(@NonNull ObjectKey objectKey) {
        return save(objectKey).onErrorMap(Exception.class, e -> e.getMessage().contains("_UK") ? new DataAccessException("Record "+objectKey.keyName()+" already exists") : new RuntimeException(e.getMessage()));
    }

    Mono<ObjectKey> update(@NonNull ObjectKey oObjectKey);
}
