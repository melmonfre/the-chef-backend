package com.api.the_chef_backend.controller;

import com.api.the_chef_backend.controller.swagger.AuthenticationControllerDoc;
import com.api.the_chef_backend.model.dtos.auth.AuthResponseDTO;
import com.api.the_chef_backend.model.dtos.auth.LoginRestaurantDTO;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController implements AuthenticationControllerDoc {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerRestaurant(@Valid @RequestBody RegisterRestaurantDTO dto) {
        AuthResponseDTO response = authService.registerRestaurant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginRestaurant(@Valid @RequestBody LoginRestaurantDTO dto) {
        AuthResponseDTO response = authService.loginRestaurant(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<AuthResponseDTO>> getAllRestaurants() {
        List<AuthResponseDTO> restaurants = authService.getAllRestaurants();
        return ResponseEntity.status(HttpStatus.OK).body(restaurants);
    }
}
