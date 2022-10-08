package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MapStorageTest {

    private final MapStorage testMapStorage = new MapStorage();

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
        testMapStorage.clear();
        testMapStorage.save(RESUME_1);
        testMapStorage.save(RESUME_2);
        testMapStorage.save(RESUME_3);
    }
    @Test
    void clear() {
        testMapStorage.clear();
        assertSize(0);
        assertArrayEquals(testMapStorage.getAll(), new Resume[0]);
    }

    @Test
    void update() {
        Resume newResume = new Resume(UUID_1);
        testMapStorage.update(newResume);
        assertSame(newResume, testMapStorage.get(UUID_1));
    }

    @Test
    void save() {
        testMapStorage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(4);
    }

    @Test
    void delete() {
        testMapStorage.delete(UUID_1);
        assertSize(2);
        assertThrows(NotExistStorageException.class, () ->
                testMapStorage.get(UUID_1)
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
        HashMap<String, Resume> hashMap = new HashMap<>();
        hashMap.put(UUID_1, RESUME_1);
        hashMap.put(UUID_2, RESUME_2);
        hashMap.put(UUID_3, RESUME_3);
        Resume[] expected = hashMap.values().toArray(new Resume[0]);
        assertArrayEquals(expected, testMapStorage.getAll());
        assertSize(3);
    }

    @Test
    void size() {
        assertSize(3);
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                testMapStorage.get(DUMMY)
        );
    }

    @Test
    void saveExist() {
        assertThrows(ExistStorageException.class, () ->
                testMapStorage.save(RESUME_1)
        );
    }

    @Test
    void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                testMapStorage.delete(DUMMY)
        );
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () ->
                testMapStorage.update(RESUME_4)
        );
    }

    private void assertSize(int size) {
        assertEquals(size, testMapStorage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, testMapStorage.get(resume.getUuid()));
    }

}