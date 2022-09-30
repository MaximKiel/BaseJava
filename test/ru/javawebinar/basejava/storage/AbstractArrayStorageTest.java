package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractArrayStorageTest {

    private final Storage storage;

    public AbstractArrayStorageTest (Storage storage) {
        this.storage = storage;
    }

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
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
        assertArrayEquals(storage.getAll(), new Resume[0]);
    }

    @Test
    void update() {
        Resume newResume = new Resume(UUID_1);
        storage.update(newResume);
        assertSame(newResume, storage.get(UUID_1));
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
    void getAll() {
        Resume[] expected = {RESUME_1, RESUME_2, RESUME_3};
        assertArrayEquals(expected, storage.getAll());
        assertSize(3);
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

    @Test
    void getOverflowException() {
        storage.clear();

        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            fail("Early overflow exception");
        }

        assertThrows(StorageException.class, () ->
            storage.save(new Resume())
        );
    }

    private void assertSize(int index) {
        assertEquals(index, storage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }
}