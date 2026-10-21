package control;

import negocio.IModeloRetiro;


public class ControlRetiro {

    private final IModeloRetiro modelo;

    public ControlRetiro(IModeloRetiro modelo) {
        this.modelo = modelo;
    }

    public void iniciar() {
        modelo.cargarMontosDisponibles();
    }

    public void seleccionarMonto(double monto) {
        modelo.seleccionarMonto(monto);
    }

    public void retirarEfectivo() {
        modelo.retirar();
    }
}
