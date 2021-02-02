package com.example.janken.application.service;

import com.example.janken.domain.model.janken.Hand;
import com.example.janken.domain.model.janken.Janken;
import com.example.janken.domain.model.janken.JankenRepository;
import com.example.janken.domain.model.player.Player;
import com.example.janken.domain.model.player.PlayerRepository;
import com.example.janken.domain.transaction.TransactionManager;
import com.example.janken.registry.ServiceLocator;
import lombok.val;

import java.util.Optional;

public class JankenApplicationService {

    private final TransactionManager tm = ServiceLocator.resolve(TransactionManager.class);

    private final PlayerRepository playerRepository = ServiceLocator.resolve(PlayerRepository.class);
    private final JankenRepository jankenRepository = ServiceLocator.resolve(JankenRepository.class);

    /**
     * じゃんけんを実行し、勝者を返す。
     */
    public Optional<Player> play(String player1Id, Hand player1Hand, String player2Id, Hand player2Hand) {

        return tm.transactional(tx -> {

            // じゃんけんを実行
            val janken = Janken.play(player1Id, player1Hand, player2Id, player2Hand);

            // じゃんけんとじゃんけん明細を保存
            jankenRepository.save(tx, janken);

            // 勝敗を返却
            return janken.winnerPlayerId()
                    .map(id -> playerRepository.findPlayerById(tx, id));
        });
    }
}
