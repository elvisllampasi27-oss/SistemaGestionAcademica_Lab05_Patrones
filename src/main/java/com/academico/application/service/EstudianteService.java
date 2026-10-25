package com.academico.application.service;

import com.academico.application.observer.EstudianteObserver;
import com.academico.application.observer.EstudianteSubject;
import com.academico.application.observer.EventoEstudiante;
import com.academico.application.observer.TipoEvento;
import com.academico.application.port.in.EstudianteUseCase;
import com.academico.domain.model.Estudiante;
import com.academico.domain.repository.EstudianteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Caso de uso de estudiantes.
 * Además es el SUJETO del patrón Observer: cada vez que un estudiante
 * se registra, actualiza o elimina, avisa a todos sus observadores.
 */
public class EstudianteService implements EstudianteUseCase, EstudianteSubject {

    private final EstudianteRepository repository;
    private final List<EstudianteObserver> observadores = new ArrayList<>();

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    // ----- Observer -----
    @Override
    public void suscribir(EstudianteObserver observador) {
        observadores.add(observador);
    }

    @Override
    public void desuscribir(EstudianteObserver observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar(EventoEstudiante evento) {
        for (EstudianteObserver o : observadores) {
            o.actualizar(evento);
        }
    }

    // ----- Casos de uso -----
    @Override
    public void registrar(Estudiante estudiante) {
        repository.guardar(estudiante);
        notificar(new EventoEstudiante(TipoEvento.REGISTRADO, estudiante));
    }

    @Override
    public List<Estudiante> listar() {
        return repository.listar();
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {
        boolean ok = repository.actualizar(estudiante);
        if (ok) {
            notificar(new EventoEstudiante(TipoEvento.ACTUALIZADO, estudiante));
        }
        return ok;
    }

    @Override
    public boolean eliminar(int id) {
        Optional<Estudiante> existente = repository.buscarPorId(id);
        boolean ok = repository.eliminar(id);
        if (ok) {
            notificar(new EventoEstudiante(TipoEvento.ELIMINADO,
                    existente.orElse(new Estudiante(id, "(desconocido)", ""))));
        }
        return ok;
    }
}
