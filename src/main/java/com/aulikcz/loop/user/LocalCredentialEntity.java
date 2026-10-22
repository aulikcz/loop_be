package com.aulikcz.loop.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "local_credential")
@Getter
@Setter
@NoArgsConstructor
public class LocalCredentialEntity {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private AppUserEntity user;

    private String passwordHash; //protože DB je to VARCHAR(256)?

    @Column(insertable = false)
    private OffsetDateTime updatedAt;
}
