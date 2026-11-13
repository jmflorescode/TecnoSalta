package com.tienda.controlador;

import com.tienda.modelo.Carrito;
import com.tienda.modelo.ItemCarrito;
import com.tienda.servicio.CarritoService;
import com.tienda.servicio.StockInsuficienteException;
import com.tienda.util.Alertas;
import com.tienda.util.Formato;
import com.tienda.util.Navegador;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CarritoController {

    @FXML private BarraController barraController;
    @FXML private TableView<ItemCarrito> tabla;
    @FXML private TableColumn<ItemCarrito, String> colProducto;
    @FXML private TableColumn<ItemCarrito, String> colPrecio;
    @FXML private TableColumn<ItemCarrito, Integer> colCantidad;
    @FXML private TableColumn<ItemCarrito, String> colSubtotal;
    @FXML private TableColumn<ItemCarrito, Void> colQuitar;
    @FXML private Label total;
    @FXML private Button botonContinuar;

    private final CarritoService carritoService = new CarritoService();

    @FXML
    private void initialize() {
        // Cada columna dice qué dato del ItemCarrito muestra
        colProducto.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getProducto().getNombre()));
        colPrecio.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getProducto().getPrecio())));
        colCantidad.setCellValueFactory(fila ->
                new SimpleObjectProperty<>(fila.getValue().getCantidad()));
        colSubtotal.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getSubtotal())));

        // Estas dos columnas no muestran texto sino un control por renglón
        colCantidad.setCellFactory(columna -> new CeldaCantidad());
        colQuitar.setCellFactory(columna -> new CeldaQuitar());

        tabla.getItems().setAll(carritoService.getCarrito().getItems());
        actualizarTotales();
    }

    @FXML
    private void irACatalogo() {
        Navegador.irA("catalogo");
    }

    @FXML
    private void continuar() {
        Navegador.irA("confirmacion");
    }

    private void cambiarCantidad(ItemCarrito item, int cantidad) {
        try {
            carritoService.cambiarCantidad(item.getProducto(), cantidad);
        } catch (StockInsuficienteException e) {
            Alertas.error(e.getMessage());
        }
        tabla.refresh(); // vuelve a dibujar los renglones (cambia el subtotal)
        actualizarTotales();
    }

    private void quitar(ItemCarrito item) {
        carritoService.quitar(item.getProducto());
        tabla.getItems().remove(item);
        actualizarTotales();
    }

    private void actualizarTotales() {
        Carrito carrito = carritoService.getCarrito();
        total.setText("Total: " + Formato.precio(carrito.getTotal()));
        botonContinuar.setDisable(carrito.getItems().isEmpty());
        barraController.actualizarContador();
    }

    /** Celda de la columna Cantidad: un Spinner que va de 1 al stock del producto. */
    private class CeldaCantidad extends TableCell<ItemCarrito, Integer> {

        private final Spinner<Integer> spinner = new Spinner<>();
        private boolean cargando; // true mientras la tabla llena el Spinner (no es un cambio del usuario)

        CeldaCantidad() {
            spinner.setPrefWidth(100);
            spinner.valueProperty().addListener((obs, antes, ahora) -> {
                if (!cargando && ahora != null) {
                    cambiarCantidad(getTableView().getItems().get(getIndex()), ahora);
                }
            });
        }

        @Override
        protected void updateItem(Integer cantidad, boolean vacia) {
            super.updateItem(cantidad, vacia);
            if (vacia || cantidad == null) {
                setGraphic(null);
                return;
            }
            ItemCarrito item = getTableView().getItems().get(getIndex());
            int maximo = Math.max(item.getProducto().getStock(), cantidad);

            cargando = true;
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maximo, cantidad));
            cargando = false;
            setGraphic(spinner);
        }
    }

    /** Celda de la última columna: un botón para sacar el producto del carrito. */
    private class CeldaQuitar extends TableCell<ItemCarrito, Void> {

        private final Button boton = new Button("Quitar");

        CeldaQuitar() {
            boton.getStyleClass().addAll("flat", "danger");
            boton.setOnAction(evento -> quitar(getTableView().getItems().get(getIndex())));
        }

        @Override
        protected void updateItem(Void valor, boolean vacia) {
            super.updateItem(valor, vacia);
            setGraphic(vacia ? null : boton);
        }
    }
}
