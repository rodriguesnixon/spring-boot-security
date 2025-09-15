package io.javabrains.springbootsecurity.controller;

import io.javabrains.springbootsecurity.model.UserLocation;
import io.javabrains.springbootsecurity.service.UserLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserLocationController {

    @Autowired
    private UserLocationService userLocationService;

    @GetMapping("/user-locations")
    public ResponseEntity<List<UserLocation>> getUserLocations() {
        List<UserLocation> userLocations = userLocationService.getUserLocations();
        return ResponseEntity.ok(userLocations);
    }
}
