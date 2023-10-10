import java.io.Serializable;
import java.util.Date;
// Clase para representar un Funko
class Funko implements Serializable {
    private String CODIGO;
    private String NOMBRE;
    private String MODELO;
    private double PRECIO;
    private Date FECHA_LANZAMIENTO;

    // Constructor
    public Funko(String CODIGO, String NOMBRE, String MODELO, double PRECIO, Date FECHA_LANZAMIENTO) {
        this.CODIGO = CODIGO;
        this.NOMBRE = NOMBRE;
        this.MODELO = MODELO;
        this.PRECIO = PRECIO;
        this.FECHA_LANZAMIENTO = FECHA_LANZAMIENTO;
    }

    // Getters

    public String getCODIGO() {
        return CODIGO;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public String getMODELO() {
        return MODELO;
    }

    public double getPRECIO() {
        return PRECIO;
    }

    public Date getFECHA_LANZAMIENTO() {
        return FECHA_LANZAMIENTO;
    }

    @Override
    public String toString() {
        return "Funko{" +
                "CODIGO=" + CODIGO +
                ", NOMBRE='" + NOMBRE + '\'' +
                ", MODELO='" + MODELO + '\'' +
                ", PRECIO=" + PRECIO +
                ", FECHA_LANZAMIENTO=" + FECHA_LANZAMIENTO +
                '}';
    }
}
