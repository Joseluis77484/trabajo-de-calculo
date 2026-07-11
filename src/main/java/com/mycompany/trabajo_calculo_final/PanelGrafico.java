package com.mycompany.trabajo_calculo_final;
import javax.swing.*;
public class PanelGrafico extends JPanel {
    private Funcion funcion;
    private MetodoIntegracion metodo;
    private double a, b;
    private int n;

    public void setParametros(Funcion f, MetodoIntegracion m, double a, double b, int n) {
        this.funcion = f;
        this.metodo = m;
        this.a = a;
        this.b = b;
        this.n = n;
    }
    



}