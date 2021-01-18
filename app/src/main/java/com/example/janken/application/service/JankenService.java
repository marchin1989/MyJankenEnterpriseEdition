package com.example.janken.application.service;

import com.example.janken.domain.dao.JankenDao;
import com.example.janken.domain.dao.JankenDetailDao;
import com.example.janken.domain.model.*;
import com.example.janken.framework.ServiceLocator;
import lombok.val;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JankenService {

    private JankenDao jankenDao = ServiceLocator.resolve(JankenDao.class);
    private JankenDetailDao jankenDetailDao = ServiceLocator.resolve(JankenDetailDao.class);

    /**
     * じゃんけんを実行し、勝者を返す。
     */
    public Optional<Player> play(Player player1, Hand player1Hand, Player player2, Hand player2Hand) {
        // 勝敗判定

        Result player1Result;
        Result player2Result;
        if (player1Hand.equals(Hand.STONE)) {
            // プレイヤーがグーの場合

            if (player2Hand.equals(Hand.STONE)) {
                player1Result = Result.DRAW;
                player2Result = Result.DRAW;
            } else if (player2Hand.equals(Hand.PAPER)) {
                player1Result = Result.LOSE;
                player2Result = Result.WIN;
            } else {
                player1Result = Result.WIN;
                player2Result = Result.LOSE;
            }

        } else if (player1Hand.equals(Hand.PAPER)) {
            // プレイヤーがパーの場合

            if (player2Hand.equals(Hand.STONE)) {
                player1Result = Result.WIN;
                player2Result = Result.LOSE;
            } else if (player2Hand.equals(Hand.PAPER)) {
                player1Result = Result.DRAW;
                player2Result = Result.DRAW;
            } else {
                player1Result = Result.LOSE;
                player2Result = Result.WIN;
            }

        } else {
            // プレイヤーがチョキの場合

            if (player2Hand.equals(Hand.STONE)) {
                player1Result = Result.LOSE;
                player2Result = Result.WIN;
            } else if (player2Hand.equals(Hand.PAPER)) {
                player1Result = Result.WIN;
                player2Result = Result.LOSE;
            } else {
                player1Result = Result.DRAW;
                player2Result = Result.DRAW;
            }
        }

        // じゃんけんを生成

        val playedAt = LocalDateTime.now();
        val janken = new Janken(null, playedAt);

        // じゃんけんを保存

        val jankenWithId = jankenDao.insert(janken);

        // じゃんけん明細を生成

        val jankenDetail1 = new JankenDetail(null, jankenWithId.getId(), player1.getId(), player1Hand, player1Result);
        val jankenDetail2 = new JankenDetail(null, jankenWithId.getId(), player2.getId(), player2Hand, player2Result);
        val jankenDetails = List.of(jankenDetail1, jankenDetail2);

        // じゃんけん明細を保存

        jankenDetailDao.insertAll(jankenDetails);

        // 勝敗の表示

        Player winner = null;
        if (player1Result.equals(Result.WIN)) {
            winner = player1;
        } else if (player2Result.equals(Result.WIN)) {
            winner = player2;
        }

        return Optional.ofNullable(winner);
    }
}