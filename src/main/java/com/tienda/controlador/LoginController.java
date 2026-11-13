package com.tienda.controlador;

import com.tienda.modelo.Usuario;
import com.tienda.servicio.AuthService;
import com.tienda.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField campoEmail;
    @FXML private PasswordField campoClave;
    @FXML private Label mensajeError;

    private final AuthService authService = new AuthService();

    @FXML
    private void ingresar() {
        try {
            Usuario usuario = authService.iniciarSesion(campoEmail.getText(), campoClave.getText());
            // Según el rol se abre el panel de administrador o el catálogo
            Navegador.irA(usuario.esAdmin() ? "admin" : "catalogo");
        } catch (IllegalArgumentException e) {
            mensajeError.setText(e.getMessage());
            campoClave.clear();
        }
    }

    @FXML
    private void irARegistro() {
        Navegador.irA("registro");
    }
}
