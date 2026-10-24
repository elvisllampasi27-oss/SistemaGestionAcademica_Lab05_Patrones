package com.academico.infrastructure.adapter.out.persistence;

import com.academico.domain.repository.EstudianteRepository;

/**
 * Fábrica (código del docente): decide QUÉ repositorio usar
 * según la variable de entorno STORAGE_TYPE (JSON por defecto).
 */
public class EstudianteRepositoryFactory {

    private EstudianteRepositoryFactory() {
    }

    public static EstudianteRepository crear() {
        String tipo = System.getenv().getOrDefault("STORAGE_TYPE", "JSON");

        return switch (tipo.trim().toUpperCase()) {
            case "JSON" -> new EstudianteRepositoryJson();
            case "POSTGRES" -> new EstudianteRepositoryPostgres();
            default -> throw new IllegalArgumentException(
                    "Almacenamiento no válido: " + tipo
                            + ". Usa JSON o POSTGRES.");
        };
    }
}
