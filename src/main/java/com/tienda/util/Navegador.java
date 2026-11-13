package com.tienda.util;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** Cambia de pantalla cargando el FXML que corresponde, para que ningún controlador repita ese código. */
public class Navegador {

    private static final String CSS = Navegador.class.getResource("/com/tienda/css/tienda.css").toExternalForm();

    // Tamaño preferido de la ventana cuando la pantalla tiene lugar
    private static final double ANCHO = 1100;
    private static final double ALTO = 700;

    private static Stage ventana;
    private static boolean modoOscuro = false;

    /** Se llama una sola vez al arrancar: prepara la ventana principal. */
    public static void iniciar(Stage ventanaPrincipal) {
        ventana = ventanaPrincipal;
        aplicarTema();

        // Área útil de la pantalla: sin la barra de tareas y ya con el escalado de Windows aplicado
        Rectangle2D pantalla = Screen.getPrimary().getVisualBounds();

        // La ventana nunca es más grande que la pantalla. Se dejan 80 px de margen
        // para la barra de título y los bordes, así el botón de cerrar siempre se ve.
        double ancho = Math.min(ANCHO, pantalla.getWidth() - 80);
        double alto = Math.min(ALTO, pantalla.getHeight() - 80);

        Scene escena = new Scene(new StackPane(), ancho, alto);
        escena.getStylesheets().add(CSS);

        ventana.setScene(escena);
        ventana.setTitle("TecnoSalta · Componentes de PC");
        ventana.setMinWidth(Math.min(800, pantalla.getWidth()));
        ventana.setMinHeight(Math.min(540, pantalla.getHeight()));

        // En pantallas chicas (por ejemplo una notebook con escalado al 150 %)
        // la ventana se abre maximizada para aprovechar todo el espacio
        if (pantalla.getWidth() < ANCHO + 80 || pantalla.getHeight() < ALTO + 80) {
            ventana.setMaximized(true);
        }
    }

    /** Muestra una pantalla en la ventana principal. Ejemplo: Navegador.irA("catalogo"). */
    public static void irA(String vista) {
        ventana.getScene().setRoot(cargar(cargador(vista)));
    }

    /** Devuelve el cargador de una vista, por si hace falta pedirle el controlador. */
    public static FXMLLoader cargador(String vista) {
        return new FXMLLoader(Navegador.class.getResource("/com/tienda/vistas/" + vista + ".fxml"));
    }

    public static Parent cargar(FXMLLoader cargador) {
        try {
            return cargador.load();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la vista " + cargador.getLocation(), e);
        }
    }

    /** Abre una ventana secundaria y espera a que se cierre. */
    public static void mostrarModal(Parent raiz, String titulo) {
        Scene escena = new Scene(raiz);
        escena.getStylesheets().add(CSS);

        Stage modal = new Stage();
        modal.initOwner(ventana);
        modal.initModality(Modality.WINDOW_MODAL);
        modal.setTitle(titulo);
        modal.setScene(escena);
        modal.setResizable(false);
        modal.showAndWait();
    }

    public static void alternarTema() {
        modoOscuro = !modoOscuro;
        aplicarTema();
    }

    public static boolean esModoOscuro() {
        return modoOscuro;
    }

    public static Stage getVentana() {
        return ventana;
    }

    private static void aplicarTema() {
        if (modoOscuro) {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
    }
}
