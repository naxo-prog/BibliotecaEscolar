
package modelo;

public class Estudiante extends Usuario implements Identificable {

    private int id;
    private String curso;

    public Estudiante() {
        super("", "", "");
    }

    public Estudiante(int id, String nombre, String rut,
                      String curso, String correo) {
        super(nombre, rut, correo);
        this.id = id;
        this.curso = curso;
    }

    @Override
    public String getTipoUsuario() {
        return "Estudiante";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
