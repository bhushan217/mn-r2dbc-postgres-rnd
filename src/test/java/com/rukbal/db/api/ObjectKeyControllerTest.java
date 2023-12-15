package com.rukbal.db.api;

import com.rukbal.db.aapi.IBaseApi;
import com.rukbal.db.command.ObjectKeyVO;
import com.rukbal.db.command.UiTypeVO;
import com.rukbal.db.domain.ObjectKey;
import com.rukbal.db.domain.UiType;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.problem.DefaultProblem;

import static com.rukbal.db.api.ObjectKeyController.BASE_TITLE;
import static com.rukbal.db.api.UiTypeController.BASE_PATH;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(rollback = false)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("API '" + BASE_PATH + "'")
class ObjectKeyControllerTest implements BaseControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectKeyControllerTest.class);
    private static UiTypeVO uiTypeTextVO = new UiTypeVO((short) 0, "text", "Textbox", "\\W");
    private static UiTypeVO uiTypeDateVO = new UiTypeVO((short) 0, "date", "Date", "\\d{2}/\\d{2}/\\d{4}");
    private static Integer savedId = -1;
    //    @Client(ObjectKeyController.BASE_PATH)
    private final IBaseApi<ObjectKeyVO, Integer, ObjectKey> objectKeyReactiveClient;
    //    @Client(UiTypeController.BASE_PATH)
    private final IBaseApi<UiTypeVO, Short, UiType> uiTypeReactiveClient;

    ObjectKeyControllerTest(
            @Client(ObjectKeyController.BASE_PATH)
            IBaseApi<ObjectKeyVO, Integer, ObjectKey> objectKeyReactiveClient,
            @Client(UiTypeController.BASE_PATH)
            IBaseApi<UiTypeVO, Short, UiType> uiTypeReactiveClient) {
        this.objectKeyReactiveClient = objectKeyReactiveClient;
        this.uiTypeReactiveClient = uiTypeReactiveClient;
    }

    @DisplayName("List should be empty")
    @Test
    @Order(1)
    void listEmpty() {
        objectKeyReactiveClient.deleteAll().block();
        var list = objectKeyReactiveClient.listPage(Pageable.UNPAGED);
        var page = list.block();
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertTrue(page.isEmpty());

        uiTypeTextVO = requireNonNull(uiTypeReactiveClient.save(uiTypeTextVO).block()).body();
        uiTypeDateVO = requireNonNull(uiTypeReactiveClient.save(uiTypeDateVO).block()).body();
        var listUI = uiTypeReactiveClient.listPage(Pageable.UNPAGED);
        var typeVOPage = listUI.block();
        assertNotNull(typeVOPage);
        LOG.debug("{} : {}", UiTypeController.BASE_TITLE, typeVOPage.getContent());
        assertEquals(2, typeVOPage.getTotalSize());

    }

    @Test
    @Order(2)
    void save() {
        var objectKeyVO = new ObjectKeyVO(savedId, "FIRST_NAME", "First Name", "This is first name", uiTypeTextVO.id());
        var save = objectKeyReactiveClient.save(objectKeyVO);
        var objectKey = requireNonNull(save.block()).body();
        assertNotNull(objectKey);
        assertNotNull(objectKey.id());
        savedId = objectKey.id();
        assertEquals("FIRST_NAME", objectKey.keyName());
        assertEquals((short) INT_ONE, objectKey.uiTypeId());
    }

    @Test
    @Order(3)
    void show() {
//        save();
        var objectKeyMono = objectKeyReactiveClient.show(savedId);
        var objectKey = objectKeyMono.block();
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
        var updatedRowsMono = objectKeyReactiveClient.update(new ObjectKeyVO(savedId, "LAST_NAME", "Last Name", "This is last name", uiTypeTextVO.id()));
        var updated = updatedRowsMono.block();
        assertNotNull(updated);
        assertEquals("LAST_NAME", updated.keyName());
    }

    @Test
    @Order(5)
    void list() {
//        save();
        var pageMono = objectKeyReactiveClient.listPage(PAGEABLE);
        var page = pageMono.block();
        assertNotNull(page);
        assertEquals(INT_ONE, page.getTotalPages());
    }

    @Test
    @Order(6)
    void delete() {
//        save();
        var longMono = objectKeyReactiveClient.delete(savedId);
        var deletedCount = longMono.block();
        assertNotNull(deletedCount);
        assertEquals(INT_ONE, deletedCount);
    }

    @Test
    @Order(8)
    @DisplayName("should throw an error on duplicate value DUP_NAME")
    void saveDuplicate() {
        var duplicateKeyName = "DUP_NAME";
        var duplicateLabel = "DUP_NAME";
        var duplicateDescription = "DUP_NAME";
        ObjectKeyVO objectKeyVO = new ObjectKeyVO(-1, duplicateKeyName, duplicateLabel, duplicateDescription, uiTypeTextVO.id());
        var save = objectKeyReactiveClient.save(objectKeyVO);
        var objectKey = requireNonNull(save.block()).body();
        assert objectKey != null;
        assertNotNull(objectKey.id());
        var save2 = objectKeyReactiveClient.save(objectKeyVO);
        var thrown = assertThrows(DefaultProblem.class, save2::block);

        var message = thrown.getMessage();
        assertEquals(BASE_TITLE + ": Record " + duplicateKeyName + " already exists", message);
//        assertEquals(EXPECTATION_FAILED, thrown.getMessage());
//        assertNotNull(message);
//        assertEquals("{\"type\":\""+ ObjectKeyController.BASE_PATH +"\",\"title\":\""+ BASE_TITLE +"\",\"status\":417,\"detail\":\"Record "
//                +duplicateKeyName+" already exists\"}", message);
    }

}