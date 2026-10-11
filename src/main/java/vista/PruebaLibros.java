
package vista;

import dao.LibroDAO;
import modelo.Libro;

public class PruebaLibros {

    public static void main(String[] args) {
        try {
            LibroDAO dao = new LibroDAO();

            for (Libro libro : dao.listar()) {
                System.out.println(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al consultar libros:");
            e.printStackTrace();
        }
    }
}
