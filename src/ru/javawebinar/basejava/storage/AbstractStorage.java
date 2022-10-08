package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void update(Resume r) {
        findExistingSearchKey(r.getUuid());
        doUpdate(r);
    }

    public void save(Resume r) {
        findNotExistingSearchKey(r.getUuid());
        doSave(r);
    }

    public void delete(String uuid) {
        findExistingSearchKey(uuid);
        doDelete(uuid);
    }

    public Resume get(String uuid) {
        Object searchKey = findExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    public Resume[] getAll() {
        return doGetAll();
    }

    private Object findExistingSearchKey(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private void findNotExistingSearchKey(String uuid) {
        if (isExist(findSearchKey(uuid))) {
            throw new ExistStorageException(uuid);
        }
    }

    protected abstract Object findSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract void doUpdate(Resume r);

    protected abstract void doSave(Resume r);

    protected abstract void doDelete(String uuid);

    protected abstract Resume doGet(Object searchKey);

    protected abstract Resume[] doGetAll();
}
