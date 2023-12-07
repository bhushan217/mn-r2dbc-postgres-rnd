package com.rukbal.db.api;

import com.rukbal.db.command.ObjectKeyVO;
import com.rukbal.db.domain.ObjectKey;
import com.rukbal.db.domain.UiType;
import com.rukbal.db.repository.ObjectKeyRepository;
import com.rukbal.db.repository.UiTypeRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.transaction.annotation.Transactional;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import reactor.core.publisher.Mono;

import java.net.URI;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller("/objectKeys")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@ExecuteOn(TaskExecutors.IO)
@Validated
@Transactional
public class ObjectKeyController {

    protected final ObjectKeyRepository objectKeyRepository;
    protected final UiTypeRepository uiTypeRepository;

    public ObjectKeyController(ObjectKeyRepository objectKeyRepository, UiTypeRepository uiTypeRepository) {
        this.objectKeyRepository = objectKeyRepository;
        this.uiTypeRepository = uiTypeRepository;
    }

    @Get("/{id}")
    public Mono<ObjectKeyVO> show(@NonNull Integer id) {
        return objectKeyRepository.findById(id).map(this::toVO);
    }

    @Put
    public Mono<ObjectKeyVO> update(@Body @Valid ObjectKeyVO objectKeyVO) {
        return Mono.just(objectKeyVO)
                .map(this::toEntity)
                .flatMap(objectKeyRepository::update)
                .map(this::toVO)
                .onErrorMap(ObjectKeyController::buildProblemStatement);
    }

    @Get("/list")
    public @NonNull Mono<Page<ObjectKeyVO>> listPage(@Nullable Pageable pageable) {
        return objectKeyRepository.findAll(pageable==null?Pageable.UNPAGED:pageable)
                .map(page -> page.map(this::toVO));
    }

    @Post
    public Mono<ObjectKeyVO> save(@Body @Valid ObjectKeyVO objectKeyVO) {
        return Mono.just(objectKeyVO)
                .map(this::toEntity)
                .flatMap(objectKeyRepository::saveElseThrowException)
                .map(this::toVO)
                .onErrorMap(ObjectKeyController::buildProblemStatement);
    }

//    @Post("/ex")
//    public Mono<ObjectKeyVO> saveExceptions(@Body @Valid ObjectKeyVO objectKeyVO) throws HttpStatusException {
//        return Mono.just(objectKeyVO)
//                .map(this::toEntity)
//                .flatMap(objectKeyRepository::saveElseThrowException)
//                .map(this::toVO)
//                .onErrorMap(ObjectKeyController::buildProblemStatement);//.onErrorMap( e -> e);
//    }

    @Delete("/{id}")
    public @NonNull Mono<Long> delete(Integer id) {
        return objectKeyRepository.deleteById(id);
    }



    @Delete
    Mono<Long> deleteAll(){
        return objectKeyRepository.deleteAll();
    }


    ///////////////////
    //      UTILS
    ///////////////////
    protected URI location(Integer id) {
        return URI.create("/objectKeys/" + id);
    }

    protected URI location(ObjectKey objectKey) {
        return location(objectKey.id());
    }


    private static ThrowableProblem buildProblemStatement(Throwable e) {
        return Problem.builder()
                .withType(URI.create("/objectKeys"))
                .withTitle("OBJECT_KEYS")
                .withStatus(new HttpStatusType(HttpStatus.EXPECTATION_FAILED))
                .withDetail(e.getMessage())
//                .with("product", "B00027Y5QG")
                .build();
    }

    private ObjectKey toEntity(ObjectKeyVO cmd) throws DataAccessException {
        UiType uiType = uiTypeRepository.findById(cmd.uiTypeId()).block();
        if(uiType==null){
            throw new DataAccessException("Invalid UI Type - [id: "+cmd.uiTypeId()+"]");
        }
        return new ObjectKey(cmd.id(), cmd.keyName(), uiType);
    }

    private ObjectKeyVO toVO(ObjectKey ok) {
        return new ObjectKeyVO(ok.id(), ok.keyName(), ok.uiType().id());
    }

}
