package com.aulikcz.loop.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "reset_token")
@Getter
@Setter
@NoArgsConstructor
public class ResetTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUserEntity appUser;

    private String tokenHash;

    private OffsetDateTime expiresAt;

    @Column(insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime usedAt;
}
