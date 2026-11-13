package com.tienda.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.EstadoPedido;
import com.tienda.modelo.Pedido;
import com.tienda.modelo.Producto;
import com.tienda.modelo.Rol;
import com.tienda.modelo.Usuario;
import com.tienda.repositorio.Almacen;
import com.tienda.repositorio.CategoriaRepositorio;
import com.tienda.repositorio.ProductoRepositorio;
import com.tienda.repositorio.UsuarioRepositorio;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoServiceTest {

    private CarritoService carritoService;
    private PedidoService pedidoService;
    private Usuario ana;
    private Usuario luis;
    private Producto mouse;
    private Producto teclado;

    @BeforeEach
    void preparar() {
        // Cada prueba arranca con datos propios en memoria: no se toca tienda.dat
        Almacen.usarMemoriaVacia();

        ana = new Usuario("Ana", "ana@correo.com", "hash", Rol.CLIENTE);
        luis = new Usuario("Luis", "luis@correo.com", "hash", Rol.CLIENTE);
        UsuarioRepositorio usuarios = new UsuarioRepositorio();
        usuarios.guardar(ana);
        usuarios.guardar(luis);

        Categoria categoria = new Categoria("Electrónica");
        new CategoriaRepositorio().guardar(categoria);

        mouse = new Producto("Mouse", "", new BigDecimal("10.50"), 5, categoria);
        teclado = new Producto("Teclado", "", new BigDecimal("30.00"), 2, categoria);
        ProductoRepositorio productos = new ProductoRepositorio();
        productos.guardar(mouse);
        productos.guardar(teclado);

        Sesion.getInstancia().iniciar(ana);
        carritoService = new CarritoService();
        pedidoService = new PedidoService();
    }

    @Test
    void confirmarCompraCreaElPedidoDescuentaStockYVaciaElCarrito() throws StockInsuficienteException {
        carritoService.agregar(mouse, 2);
        carritoService.agregar(teclado, 1);

        Pedido pedido = pedidoService.confirmarCompra();

        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
        assertEquals(2, pedido.getDetalles().size());
        assertEquals(new BigDecimal("51.00"), pedido.getTotal());
        assertEquals(3, mouse.getStock());
        assertEquals(1, teclado.getStock());
        assertTrue(carritoService.getCarrito().getItems().isEmpty());
        assertEquals(1, pedidoService.listarPorCliente(ana).size());
    }

    @Test
    void siFaltaStockDeUnProductoNoSeGuardaNada() throws StockInsuficienteException {
        carritoService.agregar(mouse, 2);
        carritoService.agregar(teclado, 2);
        teclado.setStock(1); // alguien compró un teclado mientras el carrito estaba armado

        assertThrows(StockInsuficienteException.class, () -> pedidoService.confirmarCompra());

        assertEquals(5, mouse.getStock());   // no se descontó el producto que sí tenía stock
        assertEquals(1, teclado.getStock());
        assertTrue(pedidoService.listarTodos().isEmpty());
        assertEquals(2, carritoService.getCarrito().getItems().size()); // el carrito sigue igual
    }

    @Test
    void conElCarritoVacioNoSePuedeComprar() {
        assertThrows(IllegalStateException.class, () -> pedidoService.confirmarCompra());
    }

    @Test
    void elPedidoGuardaElPrecioDelMomentoDeLaCompra() throws StockInsuficienteException {
        carritoService.agregar(mouse, 2);
        Pedido pedido = pedidoService.confirmarCompra();

        mouse.setPrecio(new BigDecimal("99.00")); // el precio cambia después de la compra

        assertEquals(new BigDecimal("21.00"), pedido.getTotal());
    }

    @Test
    void cadaClienteVeSoloSusPedidos() throws StockInsuficienteException {
        carritoService.agregar(mouse, 1);
        pedidoService.confirmarCompra();

        assertEquals(1, pedidoService.listarPorCliente(ana).size());
        assertTrue(pedidoService.listarPorCliente(luis).isEmpty());
        assertEquals(1, pedidoService.listarTodos().size());
    }

    @Test
    void cancelarUnPedidoDevuelveElStock() throws StockInsuficienteException {
        carritoService.agregar(mouse, 2);
        Pedido pedido = pedidoService.confirmarCompra();
        assertEquals(3, mouse.getStock());

        pedidoService.cambiarEstado(pedido, EstadoPedido.CANCELADO);

        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        assertEquals(5, mouse.getStock());
    }

    @Test
    void unPedidoCanceladoNoSePuedeReabrir() throws StockInsuficienteException {
        carritoService.agregar(mouse, 1);
        Pedido pedido = pedidoService.confirmarCompra();
        pedidoService.cambiarEstado(pedido, EstadoPedido.CANCELADO);

        assertThrows(IllegalStateException.class,
                () -> pedidoService.cambiarEstado(pedido, EstadoPedido.ENVIADO));
    }
}
