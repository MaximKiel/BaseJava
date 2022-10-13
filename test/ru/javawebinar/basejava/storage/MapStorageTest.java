package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Test
    @Disabled
    void getAll() {}

    @Test
    @Disabled
    void getOverflowException() {}
}