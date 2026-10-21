package negocio;

public class Cuenta {

    private final String numero;
    private double saldo;
    private final Cliente titular;

    public Cuenta(String numero, double saldo, Cliente titular) {
        this.numero = numero;
        this.saldo = saldo;
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public Cliente getTitular() {
        return titular;
    }

    public String getUltimos4() {
        return numero.length() <= 4 ? numero : numero.substring(numero.length() - 4);
    }

    public boolean tieneFondos(double monto) {
        return monto <= saldo;
    }

    public void debitar(double monto) {
        if (!tieneFondos(monto)) {
            throw new IllegalStateException("Fondos insuficientes para retirar " + monto);
        }
        saldo -= monto;
    }
}
