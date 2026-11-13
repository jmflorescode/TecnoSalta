package com.tienda.repositorio;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.Pedido;
import com.tienda.modelo.Producto;
import com.tienda.modelo.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Guarda todos los datos de la tienda en listas en memoria y los persiste en tienda.dat
 * con serialización. Es un singleton: hay un único Almacen para todo el programa.
 */
public class Almacen {

    private static final Path ARCHIVO = Path.of("tienda.dat");

    private static Almacen instancia;

    private final Path archivo; // null = solo memoria, no se escribe ningún archivo
    private Datos datos = new Datos();

    private Almacen(Path archivo) {
        this.archivo = archivo;
        if (archivo != null && Files.exists(archivo)) {
            cargar();
        }
    }

    public static Almacen getInstancia() {
        if (instancia == null) {
            instancia = new Almacen(ARCHIVO);
            // Primera vez (o archivo ilegible): se cargan los datos de ejemplo
            if (instancia.datos.usuarios.isEmpty()) {
                DatosIniciales.cargar();
            }
        }
        return instancia;
    }

    /** Para las pruebas: empieza con las listas vacías y no escribe ningún archivo. */
    public static void usarMemoriaVacia() {
        instancia = new Almacen(null);
    }

    /** Escribe todas las listas en tienda.dat. Se llama después de cada cambio. */
    public void guardar() {
        if (archivo == null) {
            return;
        }
        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(archivo))) {
            salida.writeObject(datos);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar " + archivo, e);
        }
    }

    private void cargar() {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivo))) {
            datos = (Datos) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Pasa si cambiaron las clases del modelo: se empieza de nuevo con los datos de ejemplo
            System.err.println("No se pudo leer " + archivo + ". Se vuelven a crear los datos de ejemplo.");
            datos = new Datos();
        }
    }

    public List<Usuario> getUsuarios() {
        return datos.usuarios;
    }

    public List<Categoria> getCategorias() {
        return datos.categorias;
    }

    public List<Producto> getProductos() {
        return datos.productos;
    }

    public List<Pedido> getPedidos() {
        return datos.pedidos;
    }

    public int siguienteIdUsuario() {
        return ++datos.ultimoIdUsuario;
    }

    public int siguienteIdCategoria() {
        return ++datos.ultimoIdCategoria;
    }

    public int siguienteIdProducto() {
        return ++datos.ultimoIdProducto;
    }

    public int siguienteIdPedido() {
        return ++datos.ultimoIdPedido;
    }
}
