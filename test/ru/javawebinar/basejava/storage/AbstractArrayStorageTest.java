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

    private static final Resume RESUME_1 = new Resume("uuid1");
    private static final Resume RESUME_2 = new Resume("uuid2");
    private static final Resume RESUME_3 = new Resume("uuid3");

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
        assertEquals(0, storage.size());
    }

    @Test
    void update() {
        Resume newResume = new Resume("uuid1");
        storage.update(newResume);
        assertEquals(newResume, storage.get("uuid1"));
    }

    @Test
    void save() {
        Resume resume_4 = new Resume("uuid4");
        storage.save(resume_4);
        assertEquals(4, storage.size());
        assertEquals(resume_4, storage.get("uuid4"));
    }

    @Test
    void delete() {
        storage.delete("uuid1");
        assertEquals(2, storage.size());
        assertThrows(NotExistStorageException.class, () ->
            storage.get("uuid1")
        );
    }

    @Test
    void get() {
        assertEquals(RESUME_1, storage.get("uuid1"));
        assertEquals(RESUME_2, storage.get("uuid2"));
        assertEquals(RESUME_3, storage.get("uuid3"));
    }

    @Test
    void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(RESUME_1, resumes[0]);
        assertEquals(RESUME_2, resumes[1]);
        assertEquals(RESUME_3, resumes[2]);
    }

    @Test
    void size() {
        assertEquals(3, storage.size());
    }

    @Test
    void getNotExistException() {
        assertThrows(NotExistStorageException.class, () ->
            storage.get("dummy")
        );
    }

    @Test
    void getExistException() {
        assertThrows(ExistStorageException.class, () ->
            storage.save(RESUME_1)
        );
    }

    @Test
    void getOverflowException() {
        storage.clear();
        for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
            if (storage.size() >= AbstractArrayStorage.STORAGE_LIMIT) {
                fail("Early overflow exception");
            }
            storage.save(new Resume());
        }
        assertThrows(StorageException.class, () ->
            storage.save(new Resume())
        );
    }
}