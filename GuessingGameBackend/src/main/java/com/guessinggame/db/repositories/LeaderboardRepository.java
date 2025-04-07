package com.guessinggame.db.repositories;

import com.guessinggame.db.entitys.Leaderboard;
import com.guessinggame.db.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    Optional<Leaderboard> findByUser(User user);

    List<Leaderboard> findByGamesPlayedGreaterThanEqualOrderByRankDescTotalTriesAsc(Long minGames);

}

