package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.ResumeTestData;

import java.util.UUID;

public class TestData {

    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();
    public static final String DUMMY = "dummy";

    public static final String NAME_1 = "name1";
    public static final String NAME_2 = "name2";
    public static final String NAME_3 = "name3";
    public static final String NAME_4 = "name4";

    public static final ResumeTestData RESUME_TEST_DATA = new ResumeTestData();
    public static final Resume RESUME_1 = RESUME_TEST_DATA.createResume(UUID_1, NAME_1);
    public static final Resume RESUME_2 = RESUME_TEST_DATA.createResume(UUID_2, NAME_2);
    public static final Resume RESUME_3 = RESUME_TEST_DATA.createResume(UUID_3, NAME_3);
    public static final Resume RESUME_4 = RESUME_TEST_DATA.createResume(UUID_4, NAME_4);
}
