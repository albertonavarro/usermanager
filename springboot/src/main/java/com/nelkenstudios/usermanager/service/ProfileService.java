package com.nelkenstudios.usermanager.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelkenstudios.usermanager.domain.DetailedProfile;
import com.nelkenstudios.usermanager.domain.MinimalProfile;
import com.nelkenstudios.usermanager.domain.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Component
public class ProfileService {
    private final List<Profile> profiles;



    private final InputStream PROFILES_FILE = this.getClass().getResourceAsStream("/profiles.json");

    public ProfileService() throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper().configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        profiles = objectMapper.readValue(PROFILES_FILE, new TypeReference<List<Profile>>() {
        });
    }

    protected Optional<Profile> get(String username) {
        return profiles.stream()
                .filter(profile -> profile.getLogin().getUsername().equals(username))
                .findFirst();
    }

    public Optional<MinimalProfile> minimal(String username) {
        return get(username).map(profile -> new MinimalProfile(profile));
    }

    public Optional<DetailedProfile> detailed(String username) {
        return get(username).map(profile -> new DetailedProfile(profile));
    }
}