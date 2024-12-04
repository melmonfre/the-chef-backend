package com.api.the_chef_backend.service;

import com.api.the_chef_backend.exception.ConflictException;
import com.api.the_chef_backend.exception.InvalidCpfOrCnpjException;
import com.api.the_chef_backend.exception.UnauthorizedException;
import com.api.the_chef_backend.model.dtos.auth.AuthResponseDTO;
import com.api.the_chef_backend.model.dtos.auth.LoginRestaurantDTO;
import com.api.the_chef_backend.model.dtos.auth.RegisterRestaurantDTO;
import com.api.the_chef_backend.model.entity.Restaurant;
import com.api.the_chef_backend.model.repository.RestaurantRepository;
import com.api.the_chef_backend.security.JwtTokenProvider;
import com.api.the_chef_backend.util.CpfCnpjValidatorUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    // Método para registrar um novo restaurante
    public AuthResponseDTO registerRestaurant(RegisterRestaurantDTO dto) {
        // Verifica se o email já está registrado
        if (restaurantRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Email já registrado.");
        }

        // Verifica se o CPF ou CNPJ é válido
        if (!CpfCnpjValidatorUtil.isValidCpfOrCnpj(dto.cpfOrCnpj())) {
            throw new InvalidCpfOrCnpjException("CPF ou CNPJ inválido.");
        }

        // Cria um novo restaurante
        Restaurant restaurant = Restaurant.builder()
                .name(dto.name())
                .email(dto.email())
                .cpfCnpj(dto.cpfOrCnpj())
                .phone(dto.phone())
                .tableQuantity(dto.tableQuantity())
                .waiterCommission(dto.waiterCommission())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        // Salva o restaurante no banco de dados
        restaurantRepository.save(restaurant);

        // Retorna o DTO de resposta com as informações do restaurante
        return new AuthResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getEmail(),
                null // Não precisa de token aqui, pois é apenas o registro
        );
    }
    public List<AuthResponseDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(restaurant -> new AuthResponseDTO(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getEmail(), null
                ))
                .collect(Collectors.toList());
    }
    // Método para login de um restaurante
    public AuthResponseDTO loginRestaurant(LoginRestaurantDTO dto) {
        // Verifica se o restaurante existe no banco
        Restaurant restaurant = restaurantRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Email ou senha inválidos"));

        // Verifica se a senha está correta
        if (!passwordEncoder.matches(dto.getPassword(), restaurant.getPassword())) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }

        // Realiza a autenticação
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        );

        authenticationManager.authenticate(authentication);

        // Gera o token JWT
        String token = jwtTokenProvider.generateToken(authentication);

        // Retorna a resposta com o token JWT
        return new AuthResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getEmail(),
                token // Inclui o token na resposta
        );
    }
    
}
