package com.rukbal.db.api;

import com.rukbal.db.aapi.IBaseApi;
import com.rukbal.db.command.ObjectKeyVO;
import com.rukbal.db.domain.ObjectKey;
import com.rukbal.db.repository.ObjectKeyRepository;
import com.rukbal.db.repository.UiTypeRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import static io.micronaut.http.MediaType.APPLICATION_JSON;


@Controller(ObjectKeyController.BASE_PATH)
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@ExecuteOn(TaskExecutors.IO)
@Validated
@Refreshable
//@Transactional // commented due to browser blocking
public class ObjectKeyController implements IBaseApi<ObjectKeyVO, Integer, ObjectKey> {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectKeyController.class);
    public static final String BASE_PATH = "/api/objectKeys";
    public static final String BASE_TITLE = "Object Keys";
    protected final ObjectKeyRepository objectKeyRepository;
    protected final UiTypeRepository uiTypeRepository;

//    public String getBasePath(){
//        return BASE_PATH;
//    }
    public String getBaseTitle() {
        return BASE_TITLE;
    }
    public ObjectKeyController(ObjectKeyRepository objectKeyRepository, UiTypeRepository uiTypeRepository) {
        this.objectKeyRepository = objectKeyRepository;
        this.uiTypeRepository = uiTypeRepository;
    }

    @Get(BASE_PATH_ID)
    public Mono<ObjectKeyVO> show(@NonNull Integer id) {
        return objectKeyRepository.findById(id).map(this::toVO);
    }

    @Patch(BASE_PATH_ID)
    public Mono<HttpResponse<ObjectKeyVO>> update(@NotNull Integer id, @Body @NonNull @Valid ObjectKeyVO objectKeyVO) {
        LOG.info("updating {}: {}", id, objectKeyVO);
        return Mono.just(objectKeyVO)
                .map(this::toEntity)
                .flatMap(objectKeyRepository::update)
                .onErrorMap(this::buildProblemStatement)
                .map(this::toVO)
                .map(this::updatedHeadersWithId);
    }


    /**
     * @param pageable The page object
     * @return List of @ObjectKeyVO
     */
    @ApiResponse(
            content = @Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(type="ObjectKeyVO"))
    )
    @ApiResponse(responseCode = "400", description = "Invalid ObjectKey Supplied")
    @ApiResponse(responseCode = "404", description = "ObjectKey not found")
    @Tag(name = "list ObjectKey")
    @Get(BASE_PATH_LIST)
    public Mono<Page<ObjectKeyVO>> listPage(@Nullable Pageable pageable) {
        return objectKeyRepository.listAll(pageable).map(page -> page.map(this::toVO));
//        return Mono.just(pageable)
//                .flatMap(objectKeyRepository::findAll)
//                .map(page -> page.map(this::toVO));
    }


    /**
     * @param objectKeyVO The objectKeyVO
     * @return The objectKeyVO
     */
    @ApiResponse(
            content = @Content(mediaType = APPLICATION_JSON,
            schema = @Schema(type="ObjectKeyVO"))
    )
    @ApiResponse(responseCode = "400", description = "Invalid ObjectKey Supplied")
    @ApiResponse(responseCode = "404", description = "ObjectKey not found")
    @Tag(name = "save ObjectKey")
    @Post
    public Mono<HttpResponse<ObjectKeyVO>> save(@Body @Valid ObjectKeyVO objectKeyVO) {
        LOG.info("saving {}", objectKeyVO);
        return Mono.just(objectKeyVO)
                .map(this::toEntity)
                .flatMap(objectKeyRepository::saveElseThrowException)
                .map(this::toVO)
                .onErrorMap(this::buildProblemStatement)
                .map(this::createdHeadersWithId);
    }

    @Delete(BASE_PATH_ID)
    public @NonNull Mono<Integer> delete(Integer id) {
        LOG.info("deleting {}", id);
        return Mono.just(id)
                .flatMap(objectKeyRepository::deleteById)
                .map(Math::toIntExact)
                .onErrorMap(this::buildProblemStatement);
    }



    @Delete
    public Mono<Integer> deleteAll() {
        LOG.info("deleting ALL");
        return objectKeyRepository.deleteAll()
                .map(Math::toIntExact)
                .onErrorMap(this::buildProblemStatement);
    }


    ///////////////////
    //      UTILS
    ///////////////////

    public ObjectKey toEntity(ObjectKeyVO vo) throws DataAccessException {
        var uiType = uiTypeRepository.findById(vo.uiTypeId()).block();
        if(uiType==null){
            throw new DataAccessException("Invalid UI Type - [id: " + vo.uiTypeId() + "]");
        }
        return new ObjectKey(vo.id(), vo.keyName(), vo.label(), vo.description(), uiType, vo.version());
    }

    public ObjectKeyVO toVO(ObjectKey ok) {
        return new ObjectKeyVO(ok.id(), ok.keyName(), ok.label(), ok.description(), ok.uiType().id(), ok.version());
    }

}
