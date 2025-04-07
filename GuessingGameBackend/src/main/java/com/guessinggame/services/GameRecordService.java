package com.guessinggame.services;

import com.guessinggame.db.entitys.GameRecord;
import com.guessinggame.db.entitys.User;
import com.guessinggame.db.repositories.GameRecordsRepository;
import com.guessinggame.db.repositories.UserRepository;
import com.guessinggame.rest.dto.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Service
@AllArgsConstructor
public class GameRecordService {
    private final GameRecordsRepository gameRecordsRepository;
    private final UserRepository userRepository;

    public String generateCode() {
        var digits = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Collections.shuffle(digits);
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(digits.get(i));
        }
        return code.toString();
    }

    public GameStatus validateAndCheck(GameRecord gameRecord, String guess) {
        if (guess == null || guess.length() != 4) {
            throw new IllegalArgumentException("Guess must be 4 digits.");
        }
        if (!guess.matches("\\d{4}")) {
            throw new IllegalArgumentException("Guess must contain only digits.");
        }
        if (hasDuplicates(guess)) {
            throw new IllegalArgumentException("Guess must have 4 unique digits.");
        }

        GameStatus status = new GameStatus();
        status.setGameId(gameRecord.getId());
        status.setAttemptNumber(gameRecord.getTriesUsed() + 1);

        for (int i = 0; i < 4; i++) {
            char g = guess.charAt(i);
            char s = gameRecord.getCode().charAt(i);

            if (g == s) {
                status.setP(status.getP() + 1);
            } else if (gameRecord.getCode().contains(String.valueOf(g))) {
                status.setM(status.getM() + 1);
            }
        }

        return status;
    }

    private boolean hasDuplicates(String input) {
        Set<Character> seen = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!seen.add(c)) return true;
        }
        return false;
    }


    public GameStatus newGame(long userId) {
        User user = userRepository.getReferenceById(userId);
        GameRecord savedGameRecord;
        GameRecord newGameRecord = new GameRecord();
        GameRecord lastGameRecord = gameRecordsRepository.findFirstByUserOrderByIdDesc(user).orElse(new GameRecord());
        if (!lastGameRecord.isGameClosed()) {
            gameRecordsRepository.delete(lastGameRecord);
        }
        newGameRecord.setUser(user);
        newGameRecord.setCode(generateCode());
        savedGameRecord = gameRecordsRepository.save(newGameRecord);

        GameStatus gameStatus = new GameStatus();
        gameStatus.setGameId(savedGameRecord.getId());
        return gameStatus;
    }

    public GameRecord getLastUserGame(long userId) {
        User player = userRepository.findById(userId).orElseThrow();
        return gameRecordsRepository.findFirstByUserOrderByIdDesc(player).orElseThrow();
    }

    public GameRecord saveTheGame(GameRecord lastUserGameRecord, int attempt, boolean win, boolean gameOver) {
        lastUserGameRecord.setWin(win);
        lastUserGameRecord.setGameClosed(gameOver);
        lastUserGameRecord.setTriesUsed(attempt);
        return gameRecordsRepository.save(lastUserGameRecord);
    }
}
