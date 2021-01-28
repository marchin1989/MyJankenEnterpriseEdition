package com.example.janken.infrastructure.mysqldao;

import com.example.janken.domain.dao.JankenDetailDao;
import com.example.janken.domain.model.JankenDetail;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransaction;
import com.example.janken.infrastructure.jdbctransaction.SimpleJDBCWrapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.JankenDetailRowMapper;
import com.example.janken.infrastructure.jdbctransaction.mapper.RowMapper;
import lombok.val;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JankenDetailMySQLDao implements JankenDetailDao {

    private static final String SELECT_FROM_CLAUSE = "SELECT id, janken_id, player_id, hand, result " +
            "FROM janken_details ";
    private static final String INSERT_COMMAND = "INSERT INTO janken_details (janken_id, player_id, hand, result) " +
            "VALUES ";
    private static final String INSERT_COMMAND_VALUE_CLAUSE = "(?, ?, ?, ?)";

    private final SimpleJDBCWrapper simpleJDBCWrapper = new SimpleJDBCWrapper();
    private final RowMapper<JankenDetail> mapper = new JankenDetailRowMapper();

    @Override
    public List<JankenDetail> findAllOrderById(Transaction tx) {
        val sql = SELECT_FROM_CLAUSE + "ORDER BY id";
        return simpleJDBCWrapper.findList(tx, mapper, sql);
    }

    @Override
    public Optional<JankenDetail> findById(Transaction tx, long id) {
        val sql = SELECT_FROM_CLAUSE + "WHERE id = ?";
        return simpleJDBCWrapper.findFirst(tx, mapper, sql, id);
    }

    @Override
    public long count(Transaction tx) {
        return simpleJDBCWrapper.count(tx, "janken_details");
    }

    @Override
    public List<JankenDetail> insertAll(Transaction tx, List<JankenDetail> jankenDetails) {
        if (jankenDetails.isEmpty()) {
            return new ArrayList<>();
        }

        val command = INSERT_COMMAND + jankenDetails.stream()
                .map(jd -> INSERT_COMMAND_VALUE_CLAUSE)
                .reduce((l, r) -> l + "," + r)
                .get();

        val conn = ((JdbcTransaction) tx).conn;
        try (val stmt = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS)) {

            // SQLの実行
            for (int i = 0; i < jankenDetails.size(); i++) {
                val jd = jankenDetails.get(i);
                val placeHolderOffSet = 4 * i;

                stmt.setLong(placeHolderOffSet + 1, jd.getJankenId());
                stmt.setLong(placeHolderOffSet + 2, jd.getPlayerId());
                stmt.setLong(placeHolderOffSet + 3, jd.getHand().getValue());
                stmt.setLong(placeHolderOffSet + 4, jd.getResult().getValue());
            }
            stmt.executeUpdate();

            val jankenDetailsWithId = new ArrayList<JankenDetail>();
            try (val rs = stmt.getGeneratedKeys()) {
                for (JankenDetail jd : jankenDetails) {
                    rs.next();
                    val id = rs.getLong(1);
                    jankenDetailsWithId.add(new JankenDetail(id,
                            jd.getJankenId(),
                            jd.getPlayerId(),
                            jd.getHand(),
                            jd.getResult()));
                }
            }
            return jankenDetailsWithId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
