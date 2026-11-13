package com.tienda;

import com.tienda.util.Navegador;
import javafx.application.Application;
import javafx.stage.Stage;

/** Punto de entrada: arranca JavaFX y abre la pantalla de login. */
public class App extends Application {

    @Override
    public void start(Stage ventana) {
        Navegador.iniciar(ventana);
        Navegador.irA("login");
        ventana.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
