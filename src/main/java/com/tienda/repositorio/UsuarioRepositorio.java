package com.tienda.repositorio;

import com.tienda.modelo.Usuario;

public class UsuarioRepositorio {

    private final Almacen almacen = Almacen.getInstancia();

    /** Devuelve el usuario con ese email, o null si no existe. */
    public Usuario buscarPorEmail(String email) {
        for (Usuario usuario : almacen.getUsuarios()) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    /** Guarda un usuario nuevo (le asigna id) o los cambios de uno existente. */
    public void guardar(Usuario usuario) {
        if (usuario.getId() == 0) {
            usuario.setId(almacen.siguienteIdUsuario());
            almacen.getUsuarios().add(usuario);
        }
        almacen.guardar();
    }
}
