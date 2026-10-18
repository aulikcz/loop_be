package com.aulikcz.loop.game;

import com.aulikcz.loop.common.InputValueMissingException;
import com.aulikcz.loop.gamesystem.GameSystemEntity;
import com.aulikcz.loop.gamesystem.GameSystemRepository;
import com.aulikcz.loop.user.AppUserEntity;
import com.aulikcz.loop.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final MembershipRepository membershipRepository;
    private final GameSystemRepository gameSystemRepository;
    private final AppUserRepository appUserRepository;
    private final PlayerRoleRepository playerRoleRepository;

    public GameService(GameRepository gameRepository, MembershipRepository membershipRepository, GameSystemRepository gameSystemRepository, AppUserRepository appUserRepository, PlayerRoleRepository playerRoleRepository){
        this.gameRepository = gameRepository;
        this.membershipRepository = membershipRepository;
        this.gameSystemRepository = gameSystemRepository;
        this.appUserRepository = appUserRepository;
        this.playerRoleRepository = playerRoleRepository;
    }

    @Transactional
    public GameDto create(CreateGameRequest req){
        if(req.name() == null ||req.name().isBlank()){
            throw new InputValueMissingException("Name must not be empty");
        }
        GameSystemEntity gameSystemEntity = gameSystemRepository.findByCode(req.gameSystemCode()).orElseThrow();
        AppUserEntity user = getCurrentUser();
        PlayerRoleEntity playerRole = playerRoleRepository.findByRole(PlayerRoleType.NARRATOR).orElseThrow();

        GameEntity game = new GameEntity();
        game.setName(req.name());
        game.setCreatedBy(user);
        game.setGameSystem(gameSystemEntity);
        gameRepository.save(game);

        MembershipEntity membership = new MembershipEntity();
        membership.setPlayerRole(playerRole);
        membership.setAppUser(user);
        membership.setGame(game);
        membershipRepository.save(membership);
        return GameDto.from(game);
    }

    @Transactional(readOnly = true)
    public List<GameListItemDto> getUserGames(){
        List<MembershipEntity> memberships = membershipRepository.findByAppUser(getCurrentUser());
        return memberships.stream().map(m -> GameListItemDto.from(m.getGame(), m)).toList();
    }

    private AppUserEntity getCurrentUser(){
        return appUserRepository.findByAuthProviderAndProviderUserId("local", "test").orElseThrow();
    }
}
