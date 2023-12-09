//package com.rukbal.db.client;
//
//import com.rukbal.db.aapi.IBaseApi;
//import com.rukbal.db.api.ObjectKeyController;
//import com.rukbal.db.command.ObjectKeyVO;
//import com.rukbal.db.domain.ObjectKey;
//import io.micronaut.core.annotation.NonNull;
//import io.micronaut.data.model.Page;
//import io.micronaut.data.model.Pageable;
//import io.micronaut.http.HttpResponse;
//import io.micronaut.http.annotation.*;
//import io.micronaut.http.client.annotation.Client;
//import io.micronaut.validation.Validated;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
//import reactor.core.publisher.Mono;
//
//
//@Client(ObjectKeyController.BASE_PATH+'0')
//@Validated
//public interface ObjectKeyReactiveClient extends IBaseApi<ObjectKeyVO, Integer, ObjectKey> {//{//
//
////    @Get(BASE_PATH_ID)
//    Mono<ObjectKeyVO> show(@NotNull Integer id);
//    @Put
//    Mono<ObjectKeyVO> update(@Body @Valid ObjectKeyVO objectKeyVO);
//    @Get(BASE_PATH_LIST)
//    @NonNull Mono<Page<ObjectKeyVO>> listPage(@Valid Pageable pageable);
//
//    @Post
//    Mono<HttpResponse<ObjectKeyVO>> save(@Body @Valid ObjectKeyVO objectKeyVO);
//
//    @Delete(BASE_PATH_ID)
//    @NonNull Mono<Integer> delete(Integer id);
//
//    @Delete
//    Mono<Integer> deleteAll();
//}
