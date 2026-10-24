-- =====================================================
-- Sistema de Gestión Académica - Lab 05 (Singleton)
-- Ejecutar en pgAdmin sobre la BD: sistema_gestion_academica
-- =====================================================

CREATE TABLE IF NOT EXISTS estudiantes (
    id     INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL
);

INSERT INTO estudiantes (id, nombre, correo) VALUES
    (1, 'Alberto', 'alberto@unsch.edu.pe'),
    (2, 'Marcos',  'marcos@unsch.edu.pe'),
    (3, 'Juan',    'juan@unsch.edu.pe')
ON CONFLICT (id) DO NOTHING;

SELECT * FROM estudiantes ORDER BY id;
