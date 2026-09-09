package com.drivinglicense.service.impl;

import com.drivinglicense.dto.auth.LoginRequestDTO;
import com.drivinglicense.dto.auth.LoginResponseDTO;
import com.drivinglicense.entity.User;
import com.drivinglicense.exception.UnauthorizedException;
import com.drivinglicense.repository.UserRepository;
import com.drivinglicense.security.jwt.JwtService;
import com.drivinglicense.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
   public LoginResponseDTO login(LoginRequestDTO dto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("password or email are incorrect"));

        String token = jwtService.generateToken(user);
        String fullName = user.getPerson() != null ? user.getPerson().getFirstName() + " " + user.getPerson().getLastName(): null;

        return new LoginResponseDTO(token,user.getEmail(),user.getRole(),fullName);
    }

}