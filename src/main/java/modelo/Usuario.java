
package modelo;

public abstract class Usuario {

    private String nombre;
    private String rut;
    private String correo;

    public Usuario(String nombre, String rut, String correo) {
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
    }

    public abstract String getTipoUsuario();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
