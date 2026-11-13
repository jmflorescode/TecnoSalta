package com.tienda.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String imagenRuta;
    private Categoria categoria;
    private boolean activo = true;

    public Producto() {
    }

    public Producto(String nombre, String descripcion, BigDecimal precio, int stock, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        setPrecio(precio);
        this.stock = stock;
        this.categoria = categoria;
    }

    /** Indica si alcanza el stock para la cantidad pedida. */
    public boolean hayStock(int cantidad) {
        return cantidad > 0 && cantidad <= stock;
    }

    /** Resta del stock. El stock nunca puede quedar negativo. */
    public void descontarStock(int cantidad) {
        if (!hayStock(cantidad)) {
            throw new IllegalArgumentException("No hay stock suficiente de " + nombre);
        }
        stock -= cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    /** El precio siempre se guarda redondeado a 2 decimales. */
    public void setPrecio(BigDecimal precio) {
        this.precio = precio.setScale(2, RoundingMode.HALF_UP);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagenRuta() {
        return imagenRuta;
    }

    public void setImagenRuta(String imagenRuta) {
        this.imagenRuta = imagenRuta;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
