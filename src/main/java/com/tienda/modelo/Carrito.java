package com.tienda.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Carrito de compras. Vive en memoria mientras dura la sesión: no se guarda en el archivo. */
public class Carrito {

    private final List<ItemCarrito> items = new ArrayList<>();

    /** Agrega un producto. Si ya estaba en el carrito, suma la cantidad. */
    public void agregar(Producto producto, int cantidad) {
        ItemCarrito existente = buscar(producto);
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
        } else {
            items.add(new ItemCarrito(producto, cantidad));
        }
    }

    public void quitar(Producto producto) {
        ItemCarrito existente = buscar(producto);
        if (existente != null) {
            items.remove(existente);
        }
    }

    /** Cambia la cantidad de un producto. Con cantidad 0 o menor, lo quita. */
    public void cambiarCantidad(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            quitar(producto);
            return;
        }
        ItemCarrito existente = buscar(producto);
        if (existente != null) {
            existente.setCantidad(cantidad);
        }
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrito item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /** Cantidad total de unidades (para el contador de la barra superior). */
    public int getCantidadTotal() {
        int cantidad = 0;
        for (ItemCarrito item : items) {
            cantidad += item.getCantidad();
        }
        return cantidad;
    }

    public void vaciar() {
        items.clear();
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    private ItemCarrito buscar(Producto producto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == producto.getId()) {
                return item;
            }
        }
        return null;
    }
}
