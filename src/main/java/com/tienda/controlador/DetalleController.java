package com.tienda.controlador;

import com.tienda.modelo.Producto;
import com.tienda.servicio.CarritoService;
import com.tienda.servicio.StockInsuficienteException;
import com.tienda.util.Formato;
import com.tienda.util.Imagenes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Ventana con el detalle de un producto. El catálogo le pasa el producto con setProducto. */
public class DetalleController {

    @FXML private StackPane contenedorImagen;
    @FXML private Label nombre;
    @FXML private Label categoria;
    @FXML private Label descripcion;
    @FXML private Label precio;
    @FXML private Label stock;
    @FXML private Spinner<Integer> spinnerCantidad;
    @FXML private Label mensajeError;
    @FXML private Button botonAgregar;

    private final CarritoService carritoService = new CarritoService();
    private Producto producto;

    public void setProducto(Producto producto) {
        this.producto = producto;

        contenedorImagen.getChildren().setAll(Imagenes.crear(producto, 320, 240));
        nombre.setText(producto.getNombre());
        categoria.setText(producto.getCategoria().getNombre());
        descripcion.setText(producto.getDescripcion());
        precio.setText(Formato.precio(producto.getPrecio()));

        boolean hayStock = producto.getStock() > 0;
        stock.setText(hayStock ? "Stock disponible: " + producto.getStock() : "Sin stock");

        // El Spinner no deja elegir más unidades que el stock
        int maximo = Math.max(1, producto.getStock());
        spinnerCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maximo, 1));
        spinnerCantidad.setDisable(!hayStock);
        botonAgregar.setDisable(!hayStock);
    }

    @FXML
    private void agregar() {
        try {
            carritoService.agregar(producto, spinnerCantidad.getValue());
            cerrar();
        } catch (StockInsuficienteException e) {
            mensajeError.setText(e.getMessage());
        }
    }

    @FXML
    private void cerrar() {
        Stage ventana = (Stage) nombre.getScene().getWindow();
        ventana.close();
    }
}
