package negocio;

import dtos.ReciboDTO;
import java.time.LocalDateTime;

public class Retiro {

    private final Cuenta cuenta;
    private final double monto;
    private final double comision;
    private final LocalDateTime fecha;

    public Retiro(Cuenta cuenta, double monto, double comision) {
        this.cuenta = cuenta;
        this.monto = monto;
        this.comision = comision;
        this.fecha = LocalDateTime.now();
    }

    public double getMonto() {
        return monto;
    }

    public double getComision() {
        return comision;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public ReciboDTO generarRecibo() {
        String ultimos4 = cuenta.getUltimos4();
        Cliente titular = cuenta.getTitular();
        String nombre = titular.getNombre();
        return new ReciboDTO(ultimos4, nombre, monto, comision);
    }
}
