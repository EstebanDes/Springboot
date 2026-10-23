
package com.proyecto.primeraApi.service;

import com.proyecto.primeraApi.model.Producto;
import com.proyecto.primeraApi.dto.PrecioProductoDTO;

import java.util.List;

public interface ProductoService {

    // GET - Obtener productos
    List<Producto> obtenerProductos();
    // POST - Crear producto
    Producto crearProducto(Producto producto);
    // GET - Calcular precio por ID
    PrecioProductoDTO calcularPrecio(Long id, String tipoDescuento);
    // GET - Obtener precios con descuento
    List<PrecioProductoDTO> obtenerProductosConDescuento(String tipoDescuento);
    // PUT - Actualizar producto
    Producto actualizarProducto(Long id, Producto datos);
    // DELETE - Eliminar producto
    void eliminarProducto(Long id);
}
