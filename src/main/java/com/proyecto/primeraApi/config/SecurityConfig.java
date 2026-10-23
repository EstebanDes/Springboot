
package com.proyecto.primeraApi.config;

import com.proyecto.primeraApi.repository.UsuarioRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


@Configuration
public class SecurityConfig {

    // 1. Proteger las contraseñas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Buscar usuarios en MySQL mediante su correo
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository repository) {
        return correo -> {var usuario = repository.buscarPorCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            return User.withUsername(usuario.getCorreo()).password(usuario.getPasswordHash()).roles(usuario.getRol()).disabled(!usuario.isActivo()).build();};
    }

    // 3. Permitir que AuthController autentique usuarios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {return configuration.getAuthenticationManager();}
    // 4. Validar la firma de los tokens JWT
    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String secreto) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);

        if (bytes.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes");}

        SecretKey clave = new SecretKeySpec(bytes, "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(clave).macAlgorithm(MacAlgorithm.HS256).build();
    }

    // 5. Convertir los roles del JWT en permisos de Spring
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();

        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(roles);

        return converter;
    }

    // 6. Proteger los endpoints
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtConverter) throws Exception {

        http.csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Inicio de sesión público
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // Consultar productos
                        .requestMatchers(HttpMethod.GET, "/productos", "/productos/precios", "/productos/{id}/precio").hasAnyRole("ADMIN", "USER")

                        // Crear productos
                        .requestMatchers(HttpMethod.POST, "/productos").hasRole("ADMIN")

                        // Consultar usuarios
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")

                        // Crear usuarios
                        .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMIN")

                        // Eliminar Producto
                        .requestMatchers(HttpMethod.DELETE, "/productos/{id}").hasRole("ADMIN")

                        // Actualizar Producto
                        .requestMatchers(HttpMethod.PUT, "/productos/{id}").hasRole("ADMIN")

                        // Proteger los demás endpoints
                        .anyRequest().authenticated()
                )

                // Validación JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

        return http.build();
    }
}
