package com.tienda.servicio;

import com.tienda.modelo.Carrito;
import com.tienda.modelo.Usuario;

/** Singleton que recuerda quién inició sesión y cuál es su carrito. */
public class Sesion {

    private static Sesion instancia;

    private Usuario usuario;
    private Carrito carrito = new Carrito();

    private Sesion() {
    }

    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    /** Cada sesión nueva empieza con un carrito vacío. */
    public void iniciar(Usuario usuario) {
        this.usuario = usuario;
        this.carrito = new Carrito();
    }

    public void cerrar() {
        this.usuario = null;
        this.carrito = new Carrito();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Carrito getCarrito() {
        return carrito;
    }
}
