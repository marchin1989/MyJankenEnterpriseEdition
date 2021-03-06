package com.example.janken.infrastructure.mysqldao;

import com.example.janken.domain.model.janken.Janken;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.dao.JankenDao;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.InsertMapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.JankenInsertMapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.JankenRowMapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.RowMapper;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JankenMySQLDao implements JankenDao {

    private static final String SELECT_FROM_CLAUSE = "SELECT id, played_at FROM jankens ";
    private static final String INSERT_COMMAND = "INSERT INTO jankens (id, played_at) VALUES (?, ?)";

    private final SimpleJDBCWrapper simpleJDBCWrapper = new SimpleJDBCWrapper();
    private final RowMapper<Janken> rowMapper = new JankenRowMapper();
    private final InsertMapper<Janken> insertMapper = new JankenInsertMapper();

    @Override
    public List<Janken> findAllOrderByPlayedAt(Transaction tx) {
        val sql = SELECT_FROM_CLAUSE + "ORDER BY played_at";
        return simpleJDBCWrapper.findList(tx, rowMapper, sql);
    }

    @Override
    public Optional<Janken> findById(Transaction tx, String id) {
        val sql = SELECT_FROM_CLAUSE + "WHERE id = ?";
        return simpleJDBCWrapper.findFirst(tx, rowMapper, sql, id);
    }

    @Override
    public long count(Transaction tx) {
        return simpleJDBCWrapper.count(tx, "jankens");
    }

    @Override
    public void insert(Transaction tx, Janken janken) {
        simpleJDBCWrapper.insertOne(tx, insertMapper, INSERT_COMMAND, janken);
    }
}
