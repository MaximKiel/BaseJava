package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.StreamStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;
    private final StreamStrategy storageStrategy;

    protected PathStorage(String directoryString, StreamStrategy storageStrategy) {
        directory = Paths.get(directoryString);
        this.storageStrategy = storageStrategy;
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(storageStrategy, "storageStrategy must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directoryString + " is not directory or is not writable");
        }
    }

    @Override
    protected Path findSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            storageStrategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path update error", path.toFile().getName(), e);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Path create file error", path.toFile().getName(), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.toFile().getName(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return storageStrategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path doGet error", path.toFile().getName(), e);
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        List<Resume> allFiles = new ArrayList<>();
        getFilesStream().forEach(searchKey -> allFiles.add(doGet(searchKey)));
        return allFiles;
    }

    @Override
    public void clear() {
        getFilesStream().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return getFilesStream().toList().size();
    }

    private Stream<Path> getFilesStream() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Path getFilesStream error", null);
        }
    }
}
