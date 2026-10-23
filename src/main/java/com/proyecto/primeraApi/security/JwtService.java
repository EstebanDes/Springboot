
package com.proyecto.primeraApi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(@Value("${jwt.secret}") String secreto) {

        if (secreto.getBytes(java.nio.charset.StandardCharsets.UTF_8).length < 32) {throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes");}

        SecretKey key = new SecretKeySpec(secreto.getBytes(java.nio.charset.StandardCharsets.UTF_8), "HmacSHA256");

        this.jwtEncoder = new NimbusJwtEncoder(new com.nimbusds.jose.jwk.source.ImmutableSecret<>(key));}

    public String generarToken(Authentication authentication) {

        Instant ahora = Instant.now();

        List<String> roles = authentication.getAuthorities().stream().map(a -> a.getAuthority()).toList();

        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("primeraApi").subject(authentication.getName()).issuedAt(ahora).expiresAt(ahora.plus(1, ChronoUnit.HOURS)).claim("roles", roles).build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();}
}
