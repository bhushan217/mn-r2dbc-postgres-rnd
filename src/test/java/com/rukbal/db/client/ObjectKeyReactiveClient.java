package com.rukbal.db.client;

import com.rukbal.db.command.ObjectKeyUpdateCommand;
import com.rukbal.db.dto.ObjectKey;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@Client("/objectKeys")
public interface ObjectKeyReactiveClient {

    @Get("/{id}")
    Mono<ObjectKey> show(Long id);
    @Put
    Mono<Long> update(@Body @Valid ObjectKeyUpdateCommand command);
    @Get("/list")
    @NonNull Mono<Page<ObjectKey>> list(@Valid Pageable pageable);

    @Post
    Mono<ObjectKey> save(@Body @Valid ObjectKey objectKey);

    @Post("/ex")
    Mono<ObjectKey> saveExceptions(@Body @Valid ObjectKey objectKey);

    @Delete("/{id}")
//    @Status(HttpStatus.NO_CONTENT)
    @NonNull Mono<Long> delete(Long id);

    @Delete
//    @Status(HttpStatus.NO_CONTENT)
    Mono<Long> deleteAll();
}
