package com.aulikcz.loop.character;


import com.aulikcz.loop.gamesystem.GameSystemEntity;
import com.aulikcz.loop.user.AppUserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "game_character")
@Getter
@Setter
@NoArgsConstructor
public class GameCharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUserEntity appUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_system_id")
    private GameSystemEntity gameSystem;

    @Column(name = "membership_id")
    private Long membershipId;

    private Integer templateVersion;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> sheet;

    @Column(insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
