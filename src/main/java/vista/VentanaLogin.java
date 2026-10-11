
package vista;

import conexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VentanaLogin extends JFrame {

    private final JTextField txtCorreo = new JTextField(20);
    private final JPasswordField txtClave = new JPasswordField(20);

    public VentanaLogin() {
        setTitle("Biblioteca Escolar - Inicio de sesión");
        setSize(400, 230);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new GridLayout(3, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtClave);

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnSalir = new JButton("Salir");

        formulario.add(btnIngresar);
        formulario.add(btnSalir);

        add(formulario);

        btnIngresar.addActionListener(e -> ingresar());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void ingresar() {
        String sql = "SELECT nombre, rol FROM usuarios " +
                "WHERE correo=? AND contraseña=?";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtCorreo.getText().trim());
            ps.setString(2, new String(txtClave.getPassword()));

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String rol = rs.getString("rol");

                    JOptionPane.showMessageDialog(this,
                            "Bienvenido/a " + nombre + "\nRol: " + rol);

                    MenuPrincipal menu = new MenuPrincipal(rol);
                    menu.setVisible(true);

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Correo o contraseña incorrectos.");
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VentanaLogin().setVisible(true));
    }
}
