package vista;

import control.ControlRetiro;
import dtos.EstadoCuentaDTO;
import dtos.ReciboDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import negocio.IModeloRetiro;

public class FrmRetiro extends JFrame implements IObservador {

    private static final NumberFormat MONEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));

    private final ControlRetiro control;
    private final IModeloRetiro modelo;
    private final JList<Double> lstMontos;
    private final JPanel pnlDerecho;
    private final JButton btnRetirar;

    private final DefaultListModel<Double> modeloLista = new DefaultListModel<>();
    private boolean actualizandoLista = false;

    public FrmRetiro(ControlRetiro control, IModeloRetiro modelo) {

        //setup
        super("Cajero Automático - Retirar efectivo");
        this.control = control;
        this.modelo = modelo;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 470);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblTitulo = new JLabel("Retirar efectivo", SwingConstants.LEFT);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));
        add(lblTitulo, BorderLayout.NORTH);

        //montos
        lstMontos = new JList<>(modeloLista);
        lstMontos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstMontos.setFont(lstMontos.getFont().deriveFont(16f));
        lstMontos.setFixedCellHeight(30);
        lstMontos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(MONEDA.format((Double) value));
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });
        lstMontos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !actualizandoLista && lstMontos.getSelectedValue() != null) {
                try {
                    control.seleccionarMonto(lstMontos.getSelectedValue());
                } catch (IllegalStateException ex) {
                    mostrarError(ex.getMessage());
                }
            }
        });
        JScrollPane scroll = new JScrollPane(lstMontos);
        scroll.setPreferredSize(new Dimension(220, 0));
        scroll.setBorder(BorderFactory.createTitledBorder("Cantidades disponibles"));
        add(scroll, BorderLayout.WEST);


        pnlDerecho = new JPanel(new GridBagLayout());
        pnlDerecho.setBackground(Color.WHITE);
        btnRetirar = new JButton("Retirar efectivo");
        btnRetirar.setFont(btnRetirar.getFont().deriveFont(Font.BOLD, 15f));
        btnRetirar.setEnabled(false);
        btnRetirar.addActionListener(e -> {
            try {
                control.retirarEfectivo();
            } catch (IllegalStateException ex) {
                mostrarError(ex.getMessage());
            }
        });
        JPanel zonaDerecha = new JPanel(new BorderLayout(0, 10));
        zonaDerecha.add(pnlDerecho, BorderLayout.CENTER);
        zonaDerecha.add(btnRetirar, BorderLayout.SOUTH);
        add(zonaDerecha, BorderLayout.CENTER);
        mostrarMensaje("Selecciona una cantidad de la lista");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                control.iniciar();
            }
        });
    }

    @Override
    public void actualizar() {
        ReciboDTO recibo = modelo.getRecibo();
        if (recibo != null) {
            mostrarRecibo(recibo);
            return;
        }
        EstadoCuentaDTO estado = modelo.getEstadoCuenta();
        if (estado != null) {
            mostrarEstadoCuenta(estado);
            return;
        }
        mostrarMontos(modelo.getMontosDisponibles());
    }

    public void mostrarMontos(List<Double> montos) {
        actualizandoLista = true;
        modeloLista.clear();
        for (Double monto : montos) {
            modeloLista.addElement(monto);
        }
        actualizandoLista = false;
        if (montos.isEmpty()) {
            mostrarMensaje("Saldo insuficiente para cualquier retiro");
        }
    }

    public void mostrarEstadoCuenta(EstadoCuentaDTO estado) {
        String[][] filas = {
            {"Nombre:", estado.getNombre()},
            {"Monto actual:", MONEDA.format(estado.getSaldoActual())},
            {"Cantidad a retirar:", MONEDA.format(estado.getMontoRetiro())},
            {"Comisión por retiro:", MONEDA.format(estado.getComision())},
            {"Monto final:", MONEDA.format(estado.getSaldoFinal())}
        };
        pintarTabla("Estado de cuenta", filas);
        btnRetirar.setEnabled(true);
    }

    public void mostrarRecibo(ReciboDTO recibo) {
        String[][] filas = {
            {"Cuenta:", "**** " + recibo.getUltimos4()},
            {"Nombre:", recibo.getNombre()},
            {"Cantidad retirada:", MONEDA.format(recibo.getMontoRetirado())},
            {"Cobro por comisión:", MONEDA.format(recibo.getComision())}
        };
        pintarTabla("Recibo de retiro", filas);
        btnRetirar.setEnabled(false);
        lstMontos.setEnabled(false);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Retirar efectivo", JOptionPane.WARNING_MESSAGE);
    }



    //dibujasion

    private void mostrarMensaje(String texto) {
        pnlDerecho.removeAll();
        pnlDerecho.setBorder(BorderFactory.createTitledBorder("Estado de cuenta"));
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.GRAY);
        pnlDerecho.add(lbl);
        refrescar();
    }

    private void pintarTabla(String titulo, String[][] filas) {
        pnlDerecho.removeAll();
        pnlDerecho.setBorder(BorderFactory.createTitledBorder(titulo));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        for (int i = 0; i < filas.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            JLabel etiqueta = new JLabel(filas[i][0]);
            etiqueta.setFont(etiqueta.getFont().deriveFont(15f));
            pnlDerecho.add(etiqueta, gbc);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_END;
            JLabel valor = new JLabel(filas[i][1]);
            valor.setFont(valor.getFont().deriveFont(Font.BOLD, 15f));
            pnlDerecho.add(valor, gbc);
        }
        refrescar();
    }

    private void refrescar() {
        pnlDerecho.revalidate();
        pnlDerecho.repaint();
    }
}
