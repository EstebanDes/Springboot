
    package com.proyecto.primeraApi.service;

    import com.proyecto.primeraApi.model.Producto;
    import com.proyecto.primeraApi.repository.ProductoRepository;
    import com.proyecto.primeraApi.strategy.EstrategiaDescuento;
    import com.proyecto.primeraApi.strategy.SinDescuento;
    import com.proyecto.primeraApi.strategy.DescuentoOferta;
    import com.proyecto.primeraApi.dto.PrecioProductoDTO;

    import org.springframework.stereotype.Service;
    import org.springframework.http.HttpStatus;
    import org.springframework.web.server.ResponseStatusException;

    import java.util.List;

    @Service
    public class ProductoServiceImpl implements ProductoService {
        private final ProductoRepository repository;
        public ProductoServiceImpl(ProductoRepository repository) {this.repository = repository;}

        // GET - Obtener todos los productos
        @Override public List<Producto> obtenerProductos() {return repository.obtenerTodos();}

        // POST - Crear producto
        @Override public Producto crearProducto(Producto producto) {return repository.guardar(producto);}

        // GET - Calcular precio de un producto por ID
        @Override public PrecioProductoDTO calcularPrecio(Long id, String tipoDescuento) {Producto producto = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
            EstrategiaDescuento estrategia;
            double porcentajeDescuento;

            if ("OFERTA".equalsIgnoreCase(tipoDescuento)) {estrategia = new DescuentoOferta();porcentajeDescuento = 10;
            } else if ("NORMAL".equalsIgnoreCase(tipoDescuento)) {estrategia = new SinDescuento();porcentajeDescuento = 0;
            } else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descuento no válido");}
            double precioOriginal = producto.getPrecio();
            double precioFinal = estrategia.aplicarDescuento(precioOriginal);
            double montoDescuento = precioOriginal - precioFinal;
            return new PrecioProductoDTO(producto.getId(), producto.getNombre(), precioOriginal, porcentajeDescuento, montoDescuento, precioFinal);}

        // GET - Obtener todos los productos con descuento
        @Override
        public List<PrecioProductoDTO> obtenerProductosConDescuento(String tipoDescuento) {
            return repository.obtenerTodos().stream().map(producto -> calcularPrecio(producto.getId(), tipoDescuento)).toList();}

        // PUT - Actualizar un producto existente
        @Override
        public Producto actualizarProducto(Long id, Producto datos) {Producto producto = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));producto.setPrecio(datos.getPrecio());return repository.save(producto);
        }

        // DELETE - Eliminar un producto existente
        @Override
        public void eliminarProducto(Long id) {

            Producto producto = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
            repository.delete(producto);}
    }
