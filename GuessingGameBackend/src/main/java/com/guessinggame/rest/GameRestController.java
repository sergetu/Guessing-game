package com.guessinggame.rest;

import com.guessinggame.db.entitys.GameRecord;
import com.guessinggame.rest.dto.GameStatus;
import com.guessinggame.rest.dto.GuessRequest;
import com.guessinggame.rest.dto.GuessResponse;
import com.guessinggame.services.GameRecordService;
import com.guessinggame.services.LeaderboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@AllArgsConstructor
public class GameRestController {
    private final GameRecordService gameRecordService;
    private final LeaderboardService leaderboardService;

    @PostMapping("/guess")
    public GuessResponse makeGuess(@RequestBody GuessRequest request) {
        Long playerId = request.getUserId();
        String guess = request.getGuess();


        GameRecord lastUserGameRecord = gameRecordService.getLastUserGame(playerId);
        if (lastUserGameRecord.getId() == null) {
            return new GuessResponse(null, false, true);
        }
        if (lastUserGameRecord.isGameClosed()) {
            return new GuessResponse(null, false, true);
        }

        int attempt = lastUserGameRecord.getTriesUsed() + 1;
        GameStatus status = gameRecordService.validateAndCheck(lastUserGameRecord, guess);

        boolean win = status.getP() == 4;
        boolean gameOver = win || attempt >= 8;

        gameRecordService.saveTheGame(lastUserGameRecord, attempt, win, gameOver);
        if (gameOver) {
            leaderboardService.recalculateUser(lastUserGameRecord.getUser());
        }

        return new GuessResponse(status, win, gameOver);
    }

    @GetMapping("/new-game")
    public GuessResponse newGame(@RequestParam long userId) {
        return new GuessResponse(gameRecordService.newGame(userId), false, false);
    }

    @GetMapping("/get-code")
    public String getCode(@RequestParam long userId) {
        GameRecord gameRecord = gameRecordService.getLastUserGame(userId);
        if (gameRecord.isGameClosed())
            return gameRecord.getCode();
        return "Game not ended";
    }
}
