package com.mycompany.trabajo_calculo_final.lienzo;
//Trabajo realizado por: Kenyi y Sebastian
import javax.swing.*;

import com.mycompany.trabajo_calculo_final.metodos.MetodoIntegracion;
import com.mycompany.trabajo_calculo_final.metodos.Funcion;

import java.awt.*;

public class PanelGrafico extends JPanel {

    public void setParametros(Funcion f, MetodoIntegracion metodo, double c, double d) {
        // Aquí puedes almacenar los parámetros y utilizarlos en el método paintComponent
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Aquí puedes dibujar en el panel utilizando los parámetros almacenados
    }
}
