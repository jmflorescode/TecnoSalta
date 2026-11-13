package com.tienda.controlador;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.Producto;
import com.tienda.servicio.CarritoService;
import com.tienda.servicio.ProductoService;
import com.tienda.servicio.StockInsuficienteException;
import com.tienda.util.Alertas;
import com.tienda.util.Formato;
import com.tienda.util.Imagenes;
import com.tienda.util.Navegador;
import java.math.BigDecimal;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CatalogoController {

    // Opción del ComboBox que significa "no filtrar por categoría"
    private static final Categoria TODAS = new Categoria("Todas las categorías");

    @FXML private BarraController barraController; // controlador de la barra incluida con fx:include
    @FXML private TextField campoBusqueda;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private TextField campoPrecioMaximo;
    @FXML private Label estado;
    @FXML private FlowPane grilla;

    private final ProductoService productoService = new ProductoService();
    private final CarritoService carritoService = new CarritoService();

    @FXML
    private void initialize() {
        comboCategoria.getItems().add(TODAS);
        comboCategoria.getItems().addAll(productoService.listarCategorias());
        comboCategoria.setValue(TODAS);

        // Cada vez que cambia un filtro se vuelve a armar la grilla
        campoBusqueda.textProperty().addListener((obs, antes, ahora) -> mostrarProductos());
        comboCategoria.valueProperty().addListener((obs, antes, ahora) -> mostrarProductos());
        campoPrecioMaximo.textProperty().addListener((obs, antes, ahora) -> mostrarProductos());

        mostrarProductos();
    }

    @FXML
    private void limpiarFiltros() {
        campoBusqueda.clear();
        campoPrecioMaximo.clear();
        comboCategoria.setValue(TODAS);
    }

    private void mostrarProductos() {
        Categoria categoria = comboCategoria.getValue() == TODAS ? null : comboCategoria.getValue();

        BigDecimal precioMaximo = null;
        String textoPrecio = campoPrecioMaximo.getText().trim().replace(',', '.');
        if (!textoPrecio.isEmpty()) {
            try {
                precioMaximo = new BigDecimal(textoPrecio);
            } catch (NumberFormatException e) {
                estado.setText("El precio máximo tiene que ser un número.");
                return;
            }
        }

        // El filtrado lo hace el servicio; acá solo se dibuja el resultado
        List<Producto> productos = productoService.buscar(campoBusqueda.getText(), categoria, precioMaximo);

        grilla.getChildren().clear();
        for (Producto producto : productos) {
            grilla.getChildren().add(crearTarjeta(producto));
        }

        if (productos.isEmpty()) {
            estado.setText("Ningún producto coincide con la búsqueda.");
        } else {
            estado.setText(productos.size() + " productos");
        }
    }

    private VBox crearTarjeta(Producto producto) {
        Label nombre = new Label(producto.getNombre());
        nombre.getStyleClass().add("nombre-producto");
        nombre.setWrapText(true);
        nombre.setAlignment(Pos.TOP_LEFT);
        nombre.setMinHeight(60); // lugar fijo para 3 renglones: todas las tarjetas quedan del mismo alto
        nombre.setPrefHeight(60);

        Label categoria = new Label(producto.getCategoria().getNombre());
        categoria.getStyleClass().addAll("text-muted", "text-small");

        Label precio = new Label(Formato.precio(producto.getPrecio()));
        precio.getStyleClass().add("precio");

        Button agregar = new Button("Agregar");
        agregar.getStyleClass().add("accent");
        agregar.setMaxWidth(Double.MAX_VALUE);
        agregar.setOnAction(evento -> agregarAlCarrito(producto));
        if (producto.getStock() == 0) {
            agregar.setText("Sin stock");
            agregar.setDisable(true);
        }

        VBox tarjeta = new VBox(8, Imagenes.crear(producto, 200, 140), nombre, categoria, precio, agregar);
        tarjeta.getStyleClass().add("tarjeta");
        tarjeta.setPrefWidth(226);
        tarjeta.setOnMouseClicked(evento -> abrirDetalle(producto));
        return tarjeta;
    }

    private void agregarAlCarrito(Producto producto) {
        try {
            carritoService.agregar(producto, 1);
            barraController.actualizarContador();
            estado.setText("Se agregó \"" + producto.getNombre() + "\" al carrito.");
        } catch (StockInsuficienteException e) {
            Alertas.error(e.getMessage());
        }
    }

    private void abrirDetalle(Producto producto) {
        FXMLLoader cargador = Navegador.cargador("detalle");
        Parent raiz = Navegador.cargar(cargador);

        DetalleController detalle = cargador.getController();
        detalle.setProducto(producto);

        Navegador.mostrarModal(raiz, producto.getNombre());
        barraController.actualizarContador();
    }
}
