package com.tienda.controlador;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.DetallePedido;
import com.tienda.modelo.EstadoPedido;
import com.tienda.modelo.Pedido;
import com.tienda.modelo.Producto;
import com.tienda.servicio.AuthService;
import com.tienda.servicio.PedidoService;
import com.tienda.servicio.ProductoService;
import com.tienda.util.Alertas;
import com.tienda.util.Formato;
import com.tienda.util.Imagenes;
import com.tienda.util.Navegador;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/** Panel de administrador: una pestaña para productos, otra para categorías y otra para pedidos. */
public class AdminController {

    @FXML private Button botonTema;

    // Pestaña Productos
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colProdNombre;
    @FXML private TableColumn<Producto, String> colProdCategoria;
    @FXML private TableColumn<Producto, String> colProdPrecio;
    @FXML private TableColumn<Producto, String> colProdStock;
    @FXML private TableColumn<Producto, String> colProdActivo;
    @FXML private Label tituloProducto;
    @FXML private TextField campoNombre;
    @FXML private TextArea campoDescripcion;
    @FXML private TextField campoPrecio;
    @FXML private TextField campoStock;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private Label etiquetaImagen;
    @FXML private CheckBox checkActivo;
    @FXML private Label mensajeProducto;

    // Pestaña Categorías
    @FXML private ListView<Categoria> listaCategorias;
    @FXML private Label tituloCategoria;
    @FXML private TextField campoCategoria;
    @FXML private Label mensajeCategoria;

    // Pestaña Pedidos
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, String> colPedNumero;
    @FXML private TableColumn<Pedido, String> colPedFecha;
    @FXML private TableColumn<Pedido, String> colPedCliente;
    @FXML private TableColumn<Pedido, String> colPedEstado;
    @FXML private TableColumn<Pedido, String> colPedTotal;
    @FXML private Label tituloDetalle;
    @FXML private Label mensajePedido;
    @FXML private ComboBox<EstadoPedido> comboEstado;
    @FXML private TableView<DetallePedido> tablaDetalles;
    @FXML private TableColumn<DetallePedido, String> colDetProducto;
    @FXML private TableColumn<DetallePedido, String> colDetCantidad;
    @FXML private TableColumn<DetallePedido, String> colDetPrecio;
    @FXML private TableColumn<DetallePedido, String> colDetSubtotal;

    private final ProductoService productoService = new ProductoService();
    private final PedidoService pedidoService = new PedidoService();
    private final AuthService authService = new AuthService();

    private String imagenElegida; // ruta de la imagen del producto que se está editando

