package com.academico.application.observer;

/**
 * Patrón OBSERVER - Sujeto (publicador).
 * Mantiene la lista de observadores y les avisa de cada cambio.
 */
public interface EstudianteSubject {
    void suscribir(EstudianteObserver observador);
    void desuscribir(EstudianteObserver observador);
    void notificar(EventoEstudiante evento);
}
