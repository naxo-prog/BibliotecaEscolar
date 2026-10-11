
package vista;

import conexion.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VentanaReportes extends JFrame {

    private final DefaultTableModel modelo =
            new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };

    private final JTable tabla = new JTable(modelo);

    public VentanaReportes() {
        setTitle("Biblioteca Escolar - Reportes");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton btnMasPrestados = new JButton("Libros más prestados");
        JButton btnHistorial = new JButton("Historial por estudiante");
        JButton btnVigentes = new JButton("Préstamos vigentes");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnMasPrestados);
        botones.add(btnHistorial);
        botones.add(btnVigentes);

        add(botones, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnMasPrestados.addActionListener(e -> ejecutarConsulta(
                "SELECT l.titulo AS Libro, COUNT(p.id) AS Prestamos " +
                        "FROM libros l LEFT JOIN prestamos p ON l.id=p.id_libro " +
                        "GROUP BY l.id, l.titulo ORDER BY Prestamos DESC",
                "Libros más prestados"));

        btnHistorial.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(
                    this, "Ingresa el ID del estudiante:");

            if (id == null || id.trim().isEmpty()) return;

            try {
                Integer.parseInt(id.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El ID debe ser numérico.");
                return;
            }

            ejecutarConsulta(
                    "SELECT e.nombre AS Estudiante, l.titulo AS Libro, " +
                            "p.fecha_prestamo AS Prestamo, " +
                            "p.fecha_devolucion AS Vencimiento, " +
                            "p.devuelto AS Devuelto " +
                            "FROM prestamos p " +
                            "JOIN estudiantes e ON p.id_estudiante=e.id " +
                            "JOIN libros l ON p.id_libro=l.id " +
                            "WHERE e.id=? ORDER BY p.fecha_prestamo DESC",
                    "Historial del estudiante", Integer.parseInt(id.trim()));
        });

        btnVigentes.addActionListener(e -> ejecutarConsulta(
                "SELECT p.id AS ID, e.nombre AS Estudiante, " +
                        "l.titulo AS Libro, p.fecha_prestamo AS Prestamo, " +
                        "p.fecha_devolucion AS Vencimiento " +
                        "FROM prestamos p " +
                        "JOIN estudiantes e ON p.id_estudiante=e.id " +
                        "JOIN libros l ON p.id_libro=l.id " +
                        "WHERE p.devuelto=FALSE ORDER BY p.fecha_devolucion",
                "Préstamos vigentes"));
    }

    private void ejecutarConsulta(String sql, String titulo) {
        ejecutarConsulta(sql, titulo, null);
    }

    private void ejecutarConsulta(
            String sql, String titulo, Integer idEstudiante) {

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (idEstudiante != null) {
                ps.setInt(1, idEstudiante);
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();

                modelo.setRowCount(0);
                modelo.setColumnCount(0);

                for (int i = 1; i <= columnas; i++) {
                    modelo.addColumn(meta.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[columnas];

                    for (int i = 1; i <= columnas; i++) {
                        fila[i - 1] = rs.getObject(i);
                    }

                    modelo.addRow(fila);
                }

                setTitle("Biblioteca Escolar - " + titulo);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Error SQL",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VentanaReportes().setVisible(true));
    }
}
