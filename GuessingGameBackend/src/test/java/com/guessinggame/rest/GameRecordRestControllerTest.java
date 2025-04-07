package com.guessinggame.rest;

import com.guessinggame.db.entitys.GameRecord;
import com.guessinggame.db.entitys.User;
import com.guessinggame.rest.dto.GameStatus;
import com.guessinggame.rest.dto.GuessRequest;
import com.guessinggame.rest.dto.GuessResponse;
import com.guessinggame.services.GameRecordService;
import com.guessinggame.services.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameRecordRestControllerTest {
    @Mock
    private GameRecordService gameRecordService;
    @Mock
    private LeaderboardService leaderboardService;
    @InjectMocks
    private GameRestController controller;

    @Test
    void testMakeGuess_whenGameClosed_returnsGameOver() {
        GuessRequest request = new GuessRequest(1L, "1234");

        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setGameClosed(true);

        when(gameRecordService.getLastUserGame(1L)).thenReturn(gameRecord);

        GuessResponse response = controller.makeGuess(request);

        assertTrue(response.isGameOver());
        assertFalse(response.isWin());
        assertNull(response.getStatus());
    }

    @Test
    void testMakeGuess_whenWin_returnsWinAndGameOver() {
        GuessRequest request = new GuessRequest(1L, "1234");

        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setGameClosed(false);
        gameRecord.setTriesUsed(3);
        User mockUser = new User();
        gameRecord.setUser(mockUser);

        GameStatus status = new GameStatus();
        status.setGameId(1L);
        status.setAttemptNumber(4);
        status.setM(0);
        status.setP(4);

        when(gameRecordService.getLastUserGame(1L)).thenReturn(gameRecord);
        when(gameRecordService.validateAndCheck(gameRecord, "1234")).thenReturn(status);

        GuessResponse response = controller.makeGuess(request);

        assertTrue(response.isGameOver());
        assertTrue(response.isWin());
        assertEquals(status, response.getStatus());
        verify(gameRecordService).saveTheGame(gameRecord, 4, true, true);
        verify(leaderboardService).recalculateUser(mockUser);
    }

    @Test
    void testNewGame_returnsValidStatus() {
        GameStatus status = new GameStatus();
        status.setGameId(99L);
        status.setAttemptNumber(0);
        status.setM(0);
        status.setP(0);

        when(gameRecordService.newGame(1)).thenReturn(status);

        GuessResponse response = controller.newGame(1);

        assertNotNull(response.getStatus());
        assertFalse(response.isWin());
        assertFalse(response.isGameOver());
        assertEquals(99L, response.getStatus().getGameId());
    }

    @Test
    void testMakeGuess_whenIncorrectGuess_returnsStatusButNotGameOver() {
        GuessRequest request = new GuessRequest();
        request.setUserId(1L);
        request.setGuess("5678");

        GameRecord gameRecord = new GameRecord();
        gameRecord.setId(1L);
        gameRecord.setGameClosed(false);
        gameRecord.setTriesUsed(2);

        User mockUser = new User();
        gameRecord.setUser(mockUser);

        GameStatus status = new GameStatus();
        status.setGameId(1L);
        status.setAttemptNumber(3);
        status.setM(1);
        status.setP(2);

        when(gameRecordService.getLastUserGame(1L)).thenReturn(gameRecord);
        when(gameRecordService.validateAndCheck(gameRecord, "5678")).thenReturn(status);

        GuessResponse response = controller.makeGuess(request);

        assertFalse(response.isGameOver());
        assertFalse(response.isWin());
        assertEquals(1, response.getStatus().getM());
        assertEquals(2, response.getStatus().getP());

        verify(gameRecordService).saveTheGame(gameRecord, 3, false, false);
        verify(leaderboardService, never()).recalculateUser(any());
    }

    @Test
    void testGetCode_whenGameClosed_returnsCode() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setGameClosed(true);
        gameRecord.setCode("1234");

        when(gameRecordService.getLastUserGame(1L)).thenReturn(gameRecord);

        String result = controller.getCode(1);

        assertEquals("1234", result);
    }

    @Test
    void testGetCode_whenGameNotClosed_returnsPlaceholder() {
        GameRecord gameRecord = new GameRecord();
        gameRecord.setGameClosed(false);
        gameRecord.setCode("1234");

        when(gameRecordService.getLastUserGame(1L)).thenReturn(gameRecord);

        String result = controller.getCode(1);

        assertEquals("Game not ended", result);
    }

}
