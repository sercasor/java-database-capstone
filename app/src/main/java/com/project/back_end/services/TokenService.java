package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
//Spring will manage it as a bean within its application context
//This allows the class to be injected into other Spring-managed components (like services or controllers) where it's needed.
@Component
public class TokenService {
    /*-----------------------------PRIVATE ATTRIBUTES-----------------------------*/
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Value("${jwt.secret}")
    private String jwtSecret; //injected via application.properties
    // 7 días en milisegundos: 1000ms * 60s * 60min * 24h * 7días
    private static final long EXPIRATION_MILLIS = 1000L * 60 * 60 * 24 * 7;


    /*-----------------------------PUBLIC METHODS-----------------------------*/
    /**
     * Builds the HMAC-SHA signing key from the secret defined in application.properties.
     * The same key is used both to sign new tokens and to verify existing ones.
     */
    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a signed JWT for the given user identifier (username for Admin, email for Doctor/Patient).
     * The token is valid for 7 days from the moment it's issued.
     */
    public String generateToken(String identifier) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MILLIS);

        return Jwts.builder()
                .subject(identifier)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Verifies the token's signature and extracts its subject (the identifier used at creation time).
     * Throws a JwtException (or subclass) if the token is malformed, expired, or its signature doesn't match.
     */
    public String extractIdentifier(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates that the token is genuine AND that the identifier it carries
     * actually belongs to a user of the specified role.
     *
     * @param token the JWT to validate
     * @param user  "admin", "doctor" or "patient"
     * @return true if the token is valid for that role, false otherwise (invalid, expired, or role mismatch)
     */
    public boolean validateToken(String token, String user) {
        try {
            String identifier = extractIdentifier(token); // lanza excepción si el token es inválido/expirado

            switch (user) {
                case "admin":
                    return adminRepository.findByUsername(identifier) != null;
                case "doctor":
                    return doctorRepository.findByEmail(identifier) != null;
                case "patient":
                    return patientRepository.findByEmail(identifier) != null;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
