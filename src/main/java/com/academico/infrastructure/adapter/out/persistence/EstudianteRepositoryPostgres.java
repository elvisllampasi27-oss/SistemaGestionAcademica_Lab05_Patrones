package com.academico.infrastructure.adapter.out.persistence;

import com.academico.domain.model.Estudiante;
import com.academico.domain.repository.EstudianteRepository;
import com.academico.infrastructure.config.ConexionBD;

import java.sql.*;
import java.util.*;

public class EstudianteRepositoryPostgres implements EstudianteRepository {

    // Ya no guarda url/usuario/password: se los pide al Singleton
    private Connection conectar() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }


    @Override
    public void guardar(Estudiante estudiante) {
        String sql = """
            INSERT INTO estudiantes (id, nombre, correo)
            VALUES (?, ?, ?)
            """;

        // La conexión NO va en el try: es compartida y no debe cerrarse aquí
        try (PreparedStatement ps = conectar().prepareStatement(sql)) {

            ps.setInt(1, estudiante.getId());
            ps.setString(2, estudiante.getNombre());
            ps.setString(3, estudiante.getCorreo());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo registrar al estudiante.", e
            );
        }
    }

    @Override
    public List<Estudiante> listar() {
        String sql = """
            SELECT id, nombre, correo
            FROM estudiantes
            ORDER BY id
            """;

        List<Estudiante> estudiantes = new ArrayList<>();

        // La conexión NO va en el try: es compartida y no debe cerrarse aquí
        try (PreparedStatement ps = conectar().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                estudiantes.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo")
                ));
            }

            return estudiantes;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudieron listar los estudiantes.", e
            );
        }
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) {
        String sql = """
            SELECT id, nombre, correo
            FROM estudiantes
            WHERE id = ?
            """;

        // La conexión NO va en el try: es compartida y no debe cerrarse aquí
        try (PreparedStatement ps = conectar().prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("correo")
                    ));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo buscar al estudiante.", e
            );
        }
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {
        String sql = """
            UPDATE estudiantes
            SET nombre = ?, correo = ?
            WHERE id = ?
            """;

        // La conexión NO va en el try: es compartida y no debe cerrarse aquí
        try (PreparedStatement ps = conectar().prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getCorreo());
            ps.setInt(3, estudiante.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo actualizar al estudiante.", e
            );
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        // La conexión NO va en el try: es compartida y no debe cerrarse aquí
        try (PreparedStatement ps = conectar().prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo eliminar al estudiante.", e
            );
        }
    }



}
