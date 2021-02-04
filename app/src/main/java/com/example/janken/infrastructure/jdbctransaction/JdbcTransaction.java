package com.example.janken.infrastructure.jdbctransaction;

import com.example.janken.domain.transaction.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    static final String DEFAULT_MYSQL_HOST = "localhost";
    static final String DEFAULT_MYSQL_DATABASE = "janken";
    static final String DEFAULT_MYSQL_USER = "user";
    static final String DEFAULT_MYSQL_PASSWORD = "password";

    // 環境変数から読み込む
    static final String MYSQL_HOST_ENV_VARIABLE = System.getenv("MYSQL_HOST");
    static final String MYSQL_DATABASE_ENV_VARIABLE = System.getenv("MYSQL_DATABASE");
    static final String MYSQL_USER_ENV_VARIABLE = System.getenv("MYSQL_USER");
    static final String MYSQL_PASSWORD_ENV_VARIABLE = System.getenv("MYSQL_PASSWORD");

    static final String MYSQL_HOST = MYSQL_HOST_ENV_VARIABLE != null
            ? MYSQL_HOST_ENV_VARIABLE
            : DEFAULT_MYSQL_HOST;
    static final String MYSQL_DATABASE = MYSQL_DATABASE_ENV_VARIABLE != null
            ? MYSQL_DATABASE_ENV_VARIABLE
            : DEFAULT_MYSQL_DATABASE;
    static final String MYSQL_USER = MYSQL_USER_ENV_VARIABLE != null
            ? MYSQL_USER_ENV_VARIABLE
            : DEFAULT_MYSQL_USER;
    static final String MYSQL_PASSWORD = MYSQL_PASSWORD_ENV_VARIABLE != null
            ? MYSQL_PASSWORD_ENV_VARIABLE
            : DEFAULT_MYSQL_PASSWORD;

    static final String MYSQL_URL = "jdbc:mysql://" + MYSQL_HOST + ":3306/" + MYSQL_DATABASE;


    public final Connection conn;

    public JdbcTransaction() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
            conn.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
