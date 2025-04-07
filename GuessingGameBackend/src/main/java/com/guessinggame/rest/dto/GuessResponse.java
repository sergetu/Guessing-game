package com.guessinggame.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuessResponse {

    private GameStatus status;
    private boolean win;
    private boolean gameOver;
}
