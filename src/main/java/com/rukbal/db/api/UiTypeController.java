package com.rukbal.db.api;

import com.rukbal.db.aapi.IBaseApi;
import com.rukbal.db.command.UiTypeVO;
import com.rukbal.db.domain.UiType;
import com.rukbal.db.repository.UiTypeRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.validation.Validated;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static com.rukbal.db.api.UiTypeController.BASE_PATH;
import static io.micronaut.core.util.CollectionUtils.mapOf;
import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller(BASE_PATH)
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@ExecuteOn(TaskExecutors.IO)
@Validated
//@Transactional // commented due to browser blocking
public class UiTypeController implements IBaseApi<UiTypeVO, Short, UiType> {
    public static final String BASE_PATH = "/api/uiTypes";
    public static final String BASE_TITLE = "UI Types";
    private static final Logger LOG = LoggerFactory.getLogger(UiTypeController.class);
    public static final String NAME_PATH = "/{name}";

    private final UiTypeRepository uiTypeRepository;

    UiTypeController(UiTypeRepository uiTypeRepository) {
        this.uiTypeRepository = uiTypeRepository;
    }

    @Get
    Flux<UiType> all() {
        return uiTypeRepository.findAll();
    }


    @Override
    public Mono<UiTypeVO> show(Short id) {
        return null;
    }

    @Override
    public Mono<UiTypeVO> update(UiTypeVO vo) {
        return null;
    }

    @Get(BASE_PATH_LIST)
    public Mono<Page<UiTypeVO>> listPage(@Nullable Pageable pageable) {
        return uiTypeRepository.findAll(pageable==null?Pageable.UNPAGED:pageable).map(page -> page.map(this::toVO));
    }

    @Get(NAME_PATH)
    public Mono<UiTypeVO> byName(@NotBlank String name) {
        return uiTypeRepository.findByName(name).map(this::toVO);
    }
    @Post
    public Mono<HttpResponse<UiTypeVO>> save(@Valid @NonNull @Body UiTypeVO uiTypeVO) {
        LOG.info("saving {}", uiTypeVO);
        return Mono.just(uiTypeVO)
                .map(this::toEntity)
                .flatMap(uiTypeRepository::saveElseThrowException)
                .onErrorMap(this::buildProblemStatement)
                .map(this::toVO)
                .map(this::createdHeadersWithId);
    }

    @Override
    public Mono<Short> delete(Short id) {
        return null;
    }

    @Override
    public Mono<Short> deleteAll() {
        return null;
    }

    public UiType toEntity(UiTypeVO uiTypeVO) {
        return new UiType(uiTypeVO.id(), uiTypeVO.name(), uiTypeVO.description(), uiTypeVO.pattern(), null);
    }

    public UiTypeVO toVO(UiType uiType) {
        return new UiTypeVO(uiType.id(), uiType.name(), uiType.description(), uiType.pattern());
    }

    @Error(exception = ConstraintViolationException.class)
    public Map onSaveFailed(HttpRequest<?> request, ConstraintViolationException ex) {
        var mapSet = ex.getConstraintViolations().stream()
                .map(cv -> mapOf(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toUnmodifiableSet());
        return mapOf(ERRORS_KEY, mapSet); //Map/*<String, Set<Map<String, String>>>*/
    }
}