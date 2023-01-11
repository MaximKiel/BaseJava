package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.basejava.model.SectionType.*;

public class SqlStorage implements Storage {

    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.execute("DELETE FROM resume", preparedStatement -> {
            preparedStatement.execute();
            return null;
        });
    }

    @Override
    public int size() {
        return helper.execute("SELECT COUNT(*) FROM resume", preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : 0;
        });
    }

    @Override
    public void update(Resume r) {
        helper.transactionalExecute(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                preparedStatement.setString(1, r.getFullName());
                preparedStatement.setString(2, r.getUuid());
                if (preparedStatement.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.execute();
            }
            insertContacts(r, connection);
            insertSections(r, connection);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.setString(2, r.getFullName());
                preparedStatement.execute();
            }
            insertContacts(r, connection);
            insertSections(r, connection);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        helper.execute("DELETE FROM resume WHERE uuid = ?", preparedStatement -> {
            preparedStatement.setString(1, uuid);
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return helper.execute("" +
                        "   SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "       ON r.uuid = c.resume_uuid " +
                        "    WHERE r.uuid = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, resultSet.getString("full_name"));
                    do {
                        addContacts(resultSet, resume);
                    } while (resultSet.next());
                    return resume;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> allResumes = new ArrayList<>();
        helper.transactionalExecute(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume r ORDER BY r.full_name")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Resume resume = new Resume(resultSet.getString("uuid"), resultSet.getString("full_name"));
                    allResumes.add(resume);
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contact")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    for (Resume resume : allResumes) {
                        if (resume.getUuid().equals(resultSet.getString("resume_uuid"))) {
                            addContacts(resultSet, resume);
                        }
                    }
                }
                preparedStatement.executeBatch();
            }
            return null;
        });
        return allResumes;
    }

    private static void addContacts(ResultSet resultSet, Resume resume) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            ContactType type = ContactType.valueOf(resultSet.getString("type"));
            resume.addContact(type, value);
        }
    }

    private static void insertContacts(Resume r, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.setString(2, e.getKey().name());
                preparedStatement.setString(3, e.getValue());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void addTextSections(ResultSet resultSet, Resume resume) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            SectionType type = SectionType.valueOf(resultSet.getString("type"));
            resume.addSection(type, new TextSection(value));
        }
    }

    private static void insertSections(Resume r, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
                preparedStatement.setString(1, r.getUuid());
                String sectionName = e.getKey().name();
                preparedStatement.setString(2, sectionName);

                if (sectionName.equals(PERSONAL.getTitle()) || sectionName.equals(OBJECTIVE.getTitle())) {
                    preparedStatement.setString(3, String.valueOf(e.getValue()));
                } else if (sectionName.equals(ACHIEVEMENT.getTitle()) || sectionName.equals(QUALIFICATIONS.getTitle())) {
                    List<String> listSectionValue = ((ListSection) e.getValue()).get();
                    int count = listSectionValue.size();
                    StringBuilder sectionValue = new StringBuilder();
                    for (String value : listSectionValue) {
                        count--;
                        if (count > 1) {
                            sectionValue.append(value).append("\n");
                        } else {
                            sectionValue.append(value);
                        }
                    }
                    preparedStatement.setString(3, String.valueOf(sectionValue));
                }

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void addListSections(ResultSet resultSet, Resume resume) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            String[] valueArray = value.split("\n");
            SectionType type = SectionType.valueOf(resultSet.getString("type"));
            resume.addSection(type, new ListSection(valueArray));
        }
    }

//    private static void insertListSections(Resume r, Connection connection) throws SQLException {
//        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?, ?, ?)")) {
//            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
//                preparedStatement.setString(1, r.getUuid());
//                preparedStatement.setString(2, e.getKey().name());
//
//                List<String> listSectionValue = ((ListSection) e.getValue()).get();
//                int count = listSectionValue.size();
//                StringBuilder sectionValue = new StringBuilder();
//                for (String value : listSectionValue) {
//                    count--;
//                    if (count > 1) {
//                        sectionValue.append(value).append("\n");
//                    } else {
//                        sectionValue.append(value);
//                    }
//                }
//
//                preparedStatement.setString(3, String.valueOf(sectionValue));
//                preparedStatement.addBatch();
//            }
//            preparedStatement.executeBatch();
//        }
//    }
}
