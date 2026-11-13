package com.tienda.servicio;

import com.tienda.modelo.Rol;
import com.tienda.modelo.Usuario;
import com.tienda.repositorio.UsuarioRepositorio;
import org.mindrot.jbcrypt.BCrypt;

/** Registro e inicio de sesión. Las contraseñas se guardan siempre como hash BCrypt. */
public class AuthService {

    private final UsuarioRepositorio usuarios = new UsuarioRepositorio();

    /**
     * Crea una cuenta de cliente.
     * Lanza IllegalArgumentException con un mensaje para mostrar si algún dato no es válido.
     */
    public Usuario registrar(String nombre, String email, String clave) {
        nombre = nombre == null ? "" : nombre.trim();
        email = email == null ? "" : email.trim().toLowerCase();

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException("El email no es válido.");
        }
        if (clave == null || clave.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (usuarios.buscarPorEmail(email) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email.");
        }

        String hash = BCrypt.hashpw(clave, BCrypt.gensalt());
        Usuario usuario = new Usuario(nombre, email, hash, Rol.CLIENTE);
        usuarios.guardar(usuario);
        return usuario;
    }

    /**
     * Verifica email y contraseña y deja al usuario guardado en la Sesion.
     * Lanza IllegalArgumentException si los datos no coinciden.
     */
    public Usuario iniciarSesion(String email, String clave) {
        Usuario usuario = usuarios.buscarPorEmail(email == null ? "" : email.trim());

        // Mismo mensaje en los dos casos, para no revelar qué emails están registrados
        if (usuario == null || clave == null || !BCrypt.checkpw(clave, usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos.");
        }

        Sesion.getInstancia().iniciar(usuario);
        return usuario;
    }

    public void cerrarSesion() {
        Sesion.getInstancia().cerrar();
    }
}
