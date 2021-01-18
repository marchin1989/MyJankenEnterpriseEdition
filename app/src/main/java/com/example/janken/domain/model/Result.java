package com.example.janken.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Result {
    WIN(0),
    LOSE(1),
    DRAW(2);

    public static Result of(int value) {
        return Arrays.stream(values())
                .filter(r -> r.value == value)
                .findFirst()
                .orElseThrow();
    }

    private int value;
}
