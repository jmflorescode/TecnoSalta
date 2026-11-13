package com.tienda.repositorio;

import com.tienda.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorio {

    private final Almacen almacen = Almacen.getInstancia();

    public List<Producto> listar() {
        return new ArrayList<>(almacen.getProductos());
    }

    /** Guarda un producto nuevo (le asigna id) o los cambios de uno existente. */
    public void guardar(Producto producto) {
        if (producto.getId() == 0) {
            producto.setId(almacen.siguienteIdProducto());
            almacen.getProductos().add(producto);
        }
        almacen.guardar();
    }
}
