package com.proyecto.primeraApi.repository;

import com.proyecto.primeraApi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    default Optional<Usuario> buscarPorCorreo(String correo) {return findByCorreoIgnoreCase(correo);}

    default List<Usuario> obtenerTodos() {return findAll();}

    default Usuario guardar(Usuario usuario) {return save(usuario);}
}
