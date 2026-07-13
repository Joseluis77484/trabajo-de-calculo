package com.mycompany.trabajo_calculo_final.metodos;

public class RiemannIzquierda implements MetodoIntegracion {
    
    public double integrar(Funcion f, double a, double b, int n) {
        double deltaX = (b - a) / n;
        double suma = 0.0;

        // Suma de Riemann usando el punto izquierdo de cada subintervalo
        for (int i = 0; i < n; i++) {
            // Punto izquierdo del subintervalo
            double x = a + i * deltaX;
            //Agregar el área del rectángulo correspondiente al subintervalo
            suma += f.evaluar(x) * deltaX;
        }

        return suma;
    }

    
    public String getNombre() {
        return "Suma de Riemann (Izquierda)";
    }

    //ToString para mostrar el nombre del método en la interfaz gráfica
    @Override
    public String toString() {
        return getNombre();
    }
}
