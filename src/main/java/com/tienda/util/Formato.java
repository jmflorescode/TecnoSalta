package com.tienda.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Convierte precios y fechas al texto que se muestra en pantalla. */
public class Formato {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String precio(BigDecimal valor) {
        return String.format("$ %,.2f", valor);
    }

    public static String fecha(LocalDateTime fecha) {
        return fecha.format(FECHA);
    }
}
