package com.mycompany.trabajo_calculo_final.metodos;

public class RiemannIzquierda implements MetodoIntegracion {
    
    public double integrar(Funcion f, double a, double b, int n) {
        double deltaX = (b - a) / n;
        double suma = 0.0;

        for (int i = 0; i < n; i++) {
            double x = a + i * deltaX;
            suma += f.evaluar(x) * deltaX;
        }

        return suma;
    }

    
    public String getNombre() {
        return "Suma de Riemann (Izquierda)";
    }
    public String toString() {
        return getNombre();
    }
}
