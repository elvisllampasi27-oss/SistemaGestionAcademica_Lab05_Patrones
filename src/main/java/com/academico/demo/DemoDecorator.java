package com.academico.demo;

import com.academico.domain.model.Estudiante;
import com.academico.domain.repository.EstudianteRepository;
import com.academico.infrastructure.adapter.out.persistence.EstudianteRepositoryFactory;
import com.academico.infrastructure.adapter.out.persistence.decorator.LoggingRepositoryDecorator;
import com.academico.infrastructure.adapter.out.persistence.decorator.ValidacionRepositoryDecorator;
import com.academico.infrastructure.config.ConexionBD;

/**
 * Demostración del patrón DECORATOR.
 * Se añaden funciones (log y validación) al repositorio SIN modificar su clase.
 */
public class DemoDecorator {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   DEMO PATRÓN DECORATOR - Repositorio");
        System.out.println("==============================================");

        EstudianteRepository base = EstudianteRepositoryFactory.crear();
        base.eliminar(901); // limpieza por si quedó de otra ejecución

        System.out.println("\n[1] Repositorio SIN decorar (" + base.getClass().getSimpleName() + ")");
        System.out.println("   Estudiantes: " + base.listar().size());

        System.out.println("\n[2] Mismo repositorio + LoggingRepositoryDecorator");
        EstudianteRepository conLog = new LoggingRepositoryDecorator(base);
        System.out.println("   Estudiantes: " + conLog.listar().size());

        System.out.println("\n[3] Decoradores apilados: Validación -> Log -> Repositorio");
        EstudianteRepository completo =
                new ValidacionRepositoryDecorator(new LoggingRepositoryDecorator(base));

        System.out.println("  a) Estudiante válido:");
        completo.guardar(new Estudiante(901, "Ana Torres", "ana.torres@unsch.edu.pe"));
        System.out.println("   -> Guardado correctamente.");

        System.out.println("  b) Estudiante con correo inválido:");
        try {
            completo.guardar(new Estudiante(902, "Pedro", "pedro-sin-arroba"));
        } catch (IllegalArgumentException e) {
            System.out.println("   -> RECHAZADO: " + e.getMessage());
            System.out.println("   -> No aparece [LOG]: la petición nunca llegó al repositorio.");
        }

        System.out.println("\n[4] Limpieza");
        completo.eliminar(901);

        ConexionBD.cerrarSiExiste();
        System.out.println("\n=== FIN DEMO DECORATOR ===");
    }
}
