package com.guessinggame.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStatus {

    private Long gameId;

    private int attemptNumber;
    private int m;
    private int p;

    @Override
    public String toString() {
        return String.format("M:%d; P:%d", m, p);
    }
}
