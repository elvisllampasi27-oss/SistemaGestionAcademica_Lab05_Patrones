package com.academico.demo;

import com.academico.infrastructure.config.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Demostración del patrón SINGLETON aplicado a la conexión con PostgreSQL.
 * Prueba 1: varios hilos a la vez obtienen la misma instancia (thread-safe).
 * Prueba 2: dos llamadas devuelven la misma instancia.
 * Prueba 3: la conexión (recurso compartido) se abre una vez y se reutiliza.
 */
public class DemoSingleton {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("==============================================");
        System.out.println("   DEMO PATRÓN SINGLETON - ConexionBD");
        System.out.println("==============================================");

        // ---------- PRUEBA 1: varios hilos ----------
        System.out.println("\n[PRUEBA 1] 5 hilos piden la instancia AL MISMO TIEMPO (primer acceso)");
        Set<Integer> hashes = ConcurrentHashMap.newKeySet();
        Thread[] hilos = new Thread[5];
        for (int i = 0; i < hilos.length; i++) {
            final int n = i + 1;
            hilos[i] = new Thread(() -> {
                ConexionBD instancia = ConexionBD.getInstancia();
                int hash = System.identityHashCode(instancia);
                hashes.add(hash);
                System.out.println("  Hilo " + n + " -> hash " + hash);
            });
            hilos[i].start();
        }
        for (Thread h : hilos) {
            h.join();
        }
        System.out.println("Instancias distintas encontradas: " + hashes.size()
                + (hashes.size() == 1 ? "  (OK, solo una)" : "  (ERROR)"));

        // ---------- PRUEBA 2: misma instancia ----------
        System.out.println("\n[PRUEBA 2] Dos llamadas a getInstancia()");
        ConexionBD c1 = ConexionBD.getInstancia();
        ConexionBD c2 = ConexionBD.getInstancia();
        System.out.println("c1 -> hash " + System.identityHashCode(c1));
        System.out.println("c2 -> hash " + System.identityHashCode(c2));
        System.out.println("¿c1 == c2? " + (c1 == c2));

        // ConexionBD c3 = new ConexionBD(); // NO COMPILA: el constructor es privado

        // ---------- PRUEBA 3: recurso compartido ----------
        System.out.println("\n[PRUEBA 3] La conexión se abre una vez y se reutiliza");
        try {
            Connection con1 = c1.getConexion();
            Connection con2 = c2.getConexion();
            System.out.println("con1 -> hash " + System.identityHashCode(con1));
            System.out.println("con2 -> hash " + System.identityHashCode(con2));
            System.out.println("¿Misma conexión? " + (con1 == con2));

            try (PreparedStatement ps = con1.prepareStatement(
                    "SELECT current_database(), (SELECT COUNT(*) FROM estudiantes)");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Base de datos: " + rs.getString(1));
                    System.out.println("Estudiantes registrados: " + rs.getInt(2));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo conectar a PostgreSQL: " + e.getMessage());
            System.out.println("Revisa que el servicio esté encendido, la BD exista y la contraseña.");
        } finally {
            ConexionBD.getInstancia().cerrar();
        }

        System.out.println("\n==============================================");
        System.out.println("   FIN DE LA DEMO");
        System.out.println("==============================================");
    }
}
