package com.guessinggame.db.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "leaderboard")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Long gamesPlayed;

    private Long gamesWon;

    private Long totalTries;

    private Double rank;

}
