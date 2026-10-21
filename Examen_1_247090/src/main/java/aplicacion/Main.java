package aplicacion;

import control.ControlRetiro;
import javax.swing.SwingUtilities;
import negocio.Cliente;
import negocio.Cuenta;
import negocio.ModeloRetiro;
import negocio.PoliticaComision;
import vista.FrmRetiro;

//Arquitectura de software - Examen 1
//Jorge Alonso Burgos Maldonado, 247090

public class Main {

    public static void main(String[] args) {
        Cliente cliente = new Cliente("Fulanito Perez Lopez");
        Cuenta cuenta = new Cuenta("123456789012", 8500.00, cliente);
        PoliticaComision politica = new PoliticaComision();

        ModeloRetiro modelo = new ModeloRetiro(cuenta, politica);
        ControlRetiro control = new ControlRetiro(modelo);

        SwingUtilities.invokeLater(() -> {
            FrmRetiro vista = new FrmRetiro(control, modelo);
            modelo.agregarObservador(vista);
            vista.setVisible(true);
        });
    }
}
