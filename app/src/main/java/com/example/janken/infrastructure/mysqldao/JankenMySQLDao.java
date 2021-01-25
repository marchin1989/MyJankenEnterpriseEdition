package com.example.janken.infrastructure.mysqldao;

import com.example.janken.domain.dao.JankenDao;
import com.example.janken.domain.model.Janken;
import com.example.janken.framework.Transaction;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransaction;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JankenMySQLDao implements JankenDao {

    private static final String SELECT_WHERE_ID_EQUALS_QUERY = "SELECT id, played_at " +
            "FROM jankens " +
            "WHERE id = ?";
    private static final String INSERT_COMMAND = "INSERT INTO jankens (played_at) VALUES (?)";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM jankens";

    @Override
    public Optional<Janken> findById(Transaction tx, long id) {
        val conn = ((JdbcTransaction) tx).conn;
        try (val stmt = conn.prepareStatement(SELECT_WHERE_ID_EQUALS_QUERY)) {

            stmt.setLong(1, id);

            try (val rs = stmt.executeQuery()) {
                return resultSet2Jankens(rs).stream().findFirst();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long count(Transaction tx) {
        val conn = ((JdbcTransaction) tx).conn;
        try (val stmt = conn.prepareStatement(COUNT_QUERY)) {
            try (val rs = stmt.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private List<Janken> resultSet2Jankens(ResultSet rs) throws SQLException {
        val jankens = new ArrayList<Janken>();
        while (rs.next()) {
            jankens.add(resultSet2Janken(rs));
        }
        return jankens;
    }

    private Janken resultSet2Janken(ResultSet rs) throws SQLException {
        val id = rs.getLong("id");
        val playedAt = rs.getTimestamp("played_at").toLocalDateTime();
        return new Janken(id, playedAt);
    }
}
