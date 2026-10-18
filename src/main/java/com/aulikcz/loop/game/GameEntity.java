package com.aulikcz.loop.game;

import com.aulikcz.loop.gamesystem.GameSystemEntity;
import com.aulikcz.loop.user.AppUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_system_id")
    private GameSystemEntity gameSystem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by")
    private AppUserEntity createdBy;

    @Column(insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}
