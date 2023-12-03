package com.rukbal.db.repo;

import com.rukbal.db.dto.ObjectKey;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface ObjectKeyRepository extends ReactorPageableRepository<ObjectKey, Long> {

    Mono<ObjectKey> findByKeyName(@NonNull String keyName);
    Mono<ObjectKey> save(@Id long id, @NotBlank String keyName, @NotBlank String uiType);

    @Transactional
    default Mono<ObjectKey> saveWithException(@Id long id, @NotBlank String name, @NotBlank String uiType) {
        return save(id, name, uiType)
                .then(Mono.error(new DataAccessException("test exception")));
    }

    Mono<Long> update(@Id long id, @NotBlank String keyName);
}
