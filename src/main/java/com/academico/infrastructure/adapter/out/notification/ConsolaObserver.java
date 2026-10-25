package com.academico.infrastructure.adapter.out.notification;

import com.academico.application.observer.EstudianteObserver;
import com.academico.application.observer.EventoEstudiante;

/** Observador concreto: muestra el evento en la consola. */
public class ConsolaObserver implements EstudianteObserver {

    @Override
    public void actualizar(EventoEstudiante evento) {
        System.out.println("   [CONSOLA] Estudiante " + evento.tipo()
                + ": " + evento.estudiante().getNombre()
                + " (id=" + evento.estudiante().getId() + ")");
    }
}
