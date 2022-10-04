package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {

    private final ArrayList<Resume> listStorage = new ArrayList<>();

    public void clear() {
        listStorage.clear();
        listStorage.trimToSize();
    }

    public void update(Resume r) {
        int index = listStorage.indexOf(r);
        if (index >= 0) {
            listStorage.set(index, r);
        } else {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    public void save(Resume r) {
        int index = listStorage.indexOf(r);
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        } else {
            saveResume(index, r);
        }
    }

    public Resume[] getAll() {
        return listStorage.toArray(new Resume[0]);
    }

    public int size() {
        return listStorage.size();
    }

    @Override
    protected Resume getResume(int index) {
        return listStorage.get(index);
    }

    @Override
    protected int findIndex(String uuid) {
        for (Resume r : listStorage) {
            if (r.getUuid().equals(uuid)) {
                return listStorage.indexOf(r);
            }
        }
        return -1;
    }

    @Override
    protected void saveResume(int index, Resume r) {
        listStorage.add(r);
    }

    @Override
    protected void deleteResume(int index) {
        listStorage.remove(index);
    }
}
