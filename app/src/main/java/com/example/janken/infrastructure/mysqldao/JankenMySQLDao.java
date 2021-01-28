package com.example.janken.infrastructure.mysqldao;

import com.example.janken.domain.dao.JankenDao;
import com.example.janken.domain.model.Janken;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransaction;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.JankenRowMapper;
import lombok.val;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class JankenMySQLDao implements JankenDao {

    private static final String SELECT_FROM_CLAUSE = "SELECT id, played_at FROM jankens ";
    private static final String INSERT_COMMAND = "INSERT INTO jankens (played_at) VALUES (?)";

    private final SimpleJDBCWrapper simpleJDBCWrapper = new SimpleJDBCWrapper();
    private final JankenRowMapper mapper = new JankenRowMapper();

    @Override
    public List<Janken> findAllOrderById(Transaction tx) {
        val sql = SELECT_FROM_CLAUSE + "ORDER BY id";
        return simpleJDBCWrapper.findList(tx, mapper, sql);
    }

    @Override
    public Optional<Janken> findById(Transaction tx, long id) {
        val sql = SELECT_FROM_CLAUSE + "WHERE id = ?";
        return simpleJDBCWrapper.findFirst(tx, mapper, sql, id);
    }

    @Override
    public long count(Transaction tx) {
        return simpleJDBCWrapper.count(tx, "jankens");
    }

    @Override
    public Janken insert(Transaction tx, Janken janken) {
        val conn = ((JdbcTransaction) tx).conn;
        try (val stmt = conn.prepareStatement(INSERT_COMMAND, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(janken.getPlayedAt()));
            stmt.executeUpdate();

            try (val rs = stmt.getGeneratedKeys()) {
                rs.next();
                return new Janken(rs.getLong(1), janken.getPlayedAt());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
