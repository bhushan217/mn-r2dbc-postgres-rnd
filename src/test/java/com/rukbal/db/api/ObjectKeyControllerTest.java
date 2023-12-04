package com.rukbal.db.api;

import com.rukbal.db.client.ObjectKeyReactiveClient;
import com.rukbal.db.command.ObjectKeyUpdateCommand;
import com.rukbal.db.dto.ObjectKey;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("API '/ObjectKeys'")
class ObjectKeyControllerTest {

    private final ObjectKeyReactiveClient objectKeyReactiveClient;

    ObjectKeyControllerTest(ObjectKeyReactiveClient objectKeyReactiveClient) {
        this.objectKeyReactiveClient = objectKeyReactiveClient;
    }

    private static Long savedId = -1L;


    @DisplayName("List should be empty")
    @Test
    @Order(1)
    void listEmpty() {
        objectKeyReactiveClient.deleteAll().block();
        Mono<Page<ObjectKey>> list = objectKeyReactiveClient.list(Pageable.UNPAGED);
        Page<ObjectKey> page = list.block();
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertTrue(page.isEmpty());
    }
    @Test
    @Order(2)
    void save() {
        Mono<ObjectKey> save = objectKeyReactiveClient.save(new ObjectKey(savedId, "FIRST_NAME", "text"));
        ObjectKey objectKey = save.block();
        assertNotNull(objectKey);
        assertNotNull(objectKey.id());
        savedId = objectKey.id();
        assertEquals("FIRST_NAME", objectKey.keyName());
        assertEquals("text", objectKey.uiType());
    }

    @Test
    @Order(3)
    void show() {
//        save();
        Mono<ObjectKey> objectKeyMono = objectKeyReactiveClient.show(savedId);
        ObjectKey objectKey = objectKeyMono.block();
        assertNotNull(objectKey);
        assertNotNull(objectKey.id());
        assertEquals("FIRST_NAME", objectKey.keyName());
        assertEquals("text", objectKey.uiType());
        assertEquals(savedId, objectKey.id());
    }

    @Test
    @Order(4)
    void update() {
//        save();
        Mono<Long> updatedRowsMono = objectKeyReactiveClient.update(new ObjectKeyUpdateCommand(savedId, "LAST_NAME", "text"));
        Long updated = updatedRowsMono.block();
        assertNotNull(updated);
        assertEquals(1L, updated);
    }

    @Test
    @Order(5)
    void list() {
//        save();
        Mono<Page<ObjectKey>> pageMono = objectKeyReactiveClient.list(Pageable.from(1, 10, Sort.of(Sort.Order.asc("id"))));
        Page<ObjectKey> page = pageMono.block();
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

    @Test
    @Order(7)
    void saveExceptions() {
        HttpClientResponseException httpClientResponseException = assertThrows(HttpClientResponseException.class,
                () -> objectKeyReactiveClient.saveExceptions(new ObjectKey(-1L, "null", "null"))
                        .block());
        String message = httpClientResponseException.getMessage();
        assertNotNull(message);
        assertEquals("test exception", message);
    }

    }