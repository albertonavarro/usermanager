package com.nelkenstudios.usermanager.service;

import com.nelkenstudios.usermanager.controller.LoginCredentials;
import com.nelkenstudios.usermanager.domain.MinimalProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginService {

    private ProfileService profileService;

    @SuppressWarnings("unused")
    public LoginService() {
        this(null);
    }

    @Autowired
    public LoginService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public Optional<MinimalProfile> login(LoginCredentials credentials) {
        return profileService.get(credentials.getUsername())
                .filter(profile -> profile.getLogin().getPassword().equals(credentials.getPassword()))
                .map(profile -> new MinimalProfile(profile));
    }
}