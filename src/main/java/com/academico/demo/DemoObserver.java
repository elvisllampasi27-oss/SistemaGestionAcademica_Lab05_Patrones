package com.academico.demo;

import com.academico.application.observer.EstudianteObserver;
import com.academico.application.service.EstudianteService;
import com.academico.domain.model.Estudiante;
import com.academico.infrastructure.adapter.out.notification.AuditoriaObserver;
import com.academico.infrastructure.adapter.out.notification.ConsolaObserver;
import com.academico.infrastructure.adapter.out.notification.CorreoObserver;
import com.academico.infrastructure.adapter.out.persistence.EstudianteRepositoryFactory;
import com.academico.infrastructure.config.ConexionBD;

/**
 * Demostración del patrón OBSERVER.
 * EstudianteService (sujeto) notifica cada cambio a varios observadores,
 * igual que un botón de una interfaz gráfica avisa a sus "listeners".
 */
public class DemoObserver {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   DEMO PATRÓN OBSERVER - Eventos de estudiante");
        System.out.println("==============================================");

        EstudianteService servicio = new EstudianteService(EstudianteRepositoryFactory.crear());
        servicio.eliminar(902); // limpieza previa (aún sin observadores)

        EstudianteObserver correo = new CorreoObserver();
        servicio.suscribir(new ConsolaObserver());
        servicio.suscribir(new AuditoriaObserver());
        servicio.suscribir(correo);
        // Observador con lambda: igual que button.addActionListener(e -> ...) en Swing
        servicio.suscribir(evento -> System.out.println(
                "   [LAMBDA] Evento recibido a las " + evento.fecha().toLocalTime().withNano(0)));
        System.out.println("Observadores suscritos: Consola, Auditoría, Correo, Lambda");

        System.out.println("\n[1] Registrar estudiante 902");
        servicio.registrar(new Estudiante(902, "Lucía Quispe", "lucia.quispe@unsch.edu.pe"));

        System.out.println("\n[2] Actualizar estudiante 902");
        servicio.actualizar(new Estudiante(902, "Lucía Quispe Rojas", "lucia.quispe@unsch.edu.pe"));

        System.out.println("\n[3] Desuscribir CorreoObserver y eliminar estudiante 902");
        servicio.desuscribir(correo);
        servicio.eliminar(902);

        ConexionBD.cerrarSiExiste();
        System.out.println("\n=== FIN DEMO OBSERVER (ver data/auditoria.log) ===");
    }
}
