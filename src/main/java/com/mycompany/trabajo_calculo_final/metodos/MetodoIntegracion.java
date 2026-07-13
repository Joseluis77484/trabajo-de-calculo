package com.mycompany.trabajo_calculo_final.metodos;

public interface MetodoIntegracion {
    double integrar(Funcion f, double a, double b, int n);
    String getNombre();
}
