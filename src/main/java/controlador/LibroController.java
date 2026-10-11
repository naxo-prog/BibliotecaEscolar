
package controlador;

import dao.LibroDAO;
import modelo.Libro;

import java.sql.SQLException;
import java.util.List;

public class LibroController {

    private final LibroDAO libroDAO;

    public LibroController() {
        this.libroDAO = new LibroDAO();
    }

    public List<Libro> listarLibros() throws SQLException {
        return libroDAO.listar();
    }

    public boolean agregarLibro(Libro libro) throws SQLException {
        validarLibro(libro);
        return libroDAO.agregar(libro);
    }

    public boolean modificarLibro(Libro libro) throws SQLException {
        validarLibro(libro);

        if (libro.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El ID del libro debe ser válido.");
        }

        return libroDAO.modificar(libro);
    }

    public boolean eliminarLibro(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID del libro debe ser válido.");
        }

        return libroDAO.eliminar(id);
    }

    private void validarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro no puede ser nulo.");
        }

        if (libro.getTitulo() == null ||
                libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El título es obligatorio.");
        }

        if (libro.getAutor() == null ||
                libro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El autor es obligatorio.");
        }

        if (libro.getStock() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo.");
        }

        if (libro.getIdCategoria() <= 0) {
            throw new IllegalArgumentException(
                    "La categoría debe ser válida.");
        }
    }
}
