package com.example.vet_pet.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class TokenService {
    @Value("${auth.jwt.secret}")
    private String secretKey;

    public String generateToken(Long userId) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        LocalTime now = LocalTime.now();
        LocalTime exp = now.plusMinutes(5);

        return JWT.create()
                .withIssuer("auth-service")
                .withAudience("vetPet")
                .withSubject(userId)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(algorithm);
    }
}
