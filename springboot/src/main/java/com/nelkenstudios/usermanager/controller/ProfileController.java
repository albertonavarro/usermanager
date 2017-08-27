package com.nelkenstudios.usermanager.controller;

import com.nelkenstudios.usermanager.domain.DetailedProfile;
import com.nelkenstudios.usermanager.domain.MinimalProfile;
import com.nelkenstudios.usermanager.service.JwtService;
import com.nelkenstudios.usermanager.service.LoginService;
import com.nelkenstudios.usermanager.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path = "/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final LoginService loginService;
    private final JwtService jwtService;

    @Autowired
    public ProfileController(ProfileService profileService, LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
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
    public String details(@PathVariable String username, Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", username);
        return "welcome";

    }


    @RequestMapping(path = "/token",
            method = POST,
            produces = APPLICATION_JSON_VALUE)
    public MinimalProfile login(@RequestBody LoginCredentials credentials,
                                HttpServletResponse response) {
        return loginService.login(credentials)
                .map(minimalProfile -> {
                    try {
                        response.setHeader("Token", jwtService.tokenFor(minimalProfile));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return minimalProfile;
                })
                .orElseThrow(() -> new FailedToLoginException(credentials.getUsername()));
    }
}
