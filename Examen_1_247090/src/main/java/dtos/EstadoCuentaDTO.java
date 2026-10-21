package dtos;

public class EstadoCuentaDTO {

    private final String nombre;
    private final double saldoActual;
    private final double montoRetiro;
    private final double comision;
    private final double saldoFinal;

    public EstadoCuentaDTO(String nombre, double saldoActual, double montoRetiro, double comision, double saldoFinal) {
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.montoRetiro = montoRetiro;
        this.comision = comision;
        this.saldoFinal = saldoFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public double getMontoRetiro() {
        return montoRetiro;
    }

    public double getComision() {
        return comision;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }
}
