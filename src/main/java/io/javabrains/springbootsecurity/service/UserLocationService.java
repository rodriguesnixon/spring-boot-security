package io.javabrains.springbootsecurity.service;

import io.javabrains.springbootsecurity.model.User;
import io.javabrains.springbootsecurity.model.UserLocation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLocationService {

    private final RestTemplate restTemplate;
    private final String JSON_PLACEHOLDER_URL = "https://jsonplaceholder.typicode.com/users";

    public UserLocationService() {
        this.restTemplate = new RestTemplate();
    }

    public List<UserLocation> getUserLocations() {
        try {
            User[] users = restTemplate.getForObject(JSON_PLACEHOLDER_URL, User[].class);
            
            if (users == null) {
                return List.of();
            }

            return Arrays.stream(users)
                    .filter(user -> user.getAddress() != null && 
                                  user.getAddress().getGeo() != null &&
                                  user.getAddress().getGeo().getLat() != null &&
                                  user.getAddress().getGeo().getLng() != null)
                    .map(user -> {
                        try {
                            double lat = Double.parseDouble(user.getAddress().getGeo().getLat());
                            double lng = Double.parseDouble(user.getAddress().getGeo().getLng());
                            return new UserLocation(user.getId(), lat, lng);
                        } catch (NumberFormatException e) {
                            // Skip users with invalid lat/lng values
                            return null;
                        }
                    })
                    .filter(userLocation -> userLocation != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the error and return empty list
            System.err.println("Error fetching user data: " + e.getMessage());
            return List.of();
        }
    }
}
