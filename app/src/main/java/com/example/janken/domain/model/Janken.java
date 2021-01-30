package com.example.janken.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Janken {

    public static Janken play(Player player1,
                              Hand hand1,
                              Player player2,
                              Hand hand2) {
        // 勝敗判定

        Result player1Result;
        Result player2Result;

        if (hand1.wins(hand2)) {
            player1Result = Result.WIN;
            player2Result = Result.LOSE;
        } else if (hand2.wins(hand1)) {
            player1Result = Result.LOSE;
            player2Result = Result.WIN;
        } else {
            player1Result = Result.DRAW;
            player2Result = Result.DRAW;
        }

        // じゃんけん明細を生成

        val jankenDetail1 = new JankenDetail(null, null, player1.getId(), hand1, player1Result);
        val jankenDetail2 = new JankenDetail(null, null, player2.getId(), hand2, player2Result);

        // じゃんけんを生成

        val playedAt = LocalDateTime.now();
        return new Janken(null, playedAt, jankenDetail1, jankenDetail2);
    }

    public Optional<Long> winnerPlayerId() {
        if (jankenDetail1.isResultWin()) {
            return Optional.of(jankenDetail1.getPlayerId());
        } else if (jankenDetail2.isResultWin()) {
            return Optional.of(jankenDetail2.getPlayerId());
        } else {
            return Optional.empty();
        }
    }

    private Long id;
    private LocalDateTime playedAt;
    private JankenDetail jankenDetail1;
    private JankenDetail jankenDetail2;
}
