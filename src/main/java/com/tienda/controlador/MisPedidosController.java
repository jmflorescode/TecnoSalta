package com.tienda.controlador;

import com.tienda.modelo.DetallePedido;
import com.tienda.modelo.Pedido;
import com.tienda.servicio.PedidoService;
import com.tienda.servicio.Sesion;
import com.tienda.util.Formato;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MisPedidosController {

    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, String> colNumero;
    @FXML private TableColumn<Pedido, String> colFecha;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private TableColumn<Pedido, String> colTotal;

    @FXML private Label tituloDetalle;
    @FXML private TableView<DetallePedido> tablaDetalles;
    @FXML private TableColumn<DetallePedido, String> colProducto;
    @FXML private TableColumn<DetallePedido, String> colCantidad;
    @FXML private TableColumn<DetallePedido, String> colPrecio;
    @FXML private TableColumn<DetallePedido, String> colSubtotal;

    private final PedidoService pedidoService = new PedidoService();

    @FXML
    private void initialize() {
        colNumero.setCellValueFactory(fila ->
                new SimpleStringProperty(String.valueOf(fila.getValue().getId())));
        colFecha.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.fecha(fila.getValue().getFecha())));
        colEstado.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getEstado().toString()));
        colTotal.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getTotal())));

        colProducto.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getProducto().getNombre()));
        colCantidad.setCellValueFactory(fila ->
                new SimpleStringProperty(String.valueOf(fila.getValue().getCantidad())));
        colPrecio.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getPrecioUnitario())));
        colSubtotal.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getSubtotal())));

        // Al seleccionar un pedido se muestran sus renglones en la tabla de abajo
        tablaPedidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, antes, pedido) -> mostrarDetalle(pedido));

        tablaPedidos.getItems().setAll(
                pedidoService.listarPorCliente(Sesion.getInstancia().getUsuario()));
    }

    private void mostrarDetalle(Pedido pedido) {
        if (pedido == null) {
            tablaDetalles.getItems().clear();
            tituloDetalle.setText("Seleccionar un pedido para ver su detalle");
        } else {
            tablaDetalles.getItems().setAll(pedido.getDetalles());
            tituloDetalle.setText("Detalle del pedido N.º " + pedido.getId());
        }
    }
}
