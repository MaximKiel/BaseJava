package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        int index = findIndex(r.getUuid());
        if (size >= STORAGE_LIMIT) {
            System.out.println("ERROR: the storage is full");
        } else if (index >= 0) {
            System.out.println("ERROR: resume " + r.getUuid() + " has been saved already in the storage");
        } else {
            int newIndex = (index + 1) * -1;
            System.arraycopy(storage, newIndex, storage, newIndex + 1, size - newIndex);
            storage[newIndex] = r;
            size++;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            System.arraycopy(storage, index + 1, storage, index, size - (index + 1));
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("ERROR: resume " + uuid + " is not found");
        }
    }

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
