package com.academico.application.observer;

/**
 * Patrón OBSERVER (comportamiento) - Observador.
 * Todo el que quiera enterarse de los cambios en estudiantes implementa esta interfaz.
 */
public interface EstudianteObserver {
    void actualizar(EventoEstudiante evento);
}
