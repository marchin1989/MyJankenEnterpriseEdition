package com.example.janken.application.service;

import com.example.janken.domain.model.janken.Hand;
import com.example.janken.domain.model.janken.JankenDetail;
import com.example.janken.domain.model.player.Player;
import com.example.janken.domain.transaction.Transaction;
import com.example.janken.infrastructure.dao.JankenDetailDao;
import com.example.janken.infrastructure.jdbctransaction.JdbcTransactionManager;
import com.example.janken.infrastructure.mysqldao.JankenMySQLDao;
import com.example.janken.infrastructure.mysqldao.PlayerMySQLDao;
import com.example.janken.infrastructure.mysqlrepository.JankenMySQLRepository;
import com.example.janken.infrastructure.mysqlrepository.PlayerMySQLRepository;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JankenApplicationServiceTest {

    @Test
    public void ジャンケン明細の保存時にエラーが発生した場合じゃんけんも保存されない() {
        // 準備

        // 実際のMySQL(docker container)でテストする

        val tm = new JdbcTransactionManager();

        val playerDao = new PlayerMySQLDao();
        val jankenDao = new JankenMySQLDao();
        val jankenDetailDao = new JankenDetailErrorDao();

        val playerRepository = new PlayerMySQLRepository(playerDao);
        val jankenRepository = new JankenMySQLRepository(jankenDao, jankenDetailDao);
        val jankenService = new JankenApplicationService(tm, playerRepository, jankenRepository);

        val player1 = new Player("1", "Alice");
        val player1Hand = Hand.STONE;
        val player2 = new Player("2", "Bob");
        val player2Hand = Hand.PAPER;

        val beforeJankenCount = tm.transactional(jankenRepository::count);

        // 実行
        try {
            jankenService.play(player1.getId(), player1Hand, player2.getId(), player2Hand);

            // 例外が発生しない場合は、テスト失敗
            fail();
        } catch (UnsupportedOperationException e) {
            // Do nothing
        }

        // 検証
        val afterJankenCount = tm.transactional(jankenRepository::count);
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
    public Optional<JankenDetail> findById(Transaction tx, String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<JankenDetail> findByJankenIdOrderById(Transaction tx, String jankenId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count(Transaction tx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertAll(Transaction t, List<JankenDetail> jankenDetails) {
        throw new UnsupportedOperationException();
    }
}