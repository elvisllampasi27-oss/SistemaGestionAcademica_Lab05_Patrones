package com.academico.infrastructure.adapter.out.persistence.decorator;

import com.academico.domain.model.Estudiante;
import com.academico.domain.repository.EstudianteRepository;

/**
 * Decorador concreto: valida los datos ANTES de guardarlos o actualizarlos.
 * Si algo es inválido lanza una excepción y la petición no llega a la BD.
 */
public class ValidacionRepositoryDecorator extends EstudianteRepositoryDecorator {

    public ValidacionRepositoryDecorator(EstudianteRepository envuelto) {
        super(envuelto);
    }

    private void validar(Estudiante e) {
        if (e.getId() <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0.");
        }
        if (e.getNombre() == null || e.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (e.getCorreo() == null || !e.getCorreo().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("Correo inválido: " + e.getCorreo());
        }
        System.out.println("   [VALIDACIÓN] Datos correctos para id=" + e.getId());
    }

    @Override
    public void guardar(Estudiante estudiante) {
        validar(estudiante);
        super.guardar(estudiante);
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {
        validar(estudiante);
        return super.actualizar(estudiante);
    }
}
