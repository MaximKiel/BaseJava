package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> UUID_COMPARATOR = Comparator.comparing(Resume::getUuid);

    @Override
    protected Object findSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "name");
        return Arrays.binarySearch(storage, 0, size, searchKey, UUID_COMPARATOR);
    }

    @Override
    protected void saveResume(Resume r, Object searchKey) {
        int savingIndex = ((int) searchKey + 1) * -1;
        System.arraycopy(storage, savingIndex, storage, savingIndex + 1, size - savingIndex);
        storage[savingIndex] = r;
    }

    @Override
    protected void deleteResume(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - (index + 1));
    }
}
