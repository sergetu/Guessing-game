package com.guessinggame.rest;

import com.guessinggame.rest.dto.LeaderboardResponse;
import com.guessinggame.services.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderboardRestControllerTest {
    @Mock
    private LeaderboardService leaderboardService;
    @InjectMocks
    private LeaderboardRestController controller;

    @Test
    void testGetLeaderboard_returnsCorrectData() {
        LeaderboardResponse player1 = new LeaderboardResponse("Alice", 10, 6, 40);
        LeaderboardResponse player2 = new LeaderboardResponse("Bob", 12, 8, 36);
        List<LeaderboardResponse> mockList = List.of(player1, player2);

        when(leaderboardService.getLeaderboard(5L)).thenReturn(mockList);

        List<LeaderboardResponse> result = controller.getLeaderboard(5L);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals(10, result.get(0).getGamesPlayed());
        assertEquals("Bob", result.get(1).getName());

        verify(leaderboardService, times(1)).getLeaderboard(5L);
    }
}
