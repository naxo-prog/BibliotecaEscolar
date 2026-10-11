
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private final String url =
            "jdbc:mysql://localhost:3306/biblioteca";
    private final String usuario = "biblioteca_app";
    private final String password = "BibliotecaApp_2026_Prueba";

    private DatabaseConnection() {
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                url, usuario, password
        );
    }
}
