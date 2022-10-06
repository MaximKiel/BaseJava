package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void update(Resume r) {
        if (isExist(r)) {
            doUpdate(r);
        } else {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    public void save(Resume r) {
        if (isExist(r)) {
            throw new ExistStorageException(r.getUuid());
        } else {
            doSave(r);
        }
    }

    public void delete(String uuid) {
        if (isExist(findSearchKey(uuid))) {
            doDelete(uuid);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    public Resume get(String uuid) {
        if (isExist(findSearchKey(uuid))) {
            return doGet(uuid);
        }
        throw new NotExistStorageException(uuid);
    }

    public Resume[] getAll() {
        return doGetAll();
    }

    protected abstract Object findSearchKey(String uuid);

    protected abstract boolean isExist(Object object);

    protected abstract void doUpdate(Resume r);

    protected abstract void doSave(Resume r);

    protected abstract void doDelete(String uuid);

    protected abstract Resume doGet(String uuid);

    protected abstract Resume[] doGetAll();

    private Object findExistingSearchKey(String uuid) {
        if (!isExist(findSearchKey(uuid))) {
            throw new NotExistStorageException(uuid);
        }
        return findSearchKey(uuid);
    }

    private void findNotExistingSearchKey(String uuid) {
        if (isExist(findSearchKey(uuid))) {
            throw new ExistStorageException(uuid);
        }
    }
}
