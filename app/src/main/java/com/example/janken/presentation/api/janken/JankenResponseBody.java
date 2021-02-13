package com.example.janken.presentation.api.janken;

import com.example.janken.domain.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class JankenResponseBody {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static JankenResponseBody of(Optional<Player> maybeWinner) {
        return new JankenResponseBody(maybeWinner.map(Player::getName).orElse(null));
    }

    private final String winnerPlayerName;
}
