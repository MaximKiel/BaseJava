package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected int setSavingIndex(int index) {
        int savingIndex = (index + 1) * -1;
        System.arraycopy(storage, savingIndex, storage, savingIndex + 1, size - savingIndex);
        return savingIndex;
    }

    @Override
    protected void realizeDelete(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - (index + 1));
        storage[size - 1] = null;
    }
}
