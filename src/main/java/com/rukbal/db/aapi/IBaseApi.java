package com.rukbal.db.aapi;

import com.rukbal.db.domain.BaseRecord;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.problem.HttpStatusType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.rukbal.db.aapi.IBaseApi.BASE_PATH;

@Controller(BASE_PATH)
public interface IBaseApi<V extends BaseRecord<I>, I, E extends BaseRecord<I>> {
    String BASE_PATH = "/basePath";
    String BASE_PATH_ID = "/{id}";
    String BASE_PATH_LIST = "/list";
    String BASE_TITLE = "Base API";
    String ERRORS_KEY = "errors";

    default String getBasePath() {
        return BASE_PATH;
    }

    default String getBaseTitle() {
        return BASE_TITLE;
    }

    @Get(BASE_PATH_ID)
    Mono<V> show(@NotNull I id);

    @Patch(BASE_PATH_ID)
    Mono<HttpResponse<V>> update(@NotNull I id, @Body @NonNull @Valid V vo);

    @Get(BASE_PATH_LIST)
    @NonNull Mono<Page<V>> listPage(@Nullable Pageable pageable);

    //    @Post
    Mono<HttpResponse<V>> save(@Body @Valid V vo);


    @Delete(BASE_PATH_ID)
    @NonNull Mono<I> delete(I id);

    @Delete
    Mono<I> deleteAll();

    default ThrowableProblem buildProblemStatement(Throwable e) {
        return Problem.builder()
                .withType(URI.create(getBasePath()))
                .withTitle(getBaseTitle())
                .withStatus(new HttpStatusType(HttpStatus.EXPECTATION_FAILED))
                .withDetail(e.getMessage())
//                .with("product", "B00027Y5QG")
                .build();
    }

    E toEntity(V vo) throws DataAccessException;

    V toVO(E e);

    default URI location(I id) {
        return UriBuilder.of(BASE_PATH).path(id.toString()).build();
    }

    default HttpResponse<V> createdHeadersWithId(V vo) {
        return HttpResponse.created(vo).headers(headers -> headers.location(location(vo.id())));
    }

    default HttpResponse<V> updatedHeadersWithId(V vo) {
        return HttpResponse.ok(vo).headers(headers -> headers.location(location(vo.id())));
    }
}
