package negocio;

import dtos.EstadoCuentaDTO;
import dtos.ReciboDTO;
import java.util.List;
import vista.IObservador;

public interface IModeloRetiro {

    void agregarObservador(IObservador observador);

    void cargarMontosDisponibles();

    void seleccionarMonto(double monto);

    void retirar();

    List<Double> getMontosDisponibles();

    EstadoCuentaDTO getEstadoCuenta();

    ReciboDTO getRecibo();
}
