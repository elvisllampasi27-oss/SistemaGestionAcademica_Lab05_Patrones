package com.academico.infrastructure.adapter.out.persistence.decorator;

import com.academico.domain.model.Estudiante;
import com.academico.domain.repository.EstudianteRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Decorador concreto: registra en consola cada operación
 * y el tiempo que tardó, sin tocar el repositorio original.
 */
public class LoggingRepositoryDecorator extends EstudianteRepositoryDecorator {

    public LoggingRepositoryDecorator(EstudianteRepository envuelto) {
        super(envuelto);
    }

    private <T> T medir(String operacion, Supplier<T> accion) {
        long inicio = System.nanoTime();
        T resultado = accion.get();
        long ms = (System.nanoTime() - inicio) / 1_000_000;
        System.out.println("   [LOG] " + operacion + " -> " + ms + " ms ("
                + envuelto.getClass().getSimpleName() + ")");
        return resultado;
    }

    @Override
    public void guardar(Estudiante estudiante) {
        medir("guardar(id=" + estudiante.getId() + ")", () -> {
            super.guardar(estudiante);
            return null;
        });
    }

    @Override
    public List<Estudiante> listar() {
        return medir("listar()", super::listar);
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) {
        return medir("buscarPorId(" + id + ")", () -> super.buscarPorId(id));
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {
        return medir("actualizar(id=" + estudiante.getId() + ")",
                () -> super.actualizar(estudiante));
    }

    @Override
    public boolean eliminar(int id) {
        return medir("eliminar(" + id + ")", () -> super.eliminar(id));
    }
}
