package com.guessinggame.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardResponse {
    private String name;
    private long gamesPlayed;
    private long gamesWon;
    private long totalTries;

}
