package com.academico;
import com.academico.infrastructure.adapter.out.notification.AuditoriaObserver;
import com.academico.infrastructure.adapter.out.notification.ConsolaObserver;
import com.academico.infrastructure.adapter.out.notification.CorreoObserver;
import com.academico.infrastructure.adapter.out.persistence.EstudianteRepositoryFactory;
import com.academico.infrastructure.adapter.out.persistence.decorator.LoggingRepositoryDecorator;
import com.academico.infrastructure.adapter.out.persistence.decorator.ValidacionRepositoryDecorator;
import com.academico.infrastructure.config.ConexionBD;
import com.academico.presentation.EstudianteUI;

import com.academico.application.service.EstudianteService;
import com.academico.domain.repository.EstudianteRepository;

import  java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1. Adaptador de salida: la fábrica decide JSON o POSTGRES (STORAGE_TYPE)
        EstudianteRepository base = EstudianteRepositoryFactory.crear();

        // DECORATOR: se le añaden validación y log SIN modificar la clase original
        EstudianteRepository repository =
                new ValidacionRepositoryDecorator(
                        new LoggingRepositoryDecorator(base));


        //2. Nucleo de la aplicacion: caso de uso
        EstudianteService service=new EstudianteService(repository);

        // OBSERVER: el servicio (sujeto) avisará de cada cambio a estos observadores
        service.suscribir(new ConsolaObserver());
        service.suscribir(new AuditoriaObserver());
        service.suscribir(new CorreoObserver());

        // presentacion
        EstudianteUI estudianteUI=new EstudianteUI(service);


        Scanner sc = new Scanner(System.in);

        int opcion;

        do {

            System.out.println("\n=== SISTEMA DE GESTIÓN ACADÉMICA ===");
            System.out.println("1. Gestionar estudiantes");
            System.out.println("2. Gestionar cursos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                    estudianteUI.mostrarMenu(sc);
                    break;

                case 2:
                   // CursoUI.mostrarMenu(sc);
                    break;

                case 0:
                    System.out.println("Sistema finalizado.");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        ConexionBD.cerrarSiExiste(); // libera la conexión única (Singleton)
        sc.close();
    }
}