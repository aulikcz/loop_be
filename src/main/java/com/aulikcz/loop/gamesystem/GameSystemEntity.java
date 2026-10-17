package com.aulikcz.loop.gamesystem;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "game_system")
@Getter
@Setter
@NoArgsConstructor
public class GameSystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String code;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> sheetTemplate;

    private Integer templateVersion;
}