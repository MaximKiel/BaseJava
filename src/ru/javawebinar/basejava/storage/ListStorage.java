package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {

    private final ArrayList<Resume> listStorage = new ArrayList<>();

    public void clear() {
        listStorage.clear();
        listStorage.trimToSize();
    }

    public int size() {
        return listStorage.size();
    }

    @Override
    protected Object findSearchKey(String uuid) {
        for (Resume r : listStorage) {
            if (r.getUuid().equals(uuid)) {
                return r;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object object) {
        return findSearchKey((String) object) != null;
    }

    @Override
    protected void doUpdate(Resume r) {
        listStorage.set(findIndex(r.getUuid()), (Resume) findSearchKey(r.getUuid()));
    }

    @Override
    protected void doSave(Resume r) {
        listStorage.add(r);
    }

    @Override
    protected void doDelete(String uuid) {
        listStorage.remove((Resume) findSearchKey(uuid));
    }

    @Override
    protected Resume doGet(String uuid) {
        return listStorage.get(findIndex(uuid));
    }

    @Override
    protected Resume[] doGetAll() {
        return listStorage.toArray(new Resume[0]);
    }

    private int findIndex(String uuid) {
        int index = -1;
        for (Resume r : listStorage) {
            if (r.getUuid().equals(uuid)) {
                index = listStorage.indexOf(r);
                break;
            }
        }
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }
}
