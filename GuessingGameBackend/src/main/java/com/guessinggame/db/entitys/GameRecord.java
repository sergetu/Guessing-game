package com.guessinggame.db.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_records")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private String code;

    @Column(name = "is_win")
    private boolean win;

    @Column(name = "tries_used")
    private int triesUsed;

    @Column(name = "is_game_closed")
    private boolean gameClosed = false;

}
