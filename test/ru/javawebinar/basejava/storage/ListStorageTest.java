package ru.javawebinar.basejava.storage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test
    @Disabled
    void getOverflowException() {}
}