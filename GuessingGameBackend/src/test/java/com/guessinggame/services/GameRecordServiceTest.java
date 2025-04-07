package com.guessinggame.services;

import com.guessinggame.db.entitys.GameRecord;
import com.guessinggame.rest.dto.GameStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GameRecordServiceTest {

    @Autowired
    private GameRecordService gameRecordService;

    @Test
    void validateAndCheck_correctlyCalculatesMP() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setCode("1234");
        String guess = "2134";

        GameStatus status = gameRecordService.validateAndCheck(gameRecord, guess);

        assertEquals(2, status.getM(), "Expected 2 misplaced digits");
        assertEquals(2, status.getP(), "Expected 2 perfect matches");
    }

    @Test
    void validateAndCheck_allIncorrect() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setCode("1234");
        String guess = "5678";

        GameStatus status = gameRecordService.validateAndCheck(gameRecord, guess);

        assertEquals(0, status.getM());
        assertEquals(0, status.getP());
    }

    @Test
    void validateAndCheck_allCorrect() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setCode("1234");
        String guess = "1234";

        GameStatus status = gameRecordService.validateAndCheck(gameRecord, guess);

        assertEquals(0, status.getM());
        assertEquals(4, status.getP());
    }

    @Test
    void validateAndCheck_onlyMisplaced() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setCode("1234");
        String guess = "4321";

        GameStatus status = gameRecordService.validateAndCheck(gameRecord, guess);

        assertEquals(4, status.getM());
        assertEquals(0, status.getP());
    }

    @Test
    void testGuessNullOrInvalidLength_throwsException() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setCode("1234");

        assertThrows(IllegalArgumentException.class,
                () -> gameRecordService.validateAndCheck(gameRecord, null),
                "Guess must be 4 digits.");

        assertThrows(IllegalArgumentException.class,
                () -> gameRecordService.validateAndCheck(gameRecord, "12"),
                "Guess must be 4 digits.");

        assertThrows(IllegalArgumentException.class,
                () -> gameRecordService.validateAndCheck(gameRecord, "12345"),
                "Guess must be 4 digits.");
    }

    @Test
    void testGuessWithNonDigits_throwsException() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setCode("1234");

        assertThrows(IllegalArgumentException.class,
                () -> gameRecordService.validateAndCheck(gameRecord, "12a4"),
                "Guess must contain only digits.");

        assertThrows(IllegalArgumentException.class,
                () -> gameRecordService.validateAndCheck(gameRecord, "1#34"),
                "Guess must contain only digits.");
    }

    @Test
    void testGuessWithDuplicates_throwsException() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setCode("1234");

        assertThrows(IllegalArgumentException.class,
                () -> gameRecordService.validateAndCheck(gameRecord, "1123"),
                "Guess must have 4 unique digits.");

        assertThrows(IllegalArgumentException.class,
                () -> gameRecordService.validateAndCheck(gameRecord, "3333"),
                "Guess must have 4 unique digits.");
    }
}
