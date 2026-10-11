
package vista;

import conexion.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection con =
                     DatabaseConnection.getInstance().getConnection()) {

            System.out.println("================================");
            System.out.println("BIBLIOTECA ESCOLAR");
            System.out.println("CONEXION MYSQL EXITOSA");
            System.out.println("Base de datos: "
                    + con.getCatalog());
            System.out.println("================================");

        } catch (SQLException e) {
            System.out.println("ERROR DE CONEXION MYSQL");
            e.printStackTrace();
        }
    }
}
