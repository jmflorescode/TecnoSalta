package com.tienda.servicio;

import com.tienda.modelo.Producto;

/** Se lanza cuando se pide más cantidad de un producto que el stock disponible. */
public class StockInsuficienteException extends Exception {

    public StockInsuficienteException(Producto producto, int cantidadPedida) {
        super("No hay stock suficiente de \"" + producto.getNombre() + "\": se pidieron "
                + cantidadPedida + " y quedan " + producto.getStock() + ".");
    }
}
