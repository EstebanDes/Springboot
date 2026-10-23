
package com.proyecto.primeraApi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarPassword {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = System.getenv("ADMIN_INITIAL_PASSWORD");

        if (password == null || password.isBlank()) {throw new IllegalStateException("Falta configurar ADMIN_INITIAL_PASSWORD");}

        String hash = encoder.encode(password);

        System.out.println("HASH BCrypt: " + hash);
    }
}
