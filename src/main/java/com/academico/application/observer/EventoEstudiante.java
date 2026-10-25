package com.academico.application.observer;

import com.academico.domain.model.Estudiante;

import java.time.LocalDateTime;

/** Información que el sujeto envía a cada observador cuando ocurre un cambio. */
public record EventoEstudiante(TipoEvento tipo, Estudiante estudiante, LocalDateTime fecha) {

    public EventoEstudiante(TipoEvento tipo, Estudiante estudiante) {
        this(tipo, estudiante, LocalDateTime.now());
    }
}
