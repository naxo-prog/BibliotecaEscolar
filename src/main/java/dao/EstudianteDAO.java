
package dao;

import conexion.DatabaseConnection;
import modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public List<Estudiante> listar() throws SQLException {
        List<Estudiante> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, rut, curso, correo " +
                "FROM estudiantes ORDER BY id";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rut"),
                        rs.getString("curso"),
                        rs.getString("correo")
                ));
            }
        }

        return lista;
    }

    public boolean agregar(Estudiante estudiante)
            throws SQLException {

        String sql = "INSERT INTO estudiantes " +
                "(nombre, rut, curso, correo) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean modificar(Estudiante estudiante)
            throws SQLException {

        String sql = "UPDATE estudiantes SET nombre=?, rut=?, " +
                "curso=?, correo=? WHERE id=?";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());
            ps.setInt(5, estudiante.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM estudiantes WHERE id=?";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
