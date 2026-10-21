package negocio;

public class PoliticaComision {

    private static final double PORCENTAJE = 0.02;

    public double calcular(double monto) {
        return Math.round(monto * PORCENTAJE * 100.0) / 100.0;
    }
}
