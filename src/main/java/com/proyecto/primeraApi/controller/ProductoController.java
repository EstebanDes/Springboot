
package com.proyecto.primeraApi.controller;

import com.proyecto.primeraApi.model.Producto;
import com.proyecto.primeraApi.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.primeraApi.dto.PrecioProductoDTO;
import java.util.List;


@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;


    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) { return ResponseEntity.ok(productoService.crearProducto(producto));
                //productoService.crearProducto(producto);
    }

    @GetMapping("/{id}/precio")
    public ResponseEntity<PrecioProductoDTO> calcularPrecio(@PathVariable Long id,@RequestParam String descuento) {return ResponseEntity.ok(productoService.calcularPrecio(id, descuento));}

    @GetMapping("/precios")
    public List<PrecioProductoDTO> obtenerPrecios(@RequestParam(defaultValue = "OFERTA") String descuento) {
        return productoService.obtenerProductosConDescuento(descuento);}

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto){
        return ResponseEntity.ok(productoService.actualizarProducto(id,producto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorducto(@PathVariable Long id){productoService.eliminarProducto(id);
    return ResponseEntity.noContent().build();
    }

}
