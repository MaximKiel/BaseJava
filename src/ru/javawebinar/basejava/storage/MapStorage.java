package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;

public class MapStorage extends AbstractStorage {

    private final HashMap<String, Resume> hashMapStorage = new HashMap<>();

    @Override
    public void clear() {
        hashMapStorage.clear();
    }

    @Override
    public int size() {
        return hashMapStorage.size();
    }

    @Override
    protected Object findSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return hashMapStorage.get((String) searchKey) != null;
    }

    @Override
    protected void doUpdate(Resume r) {
        hashMapStorage.replace(r.getUuid(), r);
    }

    @Override
    protected void doSave(Resume r) {
        hashMapStorage.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(String uuid) {
        hashMapStorage.remove(uuid);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return hashMapStorage.get((String) searchKey);
    }

    @Override
    protected Resume[] doGetAll() {
        return hashMapStorage.values().toArray(new Resume[0]);
    }
}
