package com.academico.infrastructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Patrón SINGLETON (creacional).
 * Garantiza que exista UNA sola instancia que administra la conexión
 * a PostgreSQL y ofrece un único punto de acceso global a ella.
 */
public final class ConexionBD {

    // 1. Atributo estático que guarda la única instancia
    private static volatile ConexionBD instancia;

    private final String url;
    private final String usuario;
    private final String password;
    private Connection conexion;

    // 2. Constructor PRIVADO: nadie puede hacer "new ConexionBD()" desde fuera
    private ConexionBD() {
        this.url = System.getenv().getOrDefault(
                "DB_URL", "jdbc:postgresql://localhost:5432/sistema_gestion_academica");
        this.usuario = System.getenv().getOrDefault("DB_USER", "postgres");
        this.password = System.getenv().getOrDefault("DB_PASSWORD", "12345");
        System.out.println(">> [Singleton] Se creó la ÚNICA instancia de ConexionBD");
    }

    // 3. Método estático de acceso global (thread-safe, doble verificación)
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            synchronized (ConexionBD.class) {
                if (instancia == null) {
                    instancia = new ConexionBD();
                }
            }
        }
        return instancia;
    }

    // 4. Recurso compartido: se abre una vez y se reutiliza
    public synchronized Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println(">> [Singleton] Conexión abierta con PostgreSQL");
        }
        return conexion;
    }

    /** Cierra la conexión solo si el Singleton ya fue creado (no lo crea para cerrarlo). */
    public static void cerrarSiExiste() {
        if (instancia != null) {
            instancia.cerrar();
        }
    }

    public synchronized void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println(">> [Singleton] Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
