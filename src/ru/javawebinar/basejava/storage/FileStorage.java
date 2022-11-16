package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.StreamStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;
    private final StreamStrategy storageStrategy;

    protected FileStorage(File directory, StreamStrategy storageStrategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(storageStrategy, "storageStrategy must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
        this.storageStrategy = storageStrategy;
    }

    @Override
    protected File findSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            storageStrategy.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File update error", file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("File create error", file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return storageStrategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File doGet error", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        List<Resume> allFiles = new ArrayList<>();
        for (File file : getFilesArray()) {
            allFiles.add(doGet(file));
        }
        return allFiles;
    }

    @Override
    public void clear() {
        for (File file : getFilesArray()) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return getFilesArray().length;
    }

    private File[] getFilesArray() {
        File[] files = directory.listFiles();
        if (files != null) {
            return files;
        }
        return new File[0];
    }
}
