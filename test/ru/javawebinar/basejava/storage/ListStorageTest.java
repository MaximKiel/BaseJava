package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.*;

class ListStorageTest {

    private final ListStorage testListStorage = new ListStorage();

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String DUMMY = "dummy";

    private static final Resume RESUME_1 = new Resume(UUID_1);
    private static final Resume RESUME_2 = new Resume(UUID_2);
    private static final Resume RESUME_3 = new Resume(UUID_3);
    private static final Resume RESUME_4 = new Resume(UUID_4);

    @BeforeEach
    public void setUp() {
        testListStorage.clear();
        testListStorage.save(RESUME_1);
        testListStorage.save(RESUME_2);
        testListStorage.save(RESUME_3);
    }

    @Test
    void clear() {
        testListStorage.clear();
        assertSize(0);
        assertArrayEquals(testListStorage.getAll(), new Resume[0]);
    }

    @Test
    void update() {
        Resume newResume = new Resume(UUID_1);
        testListStorage.update(newResume);
        assertSame(newResume, testListStorage.get(UUID_1));
    }

    @Test
    void save() {
        testListStorage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(4);
    }

    @Test
    void delete() {
        testListStorage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () ->
                testListStorage.get(UUID_1)
        );
    }

    @Test
    void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test
    void getAll() {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        assertArrayEquals(expected, testListStorage.getAll());
        assertSize(3);
    }

    @Test
    void size() {
        assertSize(3);
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                testListStorage.get(DUMMY)
        );
    }

    @Test
    void saveExist() {
        assertThrows(ExistStorageException.class, () ->
                testListStorage.save(RESUME_1)
        );
    }

    @Test
    void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                testListStorage.delete(DUMMY)
        );
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                testListStorage.update(RESUME_4)
        );
    }

    private void assertSize(int size) {
        assertEquals(size, testListStorage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, testListStorage.get(resume.getUuid()));
    }
}