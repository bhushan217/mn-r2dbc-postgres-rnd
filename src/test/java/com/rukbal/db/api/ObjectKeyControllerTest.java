package com.rukbal.db.api;

import com.rukbal.db.client.ObjectKeyReactiveClient;
import com.rukbal.db.client.UiTypeReactiveClient;
import com.rukbal.db.command.ObjectKeyVO;
import com.rukbal.db.command.UiTypeVO;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import static io.micronaut.http.HttpStatus.EXPECTATION_FAILED;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("API '/ObjectKeys'")
class ObjectKeyControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectKeyControllerTest.class);
    private final ObjectKeyReactiveClient objectKeyReactiveClient;
    private final UiTypeReactiveClient uiTypeReactiveClient;

    ObjectKeyControllerTest(ObjectKeyReactiveClient objectKeyReactiveClient, UiTypeReactiveClient uiTypeReactiveClient) {
        this.objectKeyReactiveClient = objectKeyReactiveClient;
        this.uiTypeReactiveClient = uiTypeReactiveClient;
    }

    private static Integer savedId = -1;
    private static UiTypeVO uiTypeTextVO = new UiTypeVO(-1, "text");
    private static UiTypeVO uiTypeDateVO = new UiTypeVO(-2, "date");


    @DisplayName("List should be empty")
    @Test
    @Order(1)
    void listEmpty() {
        objectKeyReactiveClient.deleteAll().block();
        Mono<Page<ObjectKeyVO>> list = objectKeyReactiveClient.list(Pageable.UNPAGED);
        Page<ObjectKeyVO> page = list.block();
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertTrue(page.isEmpty());

        uiTypeTextVO = uiTypeReactiveClient.save(uiTypeTextVO).block();
        uiTypeDateVO = uiTypeReactiveClient.save(uiTypeDateVO).block();
        Mono<Page<UiTypeVO>> listUI = uiTypeReactiveClient.list(Pageable.UNPAGED);
        Page<UiTypeVO> typeVOPage = listUI.block();
        assert typeVOPage != null;
        LOG.debug("{}", typeVOPage.getContent().get(0));

    }

    @Test
    @Order(2)
    void save() {

        Mono<ObjectKeyVO> save = objectKeyReactiveClient.save(new ObjectKeyVO(savedId, "FIRST_NAME", uiTypeTextVO.id()));
        ObjectKeyVO objectKey = save.block();
        assertNotNull(objectKey);
        assertNotNull(objectKey.id());
        savedId = objectKey.id();
        assertEquals("FIRST_NAME", objectKey.keyName());
        assertEquals(1, objectKey.uiTypeId());
    }

    @Test
    @Order(3)
    void show() {
//        save();
        Mono<ObjectKeyVO> objectKeyMono = objectKeyReactiveClient.show(savedId);
        ObjectKeyVO objectKey = objectKeyMono.block();
        assertNotNull(objectKey);
        assertNotNull(objectKey.id());
        assertEquals("FIRST_NAME", objectKey.keyName());
        assertEquals( uiTypeTextVO.id(), objectKey.uiTypeId());
        assertEquals(savedId, objectKey.id());
    }

    @Test
    @Order(4)
    void update() {
//        save();
        Mono<ObjectKeyVO> updatedRowsMono = objectKeyReactiveClient.update(new ObjectKeyVO(savedId, "LAST_NAME",  uiTypeTextVO.id()));
        ObjectKeyVO updated = updatedRowsMono.block();
        assertNotNull(updated);
        assertEquals("LAST_NAME", updated.keyName());
    }

    @Test
    @Order(5)
    void list() {
//        save();
        Mono<Page<ObjectKeyVO>> pageMono = objectKeyReactiveClient.list(Pageable.from(1, 10, Sort.of(Sort.Order.asc("id"))));
        Page<ObjectKeyVO> page = pageMono.block();
        assertNotNull(page);
        assertEquals(1L, page.getTotalPages());
    }

    @Test
    @Order(6)
    void delete() {
//        save();
        Mono<Long> longMono = objectKeyReactiveClient.delete(savedId);
        Long deletedCount = longMono.block();
        assertNotNull(deletedCount);
        assertEquals(1L, deletedCount);
    }

//    @Test
//    @Order(7)
//    void saveExceptions() {
//        ObjectKeyVO objectKey = new ObjectKeyVO(-1, "bbjtydgxwbepqphkgcwdzanaesynkrmvtqxevfnqcznpsekfnsvavsmvadwbqpxz", 0);
//        Set<ConstraintViolation<ObjectKeyVO>> constraintViolationSet = validator.validate(objectKey);
//        assertFalse(constraintViolationSet.isEmpty());
//        assertEquals(2, constraintViolationSet.size());
//        constraintViolationSet.iterator().forEachRemaining( objectKeyCV -> {
//            String message = objectKeyCV.getMessage();
//            Object invalidValue = objectKeyCV.getInvalidValue();
//            Path propertyPath = objectKeyCV.getPropertyPath();
//            LOG.info("propertyPath: {},\ninvalidValue: {},\nmessage: {}", propertyPath, invalidValue, message);
////            assertEquals(propertyPath);
//        });
//        var thrown = assertThrows(HttpClientResponseException.class,
//                () -> objectKeyReactiveClient.save(objectKey)
//                        .block());
//        String message = thrown.getMessage();
//        assertEquals(EXPECTATION_FAILED, thrown.getResponse().status());
//        assertNotNull(message);
//        assertEquals("test exception", message);
//    }

    @Test
    @Order(8)
    @DisplayName("should throw an error on duplicate value DUP_NAME")
    void saveDuplicate() {
        String duplicateKeyName = "DUP_NAME";
        Mono<ObjectKeyVO> save = objectKeyReactiveClient.save(new ObjectKeyVO(-1, duplicateKeyName,  uiTypeTextVO.id()));
        ObjectKeyVO objectKey = save.block();
        assert objectKey != null;
        assertNotNull(objectKey.id());
        Mono<ObjectKeyVO> save2 = objectKeyReactiveClient.save(new ObjectKeyVO(-1, duplicateKeyName,  uiTypeTextVO.id()));
        var thrown = assertThrows(HttpClientResponseException.class, save2::block);

        String message = thrown.getMessage();
        assertEquals(EXPECTATION_FAILED, thrown.getResponse().status());
        assertNotNull(message);
        assertEquals("{\"type\":\"/objectKeys\",\"title\":\"OBJECT_KEYS\",\"status\":417,\"detail\":\"Record "
                +duplicateKeyName+" already exists\"}", message);
    }

}