package com.example.janken.infrastructure.mysqldao;

import com.example.janken.domain.model.janken.JankenDetail;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.dao.JankenDetailDao;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.InsertMapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.JankenDetailInsertMapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.JankenDetailRowMapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.RowMapper;
import lombok.val;

import java.util.List;
import java.util.Optional;

public class JankenDetailMySQLDao implements JankenDetailDao {

    private static final String SELECT_FROM_CLAUSE = "SELECT id, janken_id, player_id, hand, result " +
            "FROM janken_details ";
    private static final String INSERT_COMMAND = "INSERT INTO janken_details (id, janken_id, player_id, hand, result) " +
            "VALUES ";
    private static final String INSERT_COMMAND_VALUE_CLAUSE = "(?, ?, ?, ?, ?)";

    private final SimpleJDBCWrapper simpleJDBCWrapper = new SimpleJDBCWrapper();
    private final RowMapper<JankenDetail> rowMapper = new JankenDetailRowMapper();
    private final InsertMapper<JankenDetail> insertMapper = new JankenDetailInsertMapper();

    @Override
    public List<JankenDetail> findAllOrderById(Transaction tx) {
        val sql = SELECT_FROM_CLAUSE + "ORDER BY id";
        return simpleJDBCWrapper.findList(tx, rowMapper, sql);
    }

    @Override
    public Optional<JankenDetail> findById(Transaction tx, String id) {
        val sql = SELECT_FROM_CLAUSE + "WHERE id = ?";
        return simpleJDBCWrapper.findFirst(tx, rowMapper, sql, id);
    }

    @Override
    public List<JankenDetail> findByJankenIdOrderById(Transaction tx, String jankenId) {
        val sql = SELECT_FROM_CLAUSE + "WHERE janken_id = ? ORDER BY id";
        return simpleJDBCWrapper.findList(tx, rowMapper, sql, jankenId);
    }

    @Override
    public long count(Transaction tx) {
        return simpleJDBCWrapper.count(tx, "janken_details");
    }

    @Override
    public void insertAll(Transaction tx, List<JankenDetail> jankenDetails) {
        if (jankenDetails.isEmpty()) {
            return;
        }

        val sql = INSERT_COMMAND + jankenDetails.stream()
                .map(jd -> INSERT_COMMAND_VALUE_CLAUSE)
                .reduce((l, r) -> l + "," + r)
                .get();

        simpleJDBCWrapper.insertAll(tx, insertMapper, sql, jankenDetails);
    }
}
