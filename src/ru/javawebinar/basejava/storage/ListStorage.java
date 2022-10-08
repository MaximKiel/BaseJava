package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {

    private final ArrayList<Resume> listStorage = new ArrayList<>();

    @Override
    public void clear() {
        listStorage.clear();
        listStorage.trimToSize();
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    @Override
    protected Object findSearchKey(String uuid) {
        int index = -1;
        for (Resume r : listStorage) {
            if (r.getUuid().equals(uuid)) {
                index = listStorage.indexOf(r);
                break;
            }
        }
        return index;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    @Override
    protected void doUpdate(Resume r) {
        listStorage.set((int) findSearchKey(r.getUuid()), r);
    }

    @Override
    protected void doSave(Resume r) {
        listStorage.add(r);
    }

    @Override
    protected void doDelete(String uuid) {
        listStorage.remove((int) findSearchKey(uuid));
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return listStorage.get((int) searchKey);
    }

    @Override
    protected Resume[] doGetAll() {
        return listStorage.toArray(new Resume[0]);
    }
}
