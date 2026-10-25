package com.academico.infrastructure.adapter.out.notification;

import com.academico.application.observer.EstudianteObserver;
import com.academico.application.observer.EventoEstudiante;
import com.academico.application.observer.TipoEvento;

/** Observador concreto: simula el envío de un correo al estudiante registrado. */
public class CorreoObserver implements EstudianteObserver {

    @Override
    public void actualizar(EventoEstudiante evento) {
        if (evento.tipo() == TipoEvento.REGISTRADO) {
            System.out.println("   [CORREO] Enviando bienvenida a "
                    + evento.estudiante().getCorreo() + " (simulado)");
        }
    }
}
