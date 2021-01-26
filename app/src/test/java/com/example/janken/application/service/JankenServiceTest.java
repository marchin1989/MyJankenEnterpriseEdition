package com.example.janken.application.service;

import com.example.janken.domain.dao.JankenDao;
import com.example.janken.domain.dao.JankenDetailDao;
import com.example.janken.domain.model.Hand;
import com.example.janken.domain.model.JankenDetail;
import com.example.janken.domain.model.Player;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.mysqldao.JankenMySQLDao;
import com.example.janken.registry.ServiceLocator;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JankenServiceTest {

    @Test
    public void ジャンケン明細の保存時にエラーが発生した場合じゃんけんも保存されない() {
        // 準備

        // 実際のMySQL(docker container)でテストする
        ServiceLocator.register(JankenDao.class, JankenMySQLDao.class);
        ServiceLocator.register(JankenDetailDao.class, JankenDetailErrorDao.class);
        ServiceLocator.register(TransactionManager.class, JdbcTransactionManager.class);

        val jankenService = new JankenService();
        JankenDao jankenDao = ServiceLocator.resolve(JankenDao.class);
        val tm = ServiceLocator.resolve(TransactionManager.class);

        val player1 = new Player(1L, "Alice");
        val player1Hand = Hand.STONE;
        val player2 = new Player(2L, "Bob");
        val player2Hand = Hand.PAPER;

        val beforeJankenCount = tm.transactional(jankenDao::count);

        // 実行
        try {
            jankenService.play(player1, player1Hand, player2, player2Hand);

            // 例外が発生しない場合は、テスト失敗
            fail();
        } catch (UnsupportedOperationException e) {
            // Do nothing
        }

        // 検証
        val afterJankenCount = tm.transactional(jankenDao::count);
        assertEquals(beforeJankenCount, afterJankenCount, "じゃんけんのデータが増えていない");
    }
}

@NoArgsConstructor
class JankenDetailErrorDao implements JankenDetailDao {

    @Override
    public List<JankenDetail> findAllOrderById(Transaction tx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<JankenDetail> findById(Transaction tx, long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count(Transaction tx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<JankenDetail> insertAll(Transaction t, List<JankenDetail> jankenDetails) {
        throw new UnsupportedOperationException();
    }
}