package com.tienda.servicio;

import com.tienda.modelo.Carrito;
import com.tienda.modelo.ItemCarrito;
import com.tienda.modelo.Producto;

/** Operaciones sobre el carrito de la sesión. Siempre valida el stock antes de cambiar algo. */
public class CarritoService {

    public Carrito getCarrito() {
        return Sesion.getInstancia().getCarrito();
    }

    /** Suma unidades de un producto. Cuenta también las que ya estaban en el carrito. */
    public void agregar(Producto producto, int cantidad) throws StockInsuficienteException {
        int cantidadFinal = cantidadEnCarrito(producto) + cantidad;
        if (!producto.hayStock(cantidadFinal)) {
            throw new StockInsuficienteException(producto, cantidadFinal);
        }
        getCarrito().agregar(producto, cantidad);
    }

    public void cambiarCantidad(Producto producto, int cantidad) throws StockInsuficienteException {
        if (!producto.hayStock(cantidad)) {
            throw new StockInsuficienteException(producto, cantidad);
        }
        getCarrito().cambiarCantidad(producto, cantidad);
    }

    public void quitar(Producto producto) {
        getCarrito().quitar(producto);
    }

    private int cantidadEnCarrito(Producto producto) {
        for (ItemCarrito item : getCarrito().getItems()) {
            if (item.getProducto().getId() == producto.getId()) {
                return item.getCantidad();
            }
        }
        return 0;
    }
}
