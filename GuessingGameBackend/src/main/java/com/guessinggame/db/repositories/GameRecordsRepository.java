package com.guessinggame.db.repositories;

import com.guessinggame.db.entitys.GameRecord;
import com.guessinggame.db.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRecordsRepository extends JpaRepository<GameRecord, Long> {
    List<GameRecord> findByUser(User user);

    Optional<GameRecord> findFirstByUserOrderByIdDesc(User user);
}
