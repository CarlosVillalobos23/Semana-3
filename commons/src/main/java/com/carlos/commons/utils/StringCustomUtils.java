package com.carlos.commons.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringCustomUtils {
    private static final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void validarNoVacio(String texto, String mensaje) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    public static void validarTamano(String texto, Integer min, Integer max, String mensaje) {
        validarNoVacio(texto, mensaje);
        if (texto.length() < min || texto.length() > max) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    public static void validarCapacidad(Integer capacidad, Integer tamano) {
        if (capacidad == null || String.valueOf(capacidad).length() > tamano || capacidad < 0) {
            throw new IllegalArgumentException("La capacidad está fuera del límite");
        }
    }

    public static String quitarAcentos(String texto) {
        return texto == null ? null : texto.toLowerCase()
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u").replace("ü", "u");
    }

    public static String localDateString(LocalDate fecha) {
        return fecha == null ? null : fecha.format(formato);
    }
}
