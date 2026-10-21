package dtos;


public class ReciboDTO {

    private final String ultimos4;
    private final String nombre;
    private final double montoRetirado;
    private final double comision;

    public ReciboDTO(String ultimos4, String nombre, double montoRetirado, double comision) {
        this.ultimos4 = ultimos4;
        this.nombre = nombre;
        this.montoRetirado = montoRetirado;
        this.comision = comision;
    }

    public String getUltimos4() {
        return ultimos4;
    }

    public String getNombre() {
        return nombre;
    }

    public double getMontoRetirado() {
        return montoRetirado;
    }

    public double getComision() {
        return comision;
    }
}
