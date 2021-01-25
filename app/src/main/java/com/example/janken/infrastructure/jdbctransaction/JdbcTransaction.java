package com.example.janken.infrastructure.jdbctransaction;

import com.example.janken.framework.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {
    static final String MYSQL_URL = "jdbc:mysql://localhost:3306/janken";
    static final String MYSQL_USER = "user";
    static final String MYSQL_PASSWORD = "password";

    public final Connection conn;

    public JdbcTransaction() {
        try {
            conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
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
