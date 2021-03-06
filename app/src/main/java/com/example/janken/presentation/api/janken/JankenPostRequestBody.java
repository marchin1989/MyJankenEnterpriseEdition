package com.example.janken.presentation.api.janken;

import com.example.janken.domain.model.janken.Hand;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class JankenPostRequestBody {
    private final String player1Id;
    private final int player1Hand;
    private final String player2Id;
    private final int player2Hand;

    Hand player1Hand() {
        return Hand.of(player1Hand);
    }

    Hand player2Hand() {
        return Hand.of(player2Hand);
    }
}
