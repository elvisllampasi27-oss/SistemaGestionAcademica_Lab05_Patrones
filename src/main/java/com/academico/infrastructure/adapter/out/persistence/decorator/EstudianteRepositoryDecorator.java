package com.academico.infrastructure.adapter.out.persistence.decorator;

import com.academico.domain.model.Estudiante;
import com.academico.domain.repository.EstudianteRepository;

import java.util.List;
import java.util.Optional;

/**
 * Patrón DECORATOR (estructural) - Decorador base.
 * Implementa la misma interfaz que el objeto que envuelve y, por defecto,
 * delega todas las operaciones en él. Los decoradores concretos
 * sobrescriben solo lo que necesitan para AÑADIR comportamiento
 * sin modificar las clases originales (JSON o PostgreSQL).
 */
public abstract class EstudianteRepositoryDecorator implements EstudianteRepository {

    protected final EstudianteRepository envuelto;

    protected EstudianteRepositoryDecorator(EstudianteRepository envuelto) {
        this.envuelto = envuelto;
    }

    @Override
    public void guardar(Estudiante estudiante) {
        envuelto.guardar(estudiante);
    }

    @Override
    public List<Estudiante> listar() {
        return envuelto.listar();
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) {
        return envuelto.buscarPorId(id);
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {
        return envuelto.actualizar(estudiante);
    }

    @Override
    public boolean eliminar(int id) {
        return envuelto.eliminar(id);
    }
}
