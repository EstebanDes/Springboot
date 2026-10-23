package com.proyecto.primeraApi.dto;
public record PrecioProductoDTO(Long id, String nombre, double precioOriginal, double porcentajeDescuento, double montoDescuento, double precioFinal) { }
