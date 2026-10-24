package com.aulikcz.loop.game;

import com.aulikcz.loop.user.AppUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "game_invite")
@Getter
@Setter
@NoArgsConstructor
public class GameInviteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by")
    private AppUserEntity createdBy;

    private String code;

    @Column(insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime expiresAt;

    private OffsetDateTime revokedAt;
}
