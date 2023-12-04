package com.rukbal.db.api;

import com.rukbal.db.command.ObjectKeyUpdateCommand;
import com.rukbal.db.dto.ObjectKey;
import com.rukbal.db.error.AppError;
import com.rukbal.db.repo.ObjectKeyRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import java.net.URI;

//@ExecuteOn(TaskExecutors.VIRTUAL)
@Controller("/objectKeys")
public class ObjectKeyController {

    protected final ObjectKeyRepository objectKeyRepository;

    public ObjectKeyController(ObjectKeyRepository objectKeyRepository) {
        this.objectKeyRepository = objectKeyRepository;
    }

    @Get("/{id}")
    public Mono<ObjectKey> show(Long id) {
        return objectKeyRepository.findById(id);
    }

    @Put
    public Mono<Long> update(@Body @Valid ObjectKeyUpdateCommand command) {
        return Mono.just(command)
                .map(cmd -> new ObjectKey(cmd.id(), cmd.KeyName(), cmd.uiType()))
                .flatMap(objectKeyRepository::update)
                .onErrorMap( e -> new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @Get("/list")
    public @NonNull Mono<Page<ObjectKey>> list(@Valid Pageable pageable) {
        return objectKeyRepository.findAll(pageable);
    }

    @Post
    public Mono<ObjectKey> save(@Body @Valid ObjectKey objectKey) {
       return objectKeyRepository.save(objectKey);
    }

    @Post("/ex")
    public Mono<ObjectKey> saveExceptions(@Body @Valid ObjectKey objectKey) throws HttpStatusException {
        return Mono.just(objectKey)
                .flatMap(objectKeyRepository::saveWithException)
                .onErrorMap( e -> new HttpStatusException(HttpStatus.EXPECTATION_FAILED, new AppError(HttpStatus.EXPECTATION_FAILED.getReason(), e.getMessage())));
    }

    @Delete("/{id}")
//    @Status(HttpStatus.NO_CONTENT)
    public @NonNull Mono<Long> delete(Long id) {
        return objectKeyRepository.deleteById(id);
    }



    @Delete
//    @Status(HttpStatus.NO_CONTENT)
    Mono<Long> deleteAll(){
        return objectKeyRepository.deleteAll();
    }

    protected URI location(Long id) {
        return URI.create("/objectKeys/" + id);
    }

    protected URI location(ObjectKey objectKey) {
        return location(objectKey.id());
    }
}
