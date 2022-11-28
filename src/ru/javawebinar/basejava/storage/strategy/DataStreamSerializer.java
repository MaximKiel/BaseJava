package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(r.getUuid());
            dataOutputStream.writeUTF(r.getFullName());
            Map <ContactType, String> contacts = r.getContacts();
            writeList(dataOutputStream, contacts.entrySet(), contactTypeStringEntry -> {
                dataOutputStream.writeUTF(contactTypeStringEntry.getKey().name());
                dataOutputStream.writeUTF(contactTypeStringEntry.getValue());
            });

            Map <SectionType, AbstractSection> sections = r.getSections();
            writeList(dataOutputStream, sections.entrySet(), sectionTypeAbstractSectionEntry -> {
                SectionType type = sectionTypeAbstractSectionEntry.getKey();
                AbstractSection section = sectionTypeAbstractSectionEntry.getValue();
                dataOutputStream.writeUTF(type.name());
                switch (type) {
                    case PERSONAL, OBJECTIVE -> dataOutputStream.writeUTF(((TextSection) section).get());
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection listSection = (ListSection) section;
                        writeList(dataOutputStream, listSection.get(), dataOutputStream::writeUTF);
                    }
                    case EXPERIENCE, EDUCATION -> {
                        OrganizationSection organizationSection = (OrganizationSection) section;
                        writeList(dataOutputStream, organizationSection.get(), organization -> {
                            dataOutputStream.writeUTF(organization.getName());
                            dataOutputStream.writeUTF(organization.getWebsite());
                            writeList(dataOutputStream, organization.getPeriods(), period -> {
                                dataOutputStream.writeUTF(period.getTitle());
                                dataOutputStream.writeUTF(period.getDescription());
                                dataOutputStream.writeInt(period.getStartDate().getYear());
                                dataOutputStream.writeInt(period.getStartDate().getMonth().getValue());
                                dataOutputStream.writeInt(period.getEndDate().getYear());
                                dataOutputStream.writeInt(period.getEndDate().getMonth().getValue());
                            });
                        });
                    }
                }
            });
        }
    }

    private <T> void writeList(DataOutputStream dataOutputStream, Collection<T> collection, WriterInterface<T> writer)
            throws IOException {
        dataOutputStream.writeInt(collection.size());
        for (T text : collection) {
            writer.write(text);
        }
    }

    private interface WriterInterface<T> {
        void write(T t) throws IOException;
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            String uuid = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();

            Resume resume = new Resume(uuid, fullName);

            int sizeContacts = dataInputStream.readInt();
            for (int i = 0; i < sizeContacts; i++) {
                resume.addContact(ContactType.valueOf(dataInputStream.readUTF()), dataInputStream.readUTF());
            }

            int sizeSections = dataInputStream.readInt();
            for (int i = 0; i < sizeSections; i++) {
                SectionType sectionType = SectionType.valueOf(dataInputStream.readUTF());
                resume.addSection(sectionType, readSection(sectionType, dataInputStream));
            }
            return resume;
        }
    }

    private AbstractSection readSection(SectionType sectionType, DataInputStream dataInputStream) throws IOException {
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> {
                return new TextSection(dataInputStream.readUTF());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                return new ListSection(readList(dataInputStream, dataInputStream::readUTF));
            }
            case EXPERIENCE, EDUCATION -> {
                return new OrganizationSection(readList(dataInputStream,() ->
                        new Organization(dataInputStream.readUTF(), dataInputStream.readUTF(),
                                readList(dataInputStream, () -> new Organization.Period(dataInputStream.readUTF(),
                                        dataInputStream.readUTF(), LocalDate.of(dataInputStream.readInt(),
                                        dataInputStream.readInt(), 1), LocalDate.of(dataInputStream.readInt(),
                                        dataInputStream.readInt(), 1)))))

                );
                }
            default -> throw new IllegalStateException("Unexpected value: " + sectionType);
        }
    }

    private <T> List<T> readList(DataInputStream dataInputStream, ReaderInterface<T> reader) throws IOException {
        List<T> list = new ArrayList<>();
        int size = dataInputStream.readInt();
        for (int i = 0; i < size; i++) {
            list.add(reader.read());
        }
        return list;
    }

    private interface ReaderInterface<T> {
        T read() throws IOException;
    }
}
