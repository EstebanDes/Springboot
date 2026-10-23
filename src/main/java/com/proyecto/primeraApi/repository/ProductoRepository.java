
package com.proyecto.primeraApi.repository;

import com.proyecto.primeraApi.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository
        extends JpaRepository<Producto, Long> {

    // GET - Obtener todos los productos
    default List<Producto> obtenerTodos() {return findAll();
    }
    // POST - Guardar producto
    default Producto guardar(Producto producto) {return save(producto);
    }

}
