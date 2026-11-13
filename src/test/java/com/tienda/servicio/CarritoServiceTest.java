package com.tienda.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.Producto;
import com.tienda.modelo.Rol;
import com.tienda.modelo.Usuario;
import com.tienda.repositorio.Almacen;
import com.tienda.repositorio.CategoriaRepositorio;
import com.tienda.repositorio.ProductoRepositorio;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarritoServiceTest {

    private CarritoService carritoService;
    private Producto mouse;
    private Producto teclado;

    @BeforeEach
    void preparar() {
        // Cada prueba arranca con datos propios en memoria: no se toca tienda.dat
        Almacen.usarMemoriaVacia();

        Categoria categoria = new Categoria("Electrónica");
        new CategoriaRepositorio().guardar(categoria);

        mouse = new Producto("Mouse", "", new BigDecimal("10.50"), 5, categoria);
        teclado = new Producto("Teclado", "", new BigDecimal("30.00"), 2, categoria);
        ProductoRepositorio productos = new ProductoRepositorio();
        productos.guardar(mouse);
        productos.guardar(teclado);

        Sesion.getInstancia().iniciar(new Usuario("Ana", "ana@correo.com", "hash", Rol.CLIENTE));
        carritoService = new CarritoService();
    }

    @Test
    void agregarElMismoProductoSumaLaCantidad() throws StockInsuficienteException {
        carritoService.agregar(mouse, 1);
        carritoService.agregar(mouse, 2);

        assertEquals(1, carritoService.getCarrito().getItems().size());
        assertEquals(3, carritoService.getCarrito().getCantidadTotal());
    }

    @Test
    void elTotalSumaLosSubtotales() throws StockInsuficienteException {
        carritoService.agregar(mouse, 2);   // 2 x 10.50 = 21.00
        carritoService.agregar(teclado, 1); // 1 x 30.00 = 30.00

        assertEquals(new BigDecimal("51.00"), carritoService.getCarrito().getTotal());
    }

    @Test
    void noSePuedeAgregarMasQueElStock() throws StockInsuficienteException {
        carritoService.agregar(teclado, 2); // el stock es 2

        assertThrows(StockInsuficienteException.class, () -> carritoService.agregar(teclado, 1));
        assertEquals(2, carritoService.getCarrito().getCantidadTotal());
    }

    @Test
    void cambiarCantidadValidaElStock() throws StockInsuficienteException {
        carritoService.agregar(mouse, 1);

        carritoService.cambiarCantidad(mouse, 5);
        assertEquals(5, carritoService.getCarrito().getCantidadTotal());

        assertThrows(StockInsuficienteException.class, () -> carritoService.cambiarCantidad(mouse, 6));
        assertEquals(5, carritoService.getCarrito().getCantidadTotal());
    }

    @Test
    void quitarSacaElProductoDelCarrito() throws StockInsuficienteException {
        carritoService.agregar(mouse, 1);
        carritoService.quitar(mouse);

        assertTrue(carritoService.getCarrito().getItems().isEmpty());
    }
}
