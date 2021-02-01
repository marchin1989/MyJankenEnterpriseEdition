package com.example.janken.application.service;

import com.example.janken.domain.dao.JankenDao;
import com.example.janken.domain.dao.JankenDetailDao;
import com.example.janken.domain.dao.PlayerDao;
import com.example.janken.domain.model.Hand;
import com.example.janken.domain.model.Janken;
import com.example.janken.domain.model.Player;
import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.registry.ServiceLocator;
import lombok.val;

import java.util.List;
import java.util.Optional;

public class JankenApplicationService {

    private final TransactionManager tm = ServiceLocator.resolve(TransactionManager.class);

    private final JankenDao jankenDao = ServiceLocator.resolve(JankenDao.class);
    private final JankenDetailDao jankenDetailDao = ServiceLocator.resolve(JankenDetailDao.class);
    private final PlayerDao playerDao = ServiceLocator.resolve(PlayerDao.class);

    /**
     * じゃんけんを実行し、勝者を返す。
     */
    public Optional<Player> play(Player player1, Hand player1Hand, Player player2, Hand player2Hand) {

        return tm.transactional(tx -> {

            // じゃんけんを実行
            val janken = Janken.play(player1, player1Hand, player2, player2Hand);

            // じゃんけんとじゃんけん明細を保存

            val jankenWithId = jankenDao.insert(tx, janken);
            jankenDetailDao.insertAll(tx, List.of(jankenWithId.getJankenDetail1(), jankenWithId.getJankenDetail2()));

            // 勝敗を返却

            return jankenWithId.winnerPlayerId()
                    .map(id -> playerDao.findPlayerById(tx, id));
        });
    }
}
