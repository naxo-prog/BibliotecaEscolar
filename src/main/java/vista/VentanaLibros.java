
package vista;

import controlador.LibroController;
import modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaLibros extends JFrame {

    private final LibroController controlador = new LibroController();

    private final JTextField txtId = new JTextField(5);
    private final JTextField txtTitulo = new JTextField(15);
    private final JTextField txtAutor = new JTextField(15);
    private final JTextField txtIsbn = new JTextField(12);
    private final JTextField txtEditorial = new JTextField(12);
    private final JTextField txtStock = new JTextField(5);
    private final JTextField txtCategoria = new JTextField("1", 5);

    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(
                    new String[]{"ID", "Título", "Autor", "ISBN",
                            "Editorial", "Stock", "Categoría"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable tabla = new JTable(modeloTabla);

    public VentanaLibros() {
        setTitle("Biblioteca Escolar - Gestión de Libros");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new GridLayout(4, 4, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Datos del libro"));

        formulario.add(new JLabel("ID (para modificar/eliminar):"));
        formulario.add(txtId);
        formulario.add(new JLabel("Título:"));
        formulario.add(txtTitulo);

        formulario.add(new JLabel("Autor:"));
        formulario.add(txtAutor);
        formulario.add(new JLabel("ISBN:"));
        formulario.add(txtIsbn);

        formulario.add(new JLabel("Editorial:"));
        formulario.add(txtEditorial);
        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);

        formulario.add(new JLabel("ID Categoría:"));
        formulario.add(txtCategoria);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar lista");
        JButton btnLimpiar = new JButton("Limpiar");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnAgregar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnLimpiar);

        setLayout(new BorderLayout(10, 10));
        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();

                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtTitulo.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtAutor.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtIsbn.setText(modeloTabla.getValueAt(fila, 3).toString());
                txtEditorial.setText(modeloTabla.getValueAt(fila, 4).toString());
                txtStock.setText(modeloTabla.getValueAt(fila, 5).toString());
                txtCategoria.setText(modeloTabla.getValueAt(fila, 6).toString());
            }
        });

        btnAgregar.addActionListener(e -> agregar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargarLibros());
        btnLimpiar.addActionListener(e -> limpiar());

        cargarLibros();
    }

    private void cargarLibros() {
        try {
            modeloTabla.setRowCount(0);

            List<Libro> libros = controlador.listarLibros();

            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getIsbn(),
                        libro.getEditorial(),
                        libro.getStock(),
                        libro.getIdCategoria()
                });
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private Libro leerFormulario() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String editorial = txtEditorial.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty()
                || isbn.isEmpty() || editorial.isEmpty()) {
            throw new IllegalArgumentException(
                    "Completa título, autor, ISBN y editorial.");
        }

        int stock = Integer.parseInt(txtStock.getText().trim());
        int categoria = Integer.parseInt(
                txtCategoria.getText().trim());

        if (stock < 0 || categoria <= 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo y la categoría debe ser positiva.");
        }

        return new Libro(0, titulo, autor, isbn,
                editorial, stock, categoria);
    }

    private void agregar() {
        try {
            Libro libro = leerFormulario();

            if (controlador.agregarLibro(libro)) {
                JOptionPane.showMessageDialog(this,
                        "Libro agregado correctamente.");
                limpiar();
                cargarLibros();
            }
        } catch (SQLException | IllegalArgumentException ex) {
            mostrarError(ex);
        }
    }

    private void modificar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            Libro libro = leerFormulario();
            libro.setId(id);

            if (controlador.modificarLibro(libro)) {
                JOptionPane.showMessageDialog(this,
                        "Libro modificado correctamente.");
                limpiar();
                cargarLibros();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el libro.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas eliminar el libro seleccionado?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (controlador.eliminarLibro(id)) {
                    JOptionPane.showMessageDialog(this,
                            "Libro eliminado correctamente.");
                    limpiar();
                    cargarLibros();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el libro.");
                }
            }
        } catch (SQLException | IllegalArgumentException ex) {
            mostrarError(ex);
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");
        txtCategoria.setText("1");
        tabla.clearSelection();
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Aviso",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VentanaLibros().setVisible(true));
    }
}
