package com.example.janken.presentation.api.janken;

import com.example.janken.domain.model.janken.Hand;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class JankenPostRequestBody {
    @NotBlank
    private final String player1Id;
    @NotNull
    private final int player1Hand;
    @NotBlank
    private final String player2Id;
    @NotNull
    private final int player2Hand;

    Hand player1Hand() {
        return Hand.of(player1Hand);
    }

    Hand player2Hand() {
        return Hand.of(player2Hand);
    }
}
