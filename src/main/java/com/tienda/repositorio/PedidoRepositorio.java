package com.tienda.repositorio;

import com.tienda.modelo.Pedido;
import com.tienda.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepositorio {

    private final Almacen almacen = Almacen.getInstancia();

    public List<Pedido> listar() {
        return new ArrayList<>(almacen.getPedidos());
    }

    public List<Pedido> listarPorCliente(Usuario cliente) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : almacen.getPedidos()) {
            if (pedido.getCliente().getId() == cliente.getId()) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }

    /**
     * Guarda un pedido nuevo (le asigna id) o los cambios de uno existente.
     * Como el archivo se escribe entero, también queda guardado el stock que cambió con la compra.
     */
    public void guardar(Pedido pedido) {
        if (pedido.getId() == 0) {
            pedido.setId(almacen.siguienteIdPedido());
            almacen.getPedidos().add(pedido);
        }
        almacen.guardar();
    }
}
