package com.mycompany.trabajo_calculo_final.metodos;

public class ReglaTrapecio implements MetodoIntegracion {
    
    public double integrar(Funcion f, double a, double b, int n) {
        double deltaX = (b - a) / n;
        double suma = f.evaluar(a) + f.evaluar(b); // Evaluar los extremos

        for (int i = 1; i < n; i++) {
            double x = a + i * deltaX;
            suma += 2 * f.evaluar(x); // Evaluar los puntos intermedios
        }
        return (deltaX / 2) * suma; // Aplicar la fórmula del trapecio
    }

    
    public String getNombre() {
        return "Regla del Trapecio";
    }
    public String toString() {
        return getNombre();
    }
    
}