    @FXML
    private void initialize() {
        botonTema.setText(Navegador.esModoOscuro() ? "Modo claro" : "Modo oscuro");
        configurarColumnas();

        // Al seleccionar un renglón, sus datos pasan al formulario de la derecha
        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
                (obs, antes, producto) -> mostrarProducto(producto));
        listaCategorias.getSelectionModel().selectedItemProperty().addListener(
                (obs, antes, categoria) -> mostrarCategoria(categoria));
        tablaPedidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, antes, pedido) -> mostrarPedido(pedido));

        comboEstado.getItems().setAll(EstadoPedido.values());

        cargarCategorias();
        cargarProductos();
        tablaPedidos.getItems().setAll(pedidoService.listarTodos());
    }

    private void configurarColumnas() {
        colProdNombre.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getNombre()));
        colProdCategoria.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getCategoria().getNombre()));
        colProdPrecio.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getPrecio())));
        colProdStock.setCellValueFactory(fila ->
                new SimpleStringProperty(String.valueOf(fila.getValue().getStock())));
        colProdActivo.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().isActivo() ? "Sí" : "No"));

        colPedNumero.setCellValueFactory(fila ->
                new SimpleStringProperty(String.valueOf(fila.getValue().getId())));
        colPedFecha.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.fecha(fila.getValue().getFecha())));
        colPedCliente.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getCliente().getNombre()));
        colPedEstado.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getEstado().toString()));
        colPedTotal.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getTotal())));

        colDetProducto.setCellValueFactory(fila ->
                new SimpleStringProperty(fila.getValue().getProducto().getNombre()));
        colDetCantidad.setCellValueFactory(fila ->
                new SimpleStringProperty(String.valueOf(fila.getValue().getCantidad())));
        colDetPrecio.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getPrecioUnitario())));
        colDetSubtotal.setCellValueFactory(fila ->
                new SimpleStringProperty(Formato.precio(fila.getValue().getSubtotal())));
    }

    // ==================== Productos ====================

    private void cargarProductos() {
        tablaProductos.getItems().setAll(productoService.listarTodos());
    }

    /** Pasa un producto al formulario. Con null deja el formulario listo para un producto nuevo. */
    private void mostrarProducto(Producto producto) {
        if (producto == null) {
            tituloProducto.setText("Producto nuevo");
            campoNombre.clear();
            campoDescripcion.clear();
            campoPrecio.clear();
            campoStock.clear();
            comboCategoria.setValue(null);
            checkActivo.setSelected(true);
            imagenElegida = null;
        } else {
            tituloProducto.setText("Editar producto");
            campoNombre.setText(producto.getNombre());
            campoDescripcion.setText(producto.getDescripcion());
            campoPrecio.setText(producto.getPrecio().toPlainString());
            campoStock.setText(String.valueOf(producto.getStock()));
            comboCategoria.setValue(producto.getCategoria());
            checkActivo.setSelected(producto.isActivo());
            imagenElegida = producto.getImagenRuta();
        }
        etiquetaImagen.setText(imagenElegida == null ? "Sin imagen" : new File(imagenElegida).getName());
    }

    @FXML
    private void nuevoProducto() {
        tablaProductos.getSelectionModel().clearSelection();
        mostrarProducto(null);
        mostrarMensaje(mensajeProducto, "", false);
    }

    @FXML
    private void elegirImagen() {
        FileChooser selector = new FileChooser();
        selector.setTitle("Elegir imagen del producto");
        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File archivo = selector.showOpenDialog(Navegador.getVentana());
        if (archivo == null) {
            return; // el usuario canceló
        }
        try {
            imagenElegida = Imagenes.importar(archivo);
            etiquetaImagen.setText(archivo.getName());
        } catch (IOException e) {
            mostrarMensaje(mensajeProducto, "No se pudo copiar la imagen.", true);
        }
    }

    @FXML
    private void guardarProducto() {
        // 1) Convertir los textos a números
        BigDecimal precio;
        int stock;
        try {
            precio = new BigDecimal(campoPrecio.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            mostrarMensaje(mensajeProducto, "El precio tiene que ser un número.", true);
            return;
        }
        try {
            stock = Integer.parseInt(campoStock.getText().trim());
        } catch (NumberFormatException e) {
            mostrarMensaje(mensajeProducto, "El stock tiene que ser un número entero.", true);
            return;
        }

        // 2) Validar antes de modificar el producto
        try {
            productoService.validar(campoNombre.getText(), precio, stock, comboCategoria.getValue());
        } catch (IllegalArgumentException e) {
            mostrarMensaje(mensajeProducto, e.getMessage(), true);
            return;
        }

        // 3) Si hay un producto seleccionado se edita; si no, se crea uno nuevo
        Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
        if (producto == null) {
            producto = new Producto();
        }
        producto.setNombre(campoNombre.getText().trim());
        producto.setDescripcion(campoDescripcion.getText().trim());
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(comboCategoria.getValue());
        producto.setImagenRuta(imagenElegida);
        producto.setActivo(checkActivo.isSelected());
        productoService.guardar(producto);

        cargarProductos();
        tablaProductos.getSelectionModel().select(producto);
        mostrarMensaje(mensajeProducto, "Producto guardado.", false);
    }

    @FXML
    private void darDeBajaProducto() {
        Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
        if (producto == null) {
            mostrarMensaje(mensajeProducto, "Primero hay que seleccionar un producto de la tabla.", true);
            return;
        }
        if (!Alertas.confirmar("¿Dar de baja \"" + producto.getNombre() + "\"? Deja de verse en el catálogo.")) {
            return;
        }
        productoService.darDeBaja(producto);
        cargarProductos();
        tablaProductos.getSelectionModel().select(producto);
        mostrarMensaje(mensajeProducto, "Producto dado de baja.", false);
    }

    // ==================== Categorías ====================

    private void cargarCategorias() {
        List<Categoria> categorias = productoService.listarCategorias();
        listaCategorias.getItems().setAll(categorias);
        comboCategoria.getItems().setAll(categorias);
    }

    private void mostrarCategoria(Categoria categoria) {
        if (categoria == null) {
            tituloCategoria.setText("Categoría nueva");
            campoCategoria.clear();
        } else {
            tituloCategoria.setText("Editar categoría");
            campoCategoria.setText(categoria.getNombre());
        }
    }

    @FXML
    private void nuevaCategoria() {
        listaCategorias.getSelectionModel().clearSelection();
        mostrarCategoria(null);
        mostrarMensaje(mensajeCategoria, "", false);
    }

    @FXML
    private void guardarCategoria() {
        Categoria categoria = listaCategorias.getSelectionModel().getSelectedItem();
        if (categoria == null) {
            categoria = new Categoria("");
        }
        try {
            productoService.guardarCategoria(categoria, campoCategoria.getText());
        } catch (IllegalArgumentException e) {
            mostrarMensaje(mensajeCategoria, e.getMessage(), true);
            return;
        }
        cargarCategorias();
        cargarProductos(); // la tabla de productos muestra el nombre de la categoría
        listaCategorias.getSelectionModel().select(categoria);
        mostrarMensaje(mensajeCategoria, "Categoría guardada.", false);
    }

    @FXML
    private void eliminarCategoria() {
        Categoria categoria = listaCategorias.getSelectionModel().getSelectedItem();
        if (categoria == null) {
            mostrarMensaje(mensajeCategoria, "Primero hay que seleccionar una categoría de la lista.", true);
            return;
        }
        if (!Alertas.confirmar("¿Eliminar la categoría \"" + categoria.getNombre() + "\"?")) {
            return;
        }
        try {
            productoService.eliminarCategoria(categoria);
        } catch (IllegalArgumentException e) {
            mostrarMensaje(mensajeCategoria, e.getMessage(), true);
            return;
        }
        cargarCategorias();
        mostrarMensaje(mensajeCategoria, "Categoría eliminada.", false);
    }

    // ==================== Pedidos ====================

    private void mostrarPedido(Pedido pedido) {
        if (pedido == null) {
            tablaDetalles.getItems().clear();
            comboEstado.setValue(null);
            tituloDetalle.setText("Seleccionar un pedido para ver su detalle");
        } else {
            tablaDetalles.getItems().setAll(pedido.getDetalles());
            comboEstado.setValue(pedido.getEstado());
            tituloDetalle.setText("Detalle del pedido N.º " + pedido.getId());
        }
        mostrarMensaje(mensajePedido, "", false);
    }

    @FXML
    private void cambiarEstado() {
        Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();
        if (pedido == null || comboEstado.getValue() == null) {
            mostrarMensaje(mensajePedido, "Primero hay que seleccionar un pedido.", true);
            return;
        }
        try {
            pedidoService.cambiarEstado(pedido, comboEstado.getValue());
        } catch (IllegalStateException e) {
            comboEstado.setValue(pedido.getEstado());
            mostrarMensaje(mensajePedido, e.getMessage(), true);
            return;
        }
        tablaPedidos.refresh();
        tablaProductos.refresh(); // al cancelar un pedido vuelve el stock
        mostrarMensaje(mensajePedido, "Estado actualizado.", false);
    }

    // ==================== Barra superior ====================

    @FXML
    private void cambiarTema() {
        Navegador.alternarTema();
        botonTema.setText(Navegador.esModoOscuro() ? "Modo claro" : "Modo oscuro");
    }

    @FXML
    private void cerrarSesion() {
        authService.cerrarSesion();
        Navegador.irA("login");
    }

    private void mostrarMensaje(Label etiqueta, String texto, boolean esError) {
        etiqueta.setText(texto);
        etiqueta.getStyleClass().removeAll("mensaje-ok", "mensaje-error");
        etiqueta.getStyleClass().add(esError ? "mensaje-error" : "mensaje-ok");
    }
}
