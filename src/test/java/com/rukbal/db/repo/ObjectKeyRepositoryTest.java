package com.rukbal.db.repo;

import com.rukbal.db.domain.ObjectKey;
import com.rukbal.db.domain.UiType;
import com.rukbal.db.repository.ObjectKeyRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @TestClassOrder(ClassOrderer.class)
class ObjectKeyRepositoryTest {

    private final ObjectKeyRepository objectKeyRepository;

    ObjectKeyRepositoryTest(ObjectKeyRepository objectKeyRepository) {
        this.objectKeyRepository = objectKeyRepository;
    }

    UiType uiTypeText = new UiType((short) 1, "text", "Textbox", "\\W", null, (short) 1);
    java.util.List<ObjectKey> objectKeys = Arrays.asList(
            new ObjectKey(1, "FIRST_NAME", "FIRST_NAME", "FIRST_NAME", uiTypeText, 1),
            new ObjectKey(2, "LAST_NAME", "LAST_NAME", "LAST_NAME", uiTypeText, 1),
            new ObjectKey(3, "MIDDLE_NAME", "MIDDLE_NAME", "MIDDLE_NAME", uiTypeText, 1),
            new ObjectKey(4, "PREFIX_NAME", "PREFIX_NAME", "PREFIX_NAME", uiTypeText, 1),
            new ObjectKey(5, "DATE_OF_BIRTH", "DATE_OF_BIRTH", "DATE_OF_BIRTH", uiTypeText, 1),
            new ObjectKey(6, "DATE_OF_JOIN", "DATE_OF_JOIN", "DATE_OF_JOIN", uiTypeText, 1));
    @BeforeEach
    void setUp() {
        objectKeyRepository.deleteAll().block();
        objectKeyRepository.saveAll(objectKeys).blockLast();
        Mono<Long> count = objectKeyRepository.count();
        System.out.println(count.block());
    }

    @AfterEach
    void tearDown() {
        objectKeyRepository.deleteAll();
    }

    @Test()
    @Order(value = 2)
    void countTest() {
        assertEquals(objectKeys.size() , objectKeyRepository.count().block());
    }

    @Test
    @Order(value = 1)
    void findByKeyName() {
        ObjectKey objectKey = objectKeyRepository.findByKeyName("FIRST_NAME").block();//Duration.of(1200, ChronoUnit.MILLIS));
        Objects.requireNonNull(objectKey);
        assertEquals("FIRST_NAME", objectKey.keyName());
    }

}