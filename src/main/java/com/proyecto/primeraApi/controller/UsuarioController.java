
package com.proyecto.primeraApi.controller;

import com.proyecto.primeraApi.dto.CrearUsuarioDTO;
import com.proyecto.primeraApi.model.Usuario;
import com.proyecto.primeraApi.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private  UsuarioService usuarioService;


    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    // Registrar un nuevo usuario
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crearUsuario(@RequestBody CrearUsuarioDTO datos) {
        return usuarioService.crearUsuario(datos);}
}
