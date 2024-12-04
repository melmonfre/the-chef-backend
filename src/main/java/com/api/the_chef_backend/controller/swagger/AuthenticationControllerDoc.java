package com.api.the_chef_backend.controller.swagger;

import com.api.the_chef_backend.model.dtos.auth.AuthResponseDTO;
import com.api.the_chef_backend.model.dtos.auth.LoginRestaurantDTO;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "authentication controller")
public interface AuthenticationControllerDoc {

    @Operation(summary = "create a new restaurant")
    @ApiResponse(responseCode = "201", description = "restaurant created successfully")
    ResponseEntity<AuthResponseDTO> registerRestaurant(@RequestBody RegisterRestaurantDTO dto);

    @Operation(summary = "login restaurant to get authentication token")
    @ApiResponse(responseCode = "200", description = "login successful")
    @ApiResponse(responseCode = "401", description = "invalid email or password")
    ResponseEntity<AuthResponseDTO> loginRestaurant(@RequestBody LoginRestaurantDTO dto);

    @Operation(summary = "get all registered restaurants")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "list of all restaurants"),
        @ApiResponse(responseCode = "404", description = "no restaurants found")
    })
    ResponseEntity<List<AuthResponseDTO>> getAllRestaurants();
}
