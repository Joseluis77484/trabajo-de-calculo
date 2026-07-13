package Ventana;

import java.awt.*;
import javax.swing.*;
import com.mycompany.trabajo_calculo_final.metodos.*;
import com.mycompany.trabajo_calculo_final.lienzo.PanelGrafico;


public class ventanaCalculo extends JFrame{
    // Componentes de la interfaz
    private JComboBox<Funcion> comboFunciones;
    private JTextField txtA, txtB;
    private JSpinner spinN;
    private JComboBox<MetodoIntegracion> comboMetodos;
    private JLabel lblResultado;
    private PanelGrafico panelGrafico;

    public ventanaCalculo() {
        super("Motor de Integración Numérica - Cálculo 2");
        MotorIntegracion();
    }

    public void MotorIntegracion() {
        configurarVentana();

        // Funciones predefinidas
        Funcion f1 = new Funcion() {
            public double evaluar(double x) { return x * x; }
            public String getNombre() { return "f(x) = x^2"; }
            @Override public String toString() { return getNombre(); }
        };
        Funcion f2 = new Funcion() {
            public double evaluar(double x) { return Math.sin(x) + 2; } // +2 para mantenerla positiva en la gráfica
            public String getNombre() { return "f(x) = sin(x) + 2"; }
            @Override public String toString() { return getNombre(); }
        };

                crearComponentes(f1, f2);
    }

    public void configurarVentana(){
        setTitle("Motor de Integración Numérica - Cálculo 2");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public void crearComponentes(Funcion f1, Funcion f2) {
        // --- Panel de Controles (Arriba) ---
        JPanel panelControles = new JPanel(new FlowLayout());
        //Selección de función
        comboFunciones = new JComboBox<>(new Funcion[]{f1, f2});
        //Selección de límites y número de intervalos
        txtA = new JTextField("0", 3);
        txtB = new JTextField("4", 3);
        spinN = new JSpinner(new SpinnerNumberModel(10, 2, 10000, 1));
        //Selección de método de integración
        comboMetodos = new JComboBox<>(new MetodoIntegracion[]{
            new RiemannIzquierda(), 
            new ReglaTrapecio()
        });
        // Botón de cálculo
        JButton btnCalcular = new JButton("Calcular y Graficar");
        
        //Rellenar el panel de controles
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
        

        // --- Panel Gráfico (Centro) ---
        panelGrafico = new PanelGrafico();
        add(panelGrafico, BorderLayout.CENTER);

        // --- Panel de Resultados (Abajo) ---
        JPanel panelResultados = new JPanel();
        lblResultado = new JLabel("Área Aproximada: ---");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 16));
        panelResultados.add(lblResultado);
        add(panelResultados, BorderLayout.SOUTH);

        // --- Evento del Botón ---
        btnCalcular.addActionListener(e -> calcularYGraficar());
    }

    private void calcularYGraficar() {
        // Validación de entradas de los límites y número de intervalos
        double a, b;
        int n;
        try {
            a = Double.parseDouble(txtA.getText());
            b = Double.parseDouble(txtB.getText());
            n = (int) spinN.getValue();
            } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en los límites.");
            return;
        }

        // Validación de límites, el límite inferior 'a' debe ser menor que el límite superior 'b'
        if (a >= b) {
            JOptionPane.showMessageDialog(this, "El límite 'a' debe ser menor que 'b'.");
            return;
        }

        // Se obtienen la función y el método de integración seleccionados
        Funcion f = (Funcion) comboFunciones.getSelectedItem();
        MetodoIntegracion metodo = (MetodoIntegracion) comboMetodos.getSelectedItem();

        // Lógica de cálculo
        // Se calcula el área aproximada usando el método seleccionado y se actualiza la etiqueta de resultado
        double resultado = metodo.integrar(f, a, b, n);
        lblResultado.setText(String.format("Área Aproximada: %.4f", resultado));

        // Actualizar gráfica
        // Se actualizan los parámetros del panel gráfico para reflejar la función, el método y los límites seleccionados
        panelGrafico.setParametros(f, metodo, a, b, n);        
    }
}
