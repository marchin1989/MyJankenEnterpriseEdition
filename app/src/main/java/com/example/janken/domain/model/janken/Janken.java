package com.example.janken.domain.model.janken;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Janken {

    public static Janken play(String player1Id,
                              Hand hand1,
                              String player2Id,
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

        // idを生成
        val jankenId = generateId();
        val jankenDetail1Id = generateId();
        val jankenDetail2Id = generateId();

        // じゃんけん明細を生成

        val jankenDetail1 = new JankenDetail(jankenDetail1Id, jankenId, player1Id, hand1, player1Result);
        val jankenDetail2 = new JankenDetail(jankenDetail2Id, jankenId, player2Id, hand2, player2Result);

        // じゃんけんを生成

        val playedAt = LocalDateTime.now();
        return new Janken(jankenId, playedAt, jankenDetail1, jankenDetail2);
    }

    private static String generateId() {
        return UUID.randomUUID().toString();
    }

    private String id;
    private LocalDateTime playedAt;
    private JankenDetail jankenDetail1;
    private JankenDetail jankenDetail2;

    public Optional<String> winnerPlayerId() {
        if (jankenDetail1.isResultWin()) {
            return Optional.of(jankenDetail1.getPlayerId());
        } else if (jankenDetail2.isResultWin()) {
            return Optional.of(jankenDetail2.getPlayerId());
        } else {
            return Optional.empty();
        }
    }

    public List<JankenDetail> details() {
        return List.of(jankenDetail1, jankenDetail2);
    }
}
