package com.mycompany.trabajo_calculo_final.metodos;

public class ReglaTrapecio implements MetodoIntegracion {
    
    public double integrar(Funcion f, double a, double b, int n) {
        // Calcular el ancho de cada subintervalo
        double deltaX = (b - a) / n;
        double suma = f.evaluar(a) + f.evaluar(b); // Evaluar los extremos

        for (int i = 1; i < n; i++) {
            double x = a + i * deltaX;
            suma += 2 * f.evaluar(x); // Evaluar los puntos intermedios
        }
        return (deltaX / 2) * suma; // Aplicar la fórmula del trapecio
        //Se usa la formula simplificada (deltaX / 2) * (f(a) + 2 * f(x1) + 2 * f(x2) + ... + 2 * f(xn-1) + f(b))
    }

    
    public String getNombre() {
        return "Regla del Trapecio";
    }

    //ToString para mostrar el nombre del método en la interfaz gráfica
    @Override
    public String toString() {
        return getNombre();
    }
    
}
