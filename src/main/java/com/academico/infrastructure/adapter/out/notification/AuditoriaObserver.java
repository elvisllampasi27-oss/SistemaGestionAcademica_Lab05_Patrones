package com.academico.infrastructure.adapter.out.notification;

import com.academico.application.observer.EstudianteObserver;
import com.academico.application.observer.EventoEstudiante;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

/** Observador concreto: deja un registro de auditoría en data/auditoria.log */
public class AuditoriaObserver implements EstudianteObserver {

    private final Path archivo = Path.of("data", "auditoria.log");
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void actualizar(EventoEstudiante evento) {
        String linea = evento.fecha().format(FORMATO) + " | " + evento.tipo()
                + " | id=" + evento.estudiante().getId()
                + " | " + evento.estudiante().getNombre() + System.lineSeparator();
        try {
            Files.createDirectories(archivo.getParent());
            Files.writeString(archivo, linea,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("   [AUDITORÍA] Registrado en " + archivo);
        } catch (IOException e) {
            System.out.println("   [AUDITORÍA] No se pudo escribir: " + e.getMessage());
        }
    }
}
