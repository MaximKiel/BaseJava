package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private final List<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    @Override
    public Resume[] getAll() {
        return listStorage.toArray(new Resume[0]);
    }

    @Override
    protected Object findSearchKey(String uuid) {
        for (int i = 0; i < listStorage.size(); i++) {
            if (uuid.equals(listStorage.get(i).getUuid())) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        listStorage.set((int) searchKey, r);
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        listStorage.add(r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        listStorage.remove((int) searchKey);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return listStorage.get((int) searchKey);
    }
}
