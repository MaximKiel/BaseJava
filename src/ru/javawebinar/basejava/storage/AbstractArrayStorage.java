package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = findIndex(r.getUuid());
        if (index >= 0) {
            storage[index] = r;
        } else {
            System.out.println("ERROR: resume " + r.getUuid() + " is not found");
        }
    }

    public void save(Resume r) {
        int index = findIndex(r.getUuid());
        if (size >= STORAGE_LIMIT) {
            System.out.println("ERROR: the storage is full");
        } else if (index >= 0) {
            System.out.println("ERROR: resume " + r.getUuid() + " has been saved already in the storage");
        } else {
            storage[saveResume(index)] = r;
            size++;
        }
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            deleteResume(index);
            size--;
        } else {
            System.out.println("ERROR: resume " + uuid + " is not found");
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("ERROR: resume " + uuid + " is not found");
        return null;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    protected abstract int findIndex(String uuid);

    protected abstract int saveResume(int index);

    protected abstract void deleteResume(int index);
}
