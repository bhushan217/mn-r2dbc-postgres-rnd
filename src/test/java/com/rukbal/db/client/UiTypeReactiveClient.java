package com.rukbal.db.client;

import com.rukbal.db.command.UiTypeVO;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@Client("/uiTypes")
@Validated
public interface UiTypeReactiveClient {

    @Get("/{id}")
    Mono<UiTypeVO> show(@NonNull Integer id);
    @Put
    Mono<UiTypeVO> update(@Body @Valid UiTypeVO uiTypeVO);
    @Get("/list")
    @NonNull Mono<Page<UiTypeVO>> list(@Valid Pageable pageable);

    @Post
    Mono<UiTypeVO> save(@Body @Valid UiTypeVO objectKey);

    @Post("/ex")
    Mono<UiTypeVO> saveElseThrowException(@Body @Valid UiTypeVO objectKey);

    @Delete("/{id}")
//    @Status(HttpStatus.NO_CONTENT)
    @NonNull Mono<Long> delete(Integer id);

    @Delete
//    @Status(HttpStatus.NO_CONTENT)
    Mono<Long> deleteAll();
}
