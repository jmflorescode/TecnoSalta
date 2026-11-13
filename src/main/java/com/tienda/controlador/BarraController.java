package com.tienda.controlador;

import com.tienda.servicio.AuthService;
import com.tienda.servicio.Sesion;
import com.tienda.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/** Barra superior del cliente: navegación, contador del carrito, tema y cierre de sesión. */
public class BarraController {

    @FXML private Label saludo;
    @FXML private Button botonCarrito;
    @FXML private Button botonTema;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        saludo.setText("Hola, " + Sesion.getInstancia().getUsuario().getNombre());
        botonTema.setText(Navegador.esModoOscuro() ? "Modo claro" : "Modo oscuro");
        actualizarContador();
    }

    /** Lo llaman las pantallas cada vez que cambia el carrito. */
    public void actualizarContador() {
        int cantidad = Sesion.getInstancia().getCarrito().getCantidadTotal();
        botonCarrito.setText("Carrito (" + cantidad + ")");
    }

    @FXML
    private void irACatalogo() {
        Navegador.irA("catalogo");
    }

    @FXML
    private void irACarrito() {
        Navegador.irA("carrito");
    }

    @FXML
    private void irAMisPedidos() {
        Navegador.irA("mis_pedidos");
    }

    @FXML
    private void cambiarTema() {
        Navegador.alternarTema();
        botonTema.setText(Navegador.esModoOscuro() ? "Modo claro" : "Modo oscuro");
    }

    @FXML
    private void cerrarSesion() {
        authService.cerrarSesion();
        Navegador.irA("login");
    }
}
