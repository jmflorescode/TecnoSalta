package com.tienda.repositorio;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.Pedido;
import com.tienda.modelo.Producto;
import com.tienda.modelo.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Todo lo que se guarda en tienda.dat: las cuatro listas y el último id usado en cada una.
 * Es un solo objeto para poder escribirlo y leerlo de una vez.
 */
class Datos implements Serializable {

    private static final long serialVersionUID = 1L;

    List<Usuario> usuarios = new ArrayList<>();
    List<Categoria> categorias = new ArrayList<>();
    List<Producto> productos = new ArrayList<>();
    List<Pedido> pedidos = new ArrayList<>();

    int ultimoIdUsuario;
    int ultimoIdCategoria;
    int ultimoIdProducto;
    int ultimoIdPedido;
}
