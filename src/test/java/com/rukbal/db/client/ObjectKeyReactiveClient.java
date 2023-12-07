package com.rukbal.db.client;

import com.rukbal.db.command.ObjectKeyVO;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

@Client("/objectKeys")
@Validated
public interface ObjectKeyReactiveClient {

    @Get("/{id}")
    Mono<ObjectKeyVO> show(@NotNull Integer id);
    @Put
    Mono<ObjectKeyVO> update(@Body @Valid ObjectKeyVO objectKeyVO);
    @Get("/list")
    @NonNull Mono<Page<ObjectKeyVO>> list(@Valid Pageable pageable);

    @Post
    Mono<ObjectKeyVO> save(@Body @Valid ObjectKeyVO objectKeyVO);


    @Delete("/{id}")
    @NonNull Mono<Long> delete(Integer id);

    @Delete
    Mono<Long> deleteAll();
}
