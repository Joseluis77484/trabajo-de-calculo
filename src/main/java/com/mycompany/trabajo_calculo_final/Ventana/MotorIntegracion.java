package com.mycompany.trabajo_calculo_final.Ventana;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.mycompany.trabajo_calculo_final.lienzo.PanelGrafico;
import com.mycompany.trabajo_calculo_final.metodos.Funcion;
import com.mycompany.trabajo_calculo_final.metodos.MetodoIntegracion;
import com.mycompany.trabajo_calculo_final.metodos.ReglaTrapecio;
import com.mycompany.trabajo_calculo_final.metodos.RiemannIzquierda;

public class MotorIntegracion extends JFrame {

    private JComboBox<Funcion> comboFunciones;
    private JTextField txtA, txtB;
    private JSpinner spinN;
    private JComboBox<MetodoIntegracion> comboMetodos;
    private JLabel lblResultado;
    private PanelGrafico panelGrafico;

    public MotorIntegracion() {
        setTitle("Motor de Integración Numérica - Cálculo 2");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelControles = new JPanel(new FlowLayout());

        Funcion f1 = new Funcion() {
            public double evaluar(double x) { return x * x; }
            public String getNombre() { return "f(x) = x^2"; }
            @Override public String toString() { return getNombre(); }
        };
        Funcion f2 = new Funcion() {
            public double evaluar(double x) { return Math.sin(x) + 2; }
            public String getNombre() { return "f(x) = sin(x) + 2"; }
            @Override public String toString() { return getNombre(); }
        };

        comboFunciones = new JComboBox<>(new Funcion[]{f1, f2});
        txtA = new JTextField("0", 3);
        txtB = new JTextField("4", 3);
        spinN = new JSpinner(new SpinnerNumberModel(10, 2, 1000, 1));

        comboMetodos = new JComboBox<>(new MetodoIntegracion[]{
            new RiemannIzquierda(),
            new ReglaTrapecio()
        });

        JButton btnCalcular = new JButton("Calcular y Graficar");

        panelControles.add(new JLabel("Función:"));
        panelControles.add(comboFunciones);
        panelControles.add(new JLabel("a:"));
        panelControles.add(txtA);
        panelControles.add(new JLabel("b:"));
        panelControles.add(txtB);
        panelControles.add(new JLabel("n (Intervalos):"));
        panelControles.add(spinN);
        panelControles.add(new JLabel("Método:"));
        panelControles.add(comboMetodos);
        panelControles.add(btnCalcular);

        add(panelControles, BorderLayout.NORTH);

        panelGrafico = new PanelGrafico();
        add(panelGrafico, BorderLayout.CENTER);

        JPanel panelResultados = new JPanel();
        lblResultado = new JLabel("Área Aproximada: ---");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 16));
        panelResultados.add(lblResultado);
        add(panelResultados, BorderLayout.SOUTH);

        btnCalcular.addActionListener(e -> calcularYGraficar());
    }

    private void calcularYGraficar() {
        try {
            double a = Double.parseDouble(txtA.getText());
            double b = Double.parseDouble(txtB.getText());
            int n = (int) spinN.getValue();

            if (a >= b) {
                JOptionPane.showMessageDialog(this, "El límite 'a' debe ser menor que 'b'.");
                return;
            }

            Funcion f = (Funcion) comboFunciones.getSelectedItem();
            MetodoIntegracion metodo = (MetodoIntegracion) comboMetodos.getSelectedItem();

            double resultado = metodo.integrar(f, a, b, n);
            lblResultado.setText(String.format("Área Aproximada: %.4f", resultado));

            panelGrafico.setParametros(f, metodo, a, b, n);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en los límites.");
        }
    }
}