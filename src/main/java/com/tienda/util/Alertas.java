package com.tienda.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/** Ventanas de aviso que usan todas las pantallas. */
public class Alertas {

    public static void info(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, "Información", mensaje);
    }

    public static void error(String mensaje) {
        mostrar(Alert.AlertType.ERROR, "Error", mensaje);
    }

    /** Devuelve true si el usuario aceptó. */
    public static boolean confirmar(String mensaje) {
        Alert alerta = crear(Alert.AlertType.CONFIRMATION, "Confirmar", mensaje);
        return alerta.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        crear(tipo, titulo, mensaje).showAndWait();
    }

    private static Alert crear(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.initOwner(Navegador.getVentana());
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        return alerta;
    }
}
