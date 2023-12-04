package com.rukbal.db.repo;

import com.rukbal.db.dto.ObjectKey;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import io.micronaut.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface ObjectKeyRepository extends ReactorPageableRepository<ObjectKey, Long> {

    Mono<ObjectKey> findByKeyName(@NonNull String keyName);

    @Transactional
    default Mono<ObjectKey> saveWithException(ObjectKey objectKey) {
        return save(objectKey)
                .then(Mono.error(new DataAccessException("test exception")));
    }

    Mono<Long> update(@NonNull ObjectKey oObjectKey);
}
