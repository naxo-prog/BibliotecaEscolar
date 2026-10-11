
package dao;

import conexion.DatabaseConnection;
import modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public List<Libro> listar() throws SQLException {
        List<Libro> lista = new ArrayList<>();

        String sql = "SELECT id, titulo, autor, isbn, " +
                "editorial, stock, id_categoria FROM libros";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getString("editorial"),
                        rs.getInt("stock"),
                        rs.getInt("id_categoria")
                ));
            }
        }

        return lista;
    }

    public boolean agregar(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros " +
                "(titulo, autor, isbn, editorial, stock, id_categoria) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean modificar(Libro libro) throws SQLException {
        String sql = "UPDATE libros SET titulo=?, autor=?, isbn=?, " +
                "editorial=?, stock=?, id_categoria=? WHERE id=?";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id=?";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
