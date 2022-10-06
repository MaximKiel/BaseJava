package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object findSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void saveResume(Resume r) {
        int savingIndex = ((int) findSearchKey(r.getUuid()) + 1) * -1;
        System.arraycopy(storage, savingIndex, storage, savingIndex + 1, size - savingIndex);
        storage[savingIndex] = r;
    }

    @Override
    protected void deleteResume(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - (index + 1));
    }
}
