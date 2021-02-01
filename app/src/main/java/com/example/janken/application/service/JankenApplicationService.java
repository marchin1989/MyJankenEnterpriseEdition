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
    public Optional<Player> play(Player player1, Hand player1Hand, Player player2, Hand player2Hand) {

        return tm.transactional(tx -> {

            // じゃんけんを実行
            val janken = Janken.play(player1, player1Hand, player2, player2Hand);

            // じゃんけんとじゃんけん明細を保存
            val jankenWithId = jankenRepository.save(tx, janken);

            // 勝敗を返却

            return jankenWithId.winnerPlayerId()
                    .map(id -> playerRepository.findPlayerById(tx, id));
        });
    }
}
