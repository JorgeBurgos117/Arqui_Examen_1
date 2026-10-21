package negocio;

import dtos.EstadoCuentaDTO;
import dtos.ReciboDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import vista.IObservador;

public class ModeloRetiro implements IModeloRetiro {

    private static final double MULTIPLO = 200.0;
    private static final double LIMITE_RETIRO = 6000.0;

    private final Cuenta cuenta;
    private final PoliticaComision politica;
    private final List<Double> montosDisponibles = new ArrayList<>();
    private double montoSeleccionado;
    private double comision;
    private Retiro retiro;
    private final List<IObservador> observadores = new ArrayList<>();

    public ModeloRetiro(Cuenta cuenta, PoliticaComision politica) {
        this.cuenta = cuenta;
        this.politica = politica;
    }

    @Override
    public void agregarObservador(IObservador observador) {
        observadores.add(observador);
    }

    @Override
    public void cargarMontosDisponibles() {
        double saldo = cuenta.getSaldo();
        montosDisponibles.clear();
        for (double monto = MULTIPLO; monto <= LIMITE_RETIRO; monto += MULTIPLO) {
            if (monto + politica.calcular(monto) <= saldo) {
                montosDisponibles.add(monto);
            }
        }
        montoSeleccionado = 0;
        comision = 0;
        retiro = null;
        notificar();
    }

    @Override
    public void seleccionarMonto(double monto) {
        if (retiro != null) {
            throw new IllegalStateException("El retiro ya se realizó.");
        }
        double comisionMonto = politica.calcular(monto);
        if (!cuenta.tieneFondos(monto + comisionMonto)) {
            throw new IllegalStateException("Fondos insuficientes.");
        }
        this.montoSeleccionado = monto;
        this.comision = comisionMonto;
        notificar();
    }

    @Override
    public void retirar() {
        if (montoSeleccionado <= 0) {
            throw new IllegalStateException("Selecciona un monto.");
        }
        if (retiro != null) {
            throw new IllegalStateException("El retiro ya se realizó.");
        }
        cuenta.debitar(montoSeleccionado + comision);
        retiro = new Retiro(cuenta, montoSeleccionado, comision);
        notificar();
    }

    @Override
    public List<Double> getMontosDisponibles() {
        return Collections.unmodifiableList(montosDisponibles);
    }

    @Override
    public EstadoCuentaDTO getEstadoCuenta() {
        if (montoSeleccionado <= 0) {
            return null;
        }
        Cliente titular = cuenta.getTitular();
        String nombre = titular.getNombre();
        double saldoActual = cuenta.getSaldo();
        double saldoFinal = saldoActual - montoSeleccionado - comision;
        return new EstadoCuentaDTO(nombre, saldoActual, montoSeleccionado, comision, saldoFinal);
    }

    @Override
    public ReciboDTO getRecibo() {
        return retiro == null ? null : retiro.generarRecibo();
    }

    private void notificar() {
        for (IObservador observador : observadores) {
            observador.actualizar();
        }
    }
}
