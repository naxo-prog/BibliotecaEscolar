
package vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private String rolUsuario;

    public MenuPrincipal(String rol) {
        this.rolUsuario = rol;
        setTitle("Sistema de Gestión de Biblioteca Escolar");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel(
                "BIBLIOTECA ESCOLAR",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitulo = new JLabel(
                "Sistema de gestión bibliotecaria",
                SwingConstants.CENTER
        );

        JButton btnLibros = new JButton("Gestión de libros");
        JButton btnEstudiantes = new JButton("Gestión de estudiantes");
        JButton btnPrestamos = new JButton("Préstamos y devoluciones");
        JButton btnReportes = new JButton("Reportes");
        JButton btnSalir = new JButton("Salir");

        JPanel centro = new JPanel(new GridLayout(5, 1, 10, 10));
        centro.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        centro.add(btnLibros);
        centro.add(btnEstudiantes);
        centro.add(btnPrestamos);
        centro.add(btnReportes);
        centro.add(btnSalir);

        if ("estudiante".equalsIgnoreCase(rolUsuario)) {
            btnLibros.setEnabled(false);
            btnEstudiantes.setEnabled(false);
            btnReportes.setEnabled(false);
        }


        JPanel encabezado = new JPanel(new GridLayout(2, 1, 5, 5));
        encabezado.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        add(encabezado, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        btnLibros.addActionListener(e ->
                new VentanaLibros().setVisible(true));

        btnEstudiantes.addActionListener(e ->
                new VentanaEstudiantes().setVisible(true));

        btnPrestamos.addActionListener(e ->
                new VentanaPrestamos().setVisible(true));

        btnReportes.addActionListener(e ->
                new VentanaReportes().setVisible(true));

        btnSalir.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas salir del sistema?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VentanaLogin().setVisible(true));
    }
}
