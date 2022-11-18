package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.StreamStrategy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;
    private final StreamStrategy streamStrategy;

    protected PathStorage(String directoryString, StreamStrategy storageStrategy) {
        Objects.requireNonNull(directoryString, "directory must not be null");
        Objects.requireNonNull(storageStrategy, "streamStrategy must not be null");
        directory = Paths.get(directoryString);
        this.streamStrategy = storageStrategy;
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
            streamStrategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", getFileName(path), e);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Path create error", getFileName(path), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", getFileName(path), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return streamStrategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path doGet error", getFileName(path), e);
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        return getFilesStream().map(this::doGet).collect(Collectors.toList());
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
            throw new StorageException("Path getFilesStream error", null, e);
        }
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }
}
