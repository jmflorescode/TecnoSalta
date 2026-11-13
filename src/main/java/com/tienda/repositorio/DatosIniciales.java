package com.tienda.repositorio;

import com.tienda.modelo.Categoria;
import com.tienda.modelo.Producto;
import com.tienda.modelo.Rol;
import com.tienda.modelo.Usuario;
import java.math.BigDecimal;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Datos de ejemplo que se cargan la primera vez que se abre el programa
 * (cuando tienda.dat todavía no existe), para que la tienda no arranque vacía.
 *
 * Los precios están en pesos argentinos y se tomaron como referencia de tiendas
 * de hardware del país en noviembre de 2026 (precio de contado o transferencia).
 */
public class DatosIniciales {

    public static void cargar() {
        UsuarioRepositorio usuarios = new UsuarioRepositorio();
        CategoriaRepositorio categorias = new CategoriaRepositorio();
        ProductoRepositorio productos = new ProductoRepositorio();

        // Usuarios de prueba (están anotados en el README)
        usuarios.guardar(new Usuario("Administrador", "admin@tecnosalta.com", hash("admin123"), Rol.ADMIN));
        usuarios.guardar(new Usuario("Cliente de prueba", "cliente@tecnosalta.com", hash("cliente123"), Rol.CLIENTE));

        Categoria procesadores = new Categoria("Procesadores");
        Categoria placasDeVideo = new Categoria("Placas de video");
        Categoria motherboards = new Categoria("Motherboards");
        Categoria memorias = new Categoria("Memorias RAM");
        Categoria almacenamiento = new Categoria("Almacenamiento");
        Categoria fuentes = new Categoria("Fuentes");
        Categoria gabinetes = new Categoria("Gabinetes");
        categorias.guardar(procesadores);
        categorias.guardar(placasDeVideo);
        categorias.guardar(motherboards);
        categorias.guardar(memorias);
        categorias.guardar(almacenamiento);
        categorias.guardar(fuentes);
        categorias.guardar(gabinetes);

        // ---------- Procesadores ----------
        productos.guardar(new Producto("AMD Ryzen 5 5600",
                "6 núcleos y 12 hilos, hasta 4.4 GHz. Socket AM4. Incluye cooler. Sin gráficos integrados.",
                new BigDecimal("219990"), 14, procesadores));
        productos.guardar(new Producto("Intel Core i5-12400F",
                "6 núcleos y 12 hilos, hasta 4.4 GHz. Socket LGA1700. Sin gráficos integrados.",
                new BigDecimal("279990"), 9, procesadores));
        productos.guardar(new Producto("AMD Ryzen 7 7800X3D",
                "8 núcleos y 16 hilos, hasta 5.0 GHz, con 3D V-Cache. Socket AM5. No incluye cooler.",
                new BigDecimal("739990"), 4, procesadores));

        // ---------- Placas de video ----------
        productos.guardar(new Producto("Sapphire Pulse Radeon RX 7600 8GB",
                "8 GB GDDR6. Ideal para jugar en 1080p. Salidas HDMI y DisplayPort.",
                new BigDecimal("734990"), 6, placasDeVideo));
        productos.guardar(new Producto("Gigabyte GeForce RTX 5060 Windforce OC 8GB",
                "8 GB GDDR7 con DLSS 4. Dos ventiladores. Para 1080p con todo en alto.",
                new BigDecimal("899990"), 5, placasDeVideo));
        productos.guardar(new Producto("MSI GeForce RTX 5070 Ventus 2X 12GB",
                "12 GB GDDR7 con DLSS 4. Pensada para jugar en 1440p.",
                new BigDecimal("1689990"), 0, placasDeVideo));

        // ---------- Motherboards ----------
        productos.guardar(new Producto("ASUS Prime A520M-K",
                "Micro ATX para socket AM4. 2 ranuras DDR4 y 1 ranura M.2.",
                new BigDecimal("98990"), 12, motherboards));
        productos.guardar(new Producto("ASRock B550M-HDV",
                "Micro ATX para socket AM4. 2 ranuras DDR4, 1 ranura M.2 NVMe y salida HDMI.",
                new BigDecimal("134990"), 10, motherboards));
        productos.guardar(new Producto("ASUS Prime B650M-A II",
                "Micro ATX para socket AM5. 4 ranuras DDR5, 2 ranuras M.2 y PCIe 4.0.",
                new BigDecimal("214990"), 7, motherboards));

        // ---------- Memorias RAM ----------
        productos.guardar(new Producto("Patriot Signature 16GB DDR4 3200MHz",
                "Un módulo de 16 GB DDR4 a 3200 MHz para PC de escritorio.",
                new BigDecimal("244990"), 20, memorias));
        productos.guardar(new Producto("Kingston Fury Beast RGB 16GB DDR5 6000MHz",
                "Un módulo de 16 GB DDR5 a 6000 MHz CL36 con iluminación RGB.",
                new BigDecimal("639990"), 8, memorias));
        productos.guardar(new Producto("Corsair Vengeance RGB 32GB (2x16GB) DDR5 5200MHz",
                "Kit de dos módulos de 16 GB DDR5 a 5200 MHz con iluminación RGB.",
                new BigDecimal("914990"), 3, memorias));

        // ---------- Almacenamiento ----------
        productos.guardar(new Producto("SSD Kingston A400 480GB SATA",
                "Disco sólido de 2,5 pulgadas SATA III. Hasta 500 MB/s de lectura.",
                new BigDecimal("159990"), 15, almacenamiento));
        productos.guardar(new Producto("SSD Kingston NV3 1TB M.2 NVMe",
                "Disco sólido M.2 2280 PCIe 4.0. Hasta 6000 MB/s de lectura.",
                new BigDecimal("269990"), 18, almacenamiento));

        // ---------- Fuentes ----------
        productos.guardar(new Producto("Fuente Gigabyte P650B 650W 80 Plus Bronze",
                "650 W reales con certificación 80 Plus Bronze y ventilador de 120 mm.",
                new BigDecimal("96990"), 11, fuentes));

        // ---------- Gabinetes ----------
        productos.guardar(new Producto("Gabinete XPG Valor Air",
                "Mid tower con frente mallado y lateral de vidrio templado. Incluye 4 coolers.",
                new BigDecimal("74990"), 9, gabinetes));
        productos.guardar(new Producto("Gabinete Thermaltake View 270 Plus TG ARGB",
                "Mid tower con frente y lateral de vidrio templado. Incluye 3 coolers ARGB.",
                new BigDecimal("124990"), 6, gabinetes));
    }

    private static String hash(String clave) {
        return BCrypt.hashpw(clave, BCrypt.gensalt());
    }
}
