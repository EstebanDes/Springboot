
package com.proyecto.primeraApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 150)
    private String correo;

    @Column(nullable = false, length = 20)
    private String rol = "USER";

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean activo = true;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String correo, String rol) {this.id = id;this.nombre = nombre;this.correo = correo;this.rol = rol;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCorreo() {return correo;}

    public void setCorreo(String correo) {this.correo = correo;}

    public String getRol() {return rol;}

    public void setRol(String rol) {this.rol = rol;}

    public String getPasswordHash() {return passwordHash;}

    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}

    public boolean isActivo() {return activo;}

    public void setActivo(boolean activo) {this.activo = activo;}
}
