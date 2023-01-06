package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        helper.execute("UPDATE resume SET full_name = ? WHERE uuid = ?", preparedStatement -> {
            preparedStatement.setString(1, r.getFullName());
            preparedStatement.setString(2, r.getUuid());
            if (preparedStatement.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
            helper.execute("UPDATE contact SET type = ?, value = ? WHERE resume_uuid = ?", preparedStatement -> {
                preparedStatement.setString(1, e.getKey().name());
                preparedStatement.setString(2, e.getValue());
                preparedStatement.setString(3, r.getUuid());
                preparedStatement.execute();
                return null;
            });
        }
    }

    @Override
    public void save(Resume r) {
        helper.transactionalExecute(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                preparedStatement.setString(1, r.getUuid());
                preparedStatement.setString(2, r.getFullName());
                preparedStatement.execute();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
                for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                    preparedStatement.setString(1, r.getUuid());
                    preparedStatement.setString(2, e.getKey().name());
                    preparedStatement.setString(3, e.getValue());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
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
                        String value = resultSet.getString("value");
                        ContactType type = ContactType.valueOf(resultSet.getString("type"));
                        resume.addContact(type, value);
                    } while (resultSet.next());
                    return resume;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> allResumes = new ArrayList<>();
        helper.execute("SELECT * FROM resume ORDER BY full_name", preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allResumes.add(new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
            }
            return null;
        });
        helper.execute("SELECT * FROM contact", preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            for (Resume resume : allResumes) {
                do {
                    String value = resultSet.getString("value");
                    ContactType type = ContactType.valueOf(resultSet.getString("type"));
                    resume.addContact(type, value);
                } while (resultSet.next());
            }
            return null;
        });
        return allResumes;
    }
}
