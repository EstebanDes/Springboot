
package com.proyecto.primeraApi.service;

import com.proyecto.primeraApi.dto.CrearUsuarioDTO;
import com.proyecto.primeraApi.model.Usuario;
import com.proyecto.primeraApi.repository.UsuarioRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository repository, PasswordEncoder passwordEncoder) {this.repository = repository;this.passwordEncoder = passwordEncoder;}

    @Override
    public List<Usuario> obtenerUsuarios() {return repository.obtenerTodos();}

    @Override
    public Usuario crearUsuario(CrearUsuarioDTO datos) {

        // Validar datos obligatorios
        if (datos.nombre() == null || datos.nombre().isBlank() || datos.correo() == null || datos.correo().isBlank() || datos.password() == null || datos.password().length() < 8) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre, correo y contraseña válida son obligatorios");}

        String nombre = datos.nombre().trim();

        String correo = datos.correo().trim().toLowerCase(Locale.ROOT);

        // Validar que el correo no esté registrado
        if (repository.buscarPorCorreo(correo).isPresent()) {throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");}
        // Validar el rol
        if (datos.rol() == null || (!datos.rol().equalsIgnoreCase("ADMIN") && !datos.rol().equalsIgnoreCase("USER"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol debe ser ADMIN o USER");}

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setRol(datos.rol().toUpperCase(Locale.ROOT));

        usuario.setActivo(true);

        // Generar hash BCrypt de la contraseña
        usuario.setPasswordHash(passwordEncoder.encode(datos.password()));

        // Guardar usuario en MySQL
        try {return repository.guardar(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "No se pudo registrar el usuario; verifica que el correo no esté duplicado", ex);}
    }
}
