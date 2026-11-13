package com.tienda.servicio;

import com.tienda.modelo.Carrito;
import com.tienda.modelo.DetallePedido;
import com.tienda.modelo.EstadoPedido;
import com.tienda.modelo.ItemCarrito;
import com.tienda.modelo.Pedido;
import com.tienda.modelo.Producto;
import com.tienda.modelo.Usuario;
import com.tienda.repositorio.PedidoRepositorio;
import java.util.Collections;
import java.util.List;

/** La compra y el seguimiento de los pedidos. */
public class PedidoService {

    private final PedidoRepositorio pedidos = new PedidoRepositorio();

    /**
     * Convierte el carrito de la sesión en un pedido. Es "todo o nada":
     * primero se verifica el stock de todos los productos y recién después se modifica algo.
     */
    public Pedido confirmarCompra() throws StockInsuficienteException {
        Sesion sesion = Sesion.getInstancia();
        Carrito carrito = sesion.getCarrito();

        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío.");
        }

        // 1) Verificar todo antes de tocar nada
        for (ItemCarrito item : carrito.getItems()) {
            if (!item.getProducto().hayStock(item.getCantidad())) {
                throw new StockInsuficienteException(item.getProducto(), item.getCantidad());
            }
        }

        // 2) Crear el pedido, congelando el precio de cada producto, y descontar el stock
        Pedido pedido = new Pedido(sesion.getUsuario());
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            pedido.agregarDetalle(new DetallePedido(producto, item.getCantidad(), producto.getPrecio()));
            producto.descontarStock(item.getCantidad());
        }

        // 3) Guardar una sola vez y vaciar el carrito
        pedidos.guardar(pedido);
        carrito.vaciar();
        return pedido;
    }

    /** Pedidos de un cliente, del más nuevo al más viejo. */
    public List<Pedido> listarPorCliente(Usuario cliente) {
        List<Pedido> lista = pedidos.listarPorCliente(cliente);
        Collections.reverse(lista);
        return lista;
    }

    /** Todos los pedidos (para el administrador), del más nuevo al más viejo. */
    public List<Pedido> listarTodos() {
        List<Pedido> lista = pedidos.listar();
        Collections.reverse(lista);
        return lista;
    }

    /** Cambia el estado. Al cancelar se devuelve el stock; un pedido cancelado no se reabre. */
    public void cambiarEstado(Pedido pedido, EstadoPedido nuevoEstado) {
        if (pedido.getEstado() == nuevoEstado) {
            return;
        }
        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("Un pedido cancelado no se puede reabrir.");
        }
        if (nuevoEstado == EstadoPedido.CANCELADO) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
            }
        }
        pedido.setEstado(nuevoEstado);
        pedidos.guardar(pedido);
    }
}
