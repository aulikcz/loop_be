package com.aulikcz.loop.auth;

import com.aulikcz.loop.common.BadLogin;
import com.aulikcz.loop.user.AppUserEntity;
import com.aulikcz.loop.user.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
    private final AppUserRepository appUserRepository;

    public CurrentUser(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    public AppUserEntity get(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null){
            throw new BadLogin("User not found!");
        }
        Long id = Long.parseLong(auth.getName());
        return appUserRepository.findById(id).orElseThrow(() -> new BadLogin("User not found!"));
    }
}
