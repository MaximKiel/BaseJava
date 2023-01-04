package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.ResumeTestData;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();
    protected static final String DB_URL = Config.getInstance().getDbUrl();
    protected static final String DB_USER = Config.getInstance().getDbUser();
    protected static final String DB_PASSWORD = Config.getInstance().getDbPassword();
    protected final Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    private static final String UUID_1 = String.valueOf(UUID.randomUUID());
    private static final String UUID_2 = String.valueOf(UUID.randomUUID());
    private static final String UUID_3 = String.valueOf(UUID.randomUUID());
    private static final String UUID_4 = String.valueOf(UUID.randomUUID());
    private static final String DUMMY = "dummy";

    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String NAME_4 = "name4";

    private static final ResumeTestData RESUME_TEST_DATA = new ResumeTestData();
    private static final Resume RESUME_1 = RESUME_TEST_DATA.createResume(UUID_1, NAME_1);
    private static final Resume RESUME_2 = RESUME_TEST_DATA.createResume(UUID_2, NAME_2);
    private static final Resume RESUME_3 = RESUME_TEST_DATA.createResume(UUID_3, NAME_3);
    private static final Resume RESUME_4 = RESUME_TEST_DATA.createResume(UUID_4, NAME_4);

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    void update() {
        Resume newResume = new Resume(UUID_1, NAME_1);
        storage.update(newResume);
        assertTrue(newResume.equals(storage.get(UUID_1)));
    }

    @Test
    void save() {
        storage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(4);
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () ->
            storage.get(UUID_1)
        );
    }

    @Test
    void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test
    void getAllSorted() {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        assertSize(3);
        assertEquals(storage.getAllSorted(), Arrays.asList(expected));
    }

    @Test
    void size() {
        assertSize(3);
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () ->
            storage.get(DUMMY)
        );
    }

    @Test
    void saveExist() {
        assertThrows(ExistStorageException.class, () ->
            storage.save(RESUME_1)
        );
    }

    @Test
    void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                storage.delete(DUMMY)
        );
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                storage.update(RESUME_4)
        );
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }
}