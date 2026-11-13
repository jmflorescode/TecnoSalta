package com.tienda.util;

import com.tienda.modelo.Producto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/** Muestra la foto de un producto y copia al proyecto las fotos que elige el administrador. */
public class Imagenes {

    private static final Path CARPETA = Path.of("imagenes");

    // Colores de fondo para los productos que todavía no tienen foto
    private static final String[] COLORES = {
        "#2f6fde", "#1a936f", "#c2571a", "#8e44ad", "#b83a4b", "#0e7c86", "#5c6b7a"
    };

    /** Devuelve la foto del producto o, si no tiene, un recuadro de color con su inicial. */
    public static Node crear(Producto producto, double ancho, double alto) {
        StackPane marco = new StackPane();
        marco.setMinSize(ancho, alto);
        marco.setPrefSize(ancho, alto);
        marco.setMaxSize(ancho, alto);

        String ruta = producto.getImagenRuta();
        if (ruta != null && Files.exists(Path.of(ruta))) {
            Image imagen = new Image(Path.of(ruta).toUri().toString(), ancho, alto, true, true);
            marco.getChildren().add(new ImageView(imagen));
        } else {
            Label inicial = new Label(producto.getNombre().substring(0, 1).toUpperCase());
            inicial.getStyleClass().add("sin-foto-letra");
            String color = COLORES[producto.getCategoria().getId() % COLORES.length];
            marco.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 6;");
            marco.getChildren().add(inicial);
        }
        return marco;
    }

    /** Copia la imagen elegida a la carpeta imagenes/ y devuelve la ruta que se guarda en el producto. */
    public static String importar(File archivo) throws IOException {
        Files.createDirectories(CARPETA);
        Path destino = CARPETA.resolve(System.currentTimeMillis() + "_" + archivo.getName());
        Files.copy(archivo.toPath(), destino);
        return destino.toString();
    }
}
