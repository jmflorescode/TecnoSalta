package com.tienda.repositorio;

import com.tienda.modelo.Categoria;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositorio {

    private final Almacen almacen = Almacen.getInstancia();

    public List<Categoria> listar() {
        return new ArrayList<>(almacen.getCategorias());
    }

    /** Devuelve la categoría con ese nombre, o null si no existe. */
    public Categoria buscarPorNombre(String nombre) {
        for (Categoria categoria : almacen.getCategorias()) {
            if (categoria.getNombre().equalsIgnoreCase(nombre)) {
                return categoria;
            }
        }
        return null;
    }

    /** Guarda una categoría nueva (le asigna id) o los cambios de una existente. */
    public void guardar(Categoria categoria) {
        if (categoria.getId() == 0) {
            categoria.setId(almacen.siguienteIdCategoria());
            almacen.getCategorias().add(categoria);
        }
        almacen.guardar();
    }

    public void eliminar(Categoria categoria) {
        almacen.getCategorias().remove(categoria);
        almacen.guardar();
    }
}
