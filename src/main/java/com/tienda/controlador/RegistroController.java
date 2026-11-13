package com.tienda.controlador;

import com.tienda.servicio.AuthService;
import com.tienda.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroController {

    @FXML private TextField campoNombre;
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoClave;
    @FXML private PasswordField campoClaveRepetida;
    @FXML private Label mensajeError;

    private final AuthService authService = new AuthService();

    @FXML
    private void registrar() {
        if (!campoClave.getText().equals(campoClaveRepetida.getText())) {
            mensajeError.setText("Las contraseñas no coinciden.");
            return;
        }
        try {
            authService.registrar(campoNombre.getText(), campoEmail.getText(), campoClave.getText());
            // La cuenta nueva entra directamente al catálogo
            authService.iniciarSesion(campoEmail.getText(), campoClave.getText());
            Navegador.irA("catalogo");
        } catch (IllegalArgumentException e) {
            mensajeError.setText(e.getMessage());
        }
    }

    @FXML
    private void irALogin() {
        Navegador.irA("login");
    }
}
