package com.tienda.controlador;

import com.tienda.modelo.Carrito;
import com.tienda.modelo.ItemCarrito;
import com.tienda.modelo.Pedido;
import com.tienda.servicio.PedidoService;
import com.tienda.servicio.Sesion;
import com.tienda.servicio.StockInsuficienteException;
import com.tienda.util.Alertas;
import com.tienda.util.Formato;
import com.tienda.util.Navegador;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ConfirmacionController {

    @FXML private TableView<ItemCarrito> tabla;
    @FXML private TableColumn<ItemCarrito, String> colProducto;
    @FXML private TableColumn<ItemCarrito, String> colCantidad;
    @FXML private TableColumn<ItemCarrito, String> colPrecio;
    @FXML private TableColumn<ItemCarrito, String> colSubtotal;
    @FXML private Label total;
    @FXML private Button botonConfirmar;

    private final PedidoService pedidoService = new PedidoService();

    @FXML
    private void initialize() {
        colProducto.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getProducto().getNombre()));
        colCantidad.setCellValueFactory(fila ->
                new SimpleStringProperty(String.valueOf(fila.getValue().getCantidad())));
        colPrecio.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getProducto().getPrecio())));
        colSubtotal.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getSubtotal())));

        Carrito carrito = Sesion.getInstancia().getCarrito();
        tabla.getItems().setAll(carrito.getItems());
        total.setText("Total a pagar: " + Formato.precio(carrito.getTotal()));
        botonConfirmar.setDisable(carrito.getItems().isEmpty());
    }

    @FXML
    private void confirmar() {
        try {
            Pedido pedido = pedidoService.confirmarCompra();
            Alertas.info("Compra confirmada. Pedido N.º " + pedido.getId()
                    + " por " + Formato.precio(pedido.getTotal()) + ".");
            Navegador.irA("mis_pedidos");
        } catch (StockInsuficienteException e) {
            // No se guardó nada: se vuelve al carrito para corregir las cantidades
            Alertas.error(e.getMessage());
            Navegador.irA("carrito");
        }
    }

    @FXML
    private void volver() {
        Navegador.irA("carrito");
    }
}
