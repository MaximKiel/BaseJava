package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public interface Storage {

    void clear();

    int size();

    void update(Resume r);

    void save(Resume r);

    void delete(String uuid);

    Resume get(String uuid);

    Resume[] getAll();
}
