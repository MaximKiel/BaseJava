package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File searchKey) {
        return searchKey.exists();
    }

    @Override
    protected void doUpdate(Resume r, File searchKey) {
        try {
            doWrite(r, searchKey);
        } catch (IOException e) {
            throw new StorageException("IO error", searchKey.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File searchKey) {
        try {
            searchKey.createNewFile();
            doWrite(r, searchKey);
        } catch (IOException e) {
            throw new StorageException("IO error", searchKey.getName(), e);
        }
    }

    @Override
    protected void doDelete(File searchKey) {
        searchKey.delete();
    }

    @Override
    protected Resume doGet(File searchKey) {
        return doRead(searchKey);
    }

    @Override
    protected List<Resume> doGetAll() {
        List<Resume> allFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                allFiles.add(doRead(file));
            }
        }
        return allFiles;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list != null) {
            return list.length;
        }
        return 0;
    }

    protected abstract void doWrite(Resume r, File searchKey) throws IOException;

    protected abstract Resume doRead(File file);
}
