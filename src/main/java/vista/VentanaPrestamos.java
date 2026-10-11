
package vista;

import conexion.DatabaseConnection;
import dao.PrestamoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VentanaPrestamos extends JFrame {

    private final JTextField txtEstudiante = new JTextField(5);
    private final JTextField txtLibro = new JTextField(5);
    private final JTextField txtPrestamo = new JTextField(5);

    private final PrestamoDAO dao = new PrestamoDAO();

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                    new String[]{"ID", "Estudiante", "Libro",
                            "Fecha préstamo", "Vencimiento", "Devuelto"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };

    private final JTable tabla = new JTable(modelo);

    public VentanaPrestamos() {
        setTitle("Biblioteca Escolar - Préstamos y Devoluciones");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new FlowLayout());
        formulario.add(new JLabel("ID estudiante:"));
        formulario.add(txtEstudiante);
        formulario.add(new JLabel("ID libro:"));
        formulario.add(txtLibro);

        JButton btnPrestar = new JButton("Registrar préstamo");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnDevolver = new JButton("Registrar devolución");

        JPanel acciones = new JPanel(new FlowLayout());
        acciones.add(btnPrestar);
        acciones.add(new JLabel("ID préstamo:"));
        acciones.add(txtPrestamo);
        acciones.add(btnDevolver);
        acciones.add(btnActualizar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);

        btnPrestar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());
        btnActualizar.addActionListener(e -> cargarPrestamos());

        cargarPrestamos();
    }

    private void registrarPrestamo() {
        try {
            int idEstudiante =
                    Integer.parseInt(txtEstudiante.getText().trim());
            int idLibro =
                    Integer.parseInt(txtLibro.getText().trim());

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            new SwingWorker<Void, Void>() {
                private String mensaje;

                @Override
                protected Void doInBackground() throws Exception {
                    dao.registrarPrestamo(idEstudiante, idLibro);
                    return null;
                }

                @Override
                protected void done() {
                    setCursor(Cursor.getDefaultCursor());

                    try {
                        get();
                        mensaje = "Préstamo registrado. Vence en 7 días.";
                        JOptionPane.showMessageDialog(
                                VentanaPrestamos.this, mensaje);
                        cargarPrestamos();
                    } catch (Exception ex) {
                        Throwable causa = ex.getCause();
                        JOptionPane.showMessageDialog(
                                VentanaPrestamos.this,
                                causa != null ? causa.getMessage()
                                        : ex.getMessage(),
                                "No se pudo registrar",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa IDs numéricos válidos.");
        }
    }

    private void registrarDevolucion() {
        try {
            int id = Integer.parseInt(txtPrestamo.getText().trim());

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    dao.registrarDevolucion(id);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(
                                VentanaPrestamos.this,
                                "Devolución registrada.");
                        cargarPrestamos();
                    } catch (Exception ex) {
                        Throwable causa = ex.getCause();
                        JOptionPane.showMessageDialog(
                                VentanaPrestamos.this,
                                causa != null ? causa.getMessage()
                                        : ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un ID de préstamo válido.");
        }
    }

    private void cargarPrestamos() {
        String sql = "SELECT p.id, e.nombre AS estudiante, " +
                "l.titulo AS libro, p.fecha_prestamo, " +
                "p.fecha_devolucion, p.devuelto " +
                "FROM prestamos p " +
                "JOIN estudiantes e ON p.id_estudiante=e.id " +
                "JOIN libros l ON p.id_libro=l.id " +
                "ORDER BY p.id DESC";

        try (Connection con = DatabaseConnection.getInstance()
                .getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            modelo.setRowCount(0);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("estudiante"),
                        rs.getString("libro"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion"),
                        rs.getBoolean("devuelto") ? "Sí" : "No"
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Error al consultar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VentanaPrestamos().setVisible(true));
    }
}
