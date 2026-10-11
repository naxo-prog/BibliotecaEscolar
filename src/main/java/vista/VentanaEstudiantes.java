
package vista;

import dao.EstudianteDAO;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class VentanaEstudiantes extends JFrame {

    private final EstudianteDAO dao = new EstudianteDAO();

    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtRut = new JTextField();
    private final JTextField txtCurso = new JTextField();
    private final JTextField txtCorreo = new JTextField();

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                    new String[]{"ID", "Nombre", "RUT", "Curso", "Correo"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };

    private final JTable tabla = new JTable(modelo);

    public VentanaEstudiantes() {
        setTitle("Biblioteca Escolar - Gestión de Estudiantes");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Datos del estudiante"));

        formulario.add(new JLabel("ID (modificar/eliminar):"));
        formulario.add(txtId);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("RUT:"));
        formulario.add(txtRut);
        formulario.add(new JLabel("Curso:"));
        formulario.add(txtCurso);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnLimpiar);

        setLayout(new BorderLayout(8, 8));
        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() >= 0) {

                int fila = tabla.getSelectedRow();

                txtId.setText(modelo.getValueAt(fila, 0).toString());
                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                txtRut.setText(modelo.getValueAt(fila, 2).toString());
                txtCurso.setText(modelo.getValueAt(fila, 3).toString());
                txtCorreo.setText(modelo.getValueAt(fila, 4).toString());
            }
        });

        btnAgregar.addActionListener(e -> agregar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargarEstudiantes());
        btnLimpiar.addActionListener(e -> limpiar());

        cargarEstudiantes();
    }

    private void cargarEstudiantes() {
        try {
            modelo.setRowCount(0);

            for (Estudiante estudiante : dao.listar()) {
                modelo.addRow(new Object[]{
                        estudiante.getId(),
                        estudiante.getNombre(),
                        estudiante.getRut(),
                        estudiante.getCurso(),
                        estudiante.getCorreo()
                });
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private Estudiante leerFormulario() {
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || rut.isEmpty()
                || curso.isEmpty() || correo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Completa todos los campos.");
        }

        if (!correo.contains("@")) {
            throw new IllegalArgumentException(
                    "Ingresa un correo válido.");
        }

        return new Estudiante(0, nombre, rut, curso, correo);
    }

    private void agregar() {
        try {
            if (dao.agregar(leerFormulario())) {
                JOptionPane.showMessageDialog(this,
                        "Estudiante registrado correctamente.");
                limpiar();
                cargarEstudiantes();
            }
        } catch (SQLException | IllegalArgumentException ex) {
            mostrarError(ex);
        }
    }

    private void modificar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            Estudiante estudiante = leerFormulario();
            estudiante.setId(id);

            if (dao.modificar(estudiante)) {
                JOptionPane.showMessageDialog(this,
                        "Estudiante actualizado correctamente.");
                limpiar();
                cargarEstudiantes();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el estudiante.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Eliminar el estudiante seleccionado?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                if (dao.eliminar(id)) {
                    JOptionPane.showMessageDialog(this,
                            "Estudiante eliminado correctamente.");
                    limpiar();
                    cargarEstudiantes();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el estudiante.");
                }
            }
        } catch (SQLException | IllegalArgumentException ex) {
            mostrarError(ex);
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
        tabla.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VentanaEstudiantes().setVisible(true));
    }
}
