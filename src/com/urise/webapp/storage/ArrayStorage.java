package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    public void update(Resume r) {
        if (findIndex(r.getUuid()) >= 0) {
            Scanner scanner = new Scanner(System.in);
            r.setUuid(scanner.nextLine());
        } else {
            System.out.println("ERROR: resume is not found");
        }
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("ERROR: the storage is full");
        } else if (findIndex(r.getUuid()) >= 0) {
            System.out.println("ERROR: resume " + r.getUuid() + " has been saved already in the storage");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public Resume get(String uuid) {
        if (findIndex(uuid) >= 0) {
            return storage[findIndex(uuid)];
        } else {
            System.out.println("ERROR: resume is not found");
            return null;
        }
    }

    public void delete(String uuid) {
        if (findIndex(uuid) >= 0) {
            storage[findIndex(uuid)] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("ERROR: resume is not found");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
