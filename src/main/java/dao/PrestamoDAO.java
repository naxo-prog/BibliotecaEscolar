
package dao;

import conexion.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class PrestamoDAO {

    public synchronized void registrarPrestamo(
            int idEstudiante, int idLibro) throws SQLException {

        String comprobar = "SELECT stock FROM libros WHERE id=? FOR UPDATE";
        String insertar = "INSERT INTO prestamos " +
                "(id_estudiante, id_libro, fecha_prestamo, " +
                "fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, FALSE)";
        String descontar = "UPDATE libros SET stock=stock-1 " +
                "WHERE id=? AND stock>0";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection()) {

            con.setAutoCommit(false);

            try {
                int stock;

                try (PreparedStatement ps =
                             con.prepareStatement(comprobar)) {
                    ps.setInt(1, idLibro);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("El libro no existe.");
                        }
                        stock = rs.getInt("stock");
                    }
                }

                if (stock <= 0) {
                    throw new SQLException(
                            "No hay ejemplares disponibles.");
                }

                try (PreparedStatement ps =
                             con.prepareStatement(descontar)) {
                    ps.setInt(1, idLibro);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "No fue posible actualizar el stock.");
                    }
                }

                LocalDate hoy = LocalDate.now();

                try (PreparedStatement ps =
                             con.prepareStatement(insertar)) {
                    ps.setInt(1, idEstudiante);
                    ps.setInt(2, idLibro);
                    ps.setDate(3, Date.valueOf(hoy));
                    ps.setDate(4, Date.valueOf(hoy.plusDays(7)));
                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException | RuntimeException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public synchronized void registrarDevolucion(
            int idPrestamo) throws SQLException {

        String consulta = "SELECT id_libro FROM prestamos " +
                "WHERE id=? AND devuelto=FALSE FOR UPDATE";
        String devolver = "UPDATE prestamos SET devuelto=TRUE " +
                "WHERE id=? AND devuelto=FALSE";
        String aumentar = "UPDATE libros SET stock=stock+1 WHERE id=?";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection()) {

            con.setAutoCommit(false);

            try {
                int idLibro;

                try (PreparedStatement ps =
                             con.prepareStatement(consulta)) {
                    ps.setInt(1, idPrestamo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Préstamo inexistente o ya devuelto.");
                        }
                        idLibro = rs.getInt("id_libro");
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(devolver)) {
                    ps.setInt(1, idPrestamo);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "No fue posible registrar la devolución.");
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(aumentar)) {
                    ps.setInt(1, idLibro);
                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException | RuntimeException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
