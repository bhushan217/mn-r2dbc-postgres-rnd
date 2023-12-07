package com.rukbal.db.api;

import com.rukbal.db.command.UiTypeVO;
import com.rukbal.db.domain.UiType;
import com.rukbal.db.repository.UiTypeRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.transaction.annotation.Transactional;
import io.micronaut.validation.Validated;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.micronaut.core.util.CollectionUtils.mapOf;
import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller("/uiTypes")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@ExecuteOn(TaskExecutors.IO)
@Validated
@Transactional
class UiTypeController {
    private static final Logger LOG = LoggerFactory.getLogger(UiTypeController.class);

    private final UiTypeRepository uiTypeRepository;

    UiTypeController(UiTypeRepository uiTypeRepository) {
        this.uiTypeRepository = uiTypeRepository;
    }

    @Get
    Flux<UiType> all() {
        return uiTypeRepository.findAll();
    }


    @Get("/list")
    Mono<Page<UiTypeVO>> listPage(@Nullable @Body Pageable pageable){
        return uiTypeRepository.findAll(pageable==null?Pageable.UNPAGED:pageable).map(page -> page.map(this::toVO));
    }

    @Get("/{name}")
    Mono<UiTypeVO> byName(@NotBlank String name) {
        return uiTypeRepository.findByName(name).map(this::toVO);
    }
    @Post
    Mono<UiTypeVO> save(@Valid @NonNull @Body UiTypeVO uiTypeVO) {
        LOG.info("saving {}", uiTypeVO);
        return uiTypeRepository.save(toEntity(uiTypeVO)).map(this::toVO);
    }

    private UiType toEntity(UiTypeVO uiTypeVO) {
        return new UiType(uiTypeVO.id(), uiTypeVO.name(), null);
    }

    private UiTypeVO toVO(UiType uiType) {
        return new UiTypeVO(uiType.id(), uiType.name());
    }

    @Error(exception = ConstraintViolationException.class)
    public Map onSaveFailed(HttpRequest<?> request, ConstraintViolationException ex) {
        Set<Map> mapSet = ex.getConstraintViolations().stream().map(cv -> mapOf(cv.getPropertyPath().toString(), cv.getMessage())).collect(Collectors.toUnmodifiableSet());
        return mapOf("errors", mapSet);
    }
}