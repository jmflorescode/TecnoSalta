package com.tienda.servicio;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.Producto;
import com.tienda.repositorio.CategoriaRepositorio;
import com.tienda.repositorio.ProductoRepositorio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Reglas del catálogo: búsqueda, filtros y alta, baja y modificación de productos y categorías. */
public class ProductoService {

    private final ProductoRepositorio productos = new ProductoRepositorio();
    private final CategoriaRepositorio categorias = new CategoriaRepositorio();

    // ---------- Productos ----------

    /** Todos los productos, incluidos los dados de baja (para el administrador). */
    public List<Producto> listarTodos() {
        return productos.listar();
    }

    /** Solo los productos que se muestran en el catálogo. */
    public List<Producto> listarActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto producto : productos.listar()) {
            if (producto.isActivo()) {
                activos.add(producto);
            }
        }
        return activos;
    }

    /**
     * Filtra el catálogo. Cada filtro es opcional:
     * texto vacío, categoría null o precio máximo null significan "no filtrar por eso".
     */
    public List<Producto> buscar(String texto, Categoria categoria, BigDecimal precioMaximo) {
        String buscado = texto == null ? "" : texto.trim().toLowerCase();

        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : listarActivos()) {
            boolean coincideNombre = producto.getNombre().toLowerCase().contains(buscado);
            boolean coincideCategoria = categoria == null
                    || producto.getCategoria().getId() == categoria.getId();
            boolean coincidePrecio = precioMaximo == null
                    || producto.getPrecio().compareTo(precioMaximo) <= 0;

            if (coincideNombre && coincideCategoria && coincidePrecio) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    /** Lanza IllegalArgumentException con un mensaje para mostrar si algún dato no es válido. */
    public void validar(String nombre, BigDecimal precio, int stock, Categoria categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Hay que elegir una categoría.");
        }
    }

    public void guardar(Producto producto) {
        validar(producto.getNombre(), producto.getPrecio(), producto.getStock(), producto.getCategoria());
        productos.guardar(producto);
    }

    /** Borrado lógico: el producto deja de verse en el catálogo pero sigue en los pedidos viejos. */
    public void darDeBaja(Producto producto) {
        producto.setActivo(false);
        productos.guardar(producto);
    }

    // ---------- Categorías ----------

    public List<Categoria> listarCategorias() {
        return categorias.listar();
    }

    /** Crea la categoría (si es nueva) o le cambia el nombre. El nombre no se puede repetir. */
    public void guardarCategoria(Categoria categoria, String nombre) {
        nombre = nombre == null ? "" : nombre.trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        Categoria existente = categorias.buscarPorNombre(nombre);
        if (existente != null && existente != categoria) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre.");
        }
        categoria.setNombre(nombre);
        categorias.guardar(categoria);
    }

    /** Una categoría con productos no se puede eliminar. */
    public void eliminarCategoria(Categoria categoria) {
        for (Producto producto : productos.listar()) {
            if (producto.getCategoria().getId() == categoria.getId()) {
                throw new IllegalArgumentException("No se puede eliminar: hay productos en esa categoría.");
            }
        }
        categorias.eliminar(categoria);
    }
}
