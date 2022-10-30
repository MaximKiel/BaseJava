package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {

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
    protected Integer findSearchKey(String uuid) {
        for (int i = 0; i < listStorage.size(); i++) {
            if (uuid.equals(listStorage.get(i).getUuid())) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey != null;
    }

    @Override
    protected void doUpdate(Resume r, Integer searchKey) {
        listStorage.set(searchKey, r);
    }

    @Override
    protected void doSave(Resume r, Integer searchKey) {
        listStorage.add(r);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        listStorage.remove(searchKey.intValue());
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return listStorage.get(searchKey);
    }

    @Override
    protected List<Resume> doGetAll() {
        return listStorage;
    }
}
