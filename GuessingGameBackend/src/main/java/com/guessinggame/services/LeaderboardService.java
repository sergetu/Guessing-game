package com.guessinggame.services;

import com.guessinggame.db.entitys.GameRecord;
import com.guessinggame.db.entitys.Leaderboard;
import com.guessinggame.db.entitys.User;
import com.guessinggame.db.repositories.GameRecordsRepository;
import com.guessinggame.db.repositories.LeaderboardRepository;
import com.guessinggame.rest.dto.LeaderboardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class LeaderboardService {
    private final GameRecordsRepository gameRecordsRepository;
    private final LeaderboardRepository leaderboardRepository;

    public List<LeaderboardResponse> getLeaderboard(Long minTotalGames) {


        return leaderboardRepository
                .findByGamesPlayedGreaterThanEqualOrderByRankDescTotalTriesAsc(minTotalGames)
                .stream()
                .map(l -> new LeaderboardResponse(
                        l.getUser().getName(),
                        l.getGamesPlayed(),
                        l.getGamesWon(),
                        l.getTotalTries()
                ))
                .toList();
    }

    public Leaderboard recalculateUser(User user) {

        List<GameRecord> entries = gameRecordsRepository.findByUser(user);
        long played = entries.size();
        long won = entries.stream().filter(GameRecord::isWin).count();
        long tries = entries.stream().mapToInt(GameRecord::getTriesUsed).sum();

        Leaderboard userLeaderboard = leaderboardRepository.findByUser(user).orElse(new Leaderboard());

        userLeaderboard.setUser(user);
        userLeaderboard.setGamesPlayed(played);
        userLeaderboard.setGamesWon(won);
        userLeaderboard.setTotalTries(tries);
        userLeaderboard.setRank((double) won / played);

        return leaderboardRepository.save(userLeaderboard);
    }


}
