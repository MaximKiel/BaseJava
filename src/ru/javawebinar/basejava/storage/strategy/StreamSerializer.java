package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.Resume;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamSerializer {

    void doWrite(Resume r, OutputStream outputStream) throws IOException;

    Resume doRead(InputStream inputStream) throws IOException;
}
