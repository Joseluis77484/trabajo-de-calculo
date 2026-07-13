package com.mycompany.trabajo_calculo_final;

import com.mycompany.trabajo_calculo_final.Ventana.MotorIntegracion;
import javax.swing.SwingUtilities;

public class Trabajo_Calculo_Final {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MotorIntegracion().setVisible(true);
        });
    }
}