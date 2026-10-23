package com.proyecto.primeraApi.service;

import com.proyecto.primeraApi.dto.CrearUsuarioDTO;
import com.proyecto.primeraApi.model.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> obtenerUsuarios();
    Usuario crearUsuario(CrearUsuarioDTO datos);

}
