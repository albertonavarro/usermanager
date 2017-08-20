package com.nelkenstudios.usermanager.controller;

import com.nelkenstudios.usermanager.domain.DetailedProfile;
import com.nelkenstudios.usermanager.domain.MinimalProfile;
import com.nelkenstudios.usermanager.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

    private final ProfileService profileService;

    @SuppressWarnings("unused")
    public ProfileController() {
        this(null);
    }

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping(path = "/{username}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public MinimalProfile minimal(@PathVariable String username) {
        return profileService.minimal(username)
                .orElseThrow(() -> new ProfileNotFoundException(username));
    }

    @RequestMapping(path = "/details/{username}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public DetailedProfile details(@PathVariable String username) {
        return profileService.detailed(username)
                .orElseThrow(() -> new ProfileNotFoundException(username));
    }
}
