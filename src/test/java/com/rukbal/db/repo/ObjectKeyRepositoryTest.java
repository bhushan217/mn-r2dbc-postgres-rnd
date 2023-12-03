package com.rukbal.db.repo;

import com.rukbal.db.dto.ObjectKey;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.class)
class ObjectKeyRepositoryTest {

    private final ObjectKeyRepository objectKeyRepository;

    ObjectKeyRepositoryTest(ObjectKeyRepository objectKeyRepository) {
        this.objectKeyRepository = objectKeyRepository;
    }

    java.util.List<ObjectKey> objectKeys = Arrays.asList(
            new ObjectKey(1L, "FIRST_NAME", "text"),
            new ObjectKey(2L, "LAST_NAME", "text"),
            new ObjectKey(3L, "MIDDLE_NAME", "text"),
            new ObjectKey(4L, "PREFIX_NAME", "text"),
            new ObjectKey(5L, "DATE_OF_BIRTH", "date"),
            new ObjectKey(6L, "DATE_OF_JOIN", "date"));
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
        assertEquals("FIRST_NAME", Objects.requireNonNull(objectKey).getKeyName());
    }

}