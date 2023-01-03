package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlExecute<T> {

    T doExecute(PreparedStatement preparedStatement) throws SQLException;
}
