package com.guessinggame.rest;

import com.guessinggame.rest.dto.LeaderboardResponse;
import com.guessinggame.services.LeaderboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@AllArgsConstructor
public class LeaderboardRestController {
    private final LeaderboardService leaderboardService;

    @GetMapping
    public List<LeaderboardResponse> getLeaderboard(@RequestParam Long minPlayedGames) {
        return leaderboardService.getLeaderboard(minPlayedGames);
    }
}
