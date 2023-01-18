package ru.javawebinar.basejava.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.model.AbstractSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.TextSection;

import static ru.javawebinar.basejava.TestData.RESUME_1;

class JsonParserTest {

    @Test
    void read() {
        String json = JsonParser.write(RESUME_1, Resume.class);
        Resume resume = JsonParser.read(json, Resume.class);
        Assertions.assertEquals(RESUME_1, resume);
    }

    @Test
    void write() {
        AbstractSection section1 = new TextSection("Аналитический склад ума, сильная логика, креативность");
        String json = JsonParser.write(section1, AbstractSection.class);
        AbstractSection section2 = JsonParser.read(json, AbstractSection.class);
        Assertions.assertEquals(section1, section2);
    }
}