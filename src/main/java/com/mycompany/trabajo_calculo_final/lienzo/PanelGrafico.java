package com.mycompany.trabajo_calculo_final.lienzo;

//Trabajo realizado por: Kenyi y Sebastian
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

import com.mycompany.trabajo_calculo_final.metodos.Funcion;
import com.mycompany.trabajo_calculo_final.metodos.MetodoIntegracion;
import com.mycompany.trabajo_calculo_final.metodos.ReglaTrapecio;
import com.mycompany.trabajo_calculo_final.metodos.RiemannIzquierda;

public class PanelGrafico extends JPanel {

    private Funcion funcion;
    private MetodoIntegracion metodo;
    private double a, b;
    private int n;

    // Firma cambiada: necesitamos la función y el método, no 4 doubles genéricos
    public void setParametros(Funcion f, MetodoIntegracion m, double a, double b, int n) {
        this.funcion = f;
        this.metodo = m;
        this.a = a;
        this.b = b;
        this.n = n;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (funcion == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto  = getHeight();

        double margenX = (b - a) * 0.15;
        double xMin    = a - margenX;
        double xMax    = b + margenX;

        double maximoY = 0;
        for (double x = a; x <= b; x += (b - a) / 100.0) {
            maximoY = Math.max(maximoY, funcion.evaluar(x));
        }
        maximoY *= 1.2;

        int margenYpx = 30;
        int margenYtop = 20;

        double escalaX = ancho / (xMax - xMin);
        double escalaY = (alto - margenYpx - margenYtop) / maximoY;

        int origenX = (int)((0 - xMin) * escalaX);
        int origenY = alto - margenYpx;

        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, origenY, ancho, origenY);
        g2d.drawLine(origenX, 0, origenX, alto);

        double deltaX = (b - a) / n;
        g2d.setColor(new Color(100, 150, 255, 120));

        for (int i = 0; i < n; i++) {
            double xIzq = a + i * deltaX;
            double xDer = a + (i + 1) * deltaX;

            int pxIzq = (int)((xIzq - xMin) * escalaX);
            int pxDer = (int)((xDer - xMin) * escalaX);
            int pAncho = pxDer - pxIzq;

            if (metodo instanceof RiemannIzquierda) {
                double y    = funcion.evaluar(xIzq);
                int    pAlto = (int)(y * escalaY);
                g2d.fillRect(pxIzq, origenY - pAlto, pAncho, pAlto);
                g2d.setColor(Color.BLUE);
                g2d.drawRect(pxIzq, origenY - pAlto, pAncho, pAlto);
                g2d.setColor(new Color(100, 150, 255, 120));
            } else if (metodo instanceof ReglaTrapecio) {
                double yIzq = funcion.evaluar(xIzq);
                double yDer = funcion.evaluar(xDer);
                int pyIzq   = origenY - (int)(yIzq * escalaY);
                int pyDer   = origenY - (int)(yDer * escalaY);
                int[] xPoints = {pxIzq, pxDer, pxDer, pxIzq};
                int[] yPoints = {origenY, origenY, pyDer, pyIzq};
                g2d.fillPolygon(xPoints, yPoints, 4);
                g2d.setColor(Color.BLUE);
                g2d.drawPolygon(xPoints, yPoints, 4);
                g2d.setColor(new Color(100, 150, 255, 120));
            }
        }

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        Path2D curva = new Path2D.Double();
        boolean primerPunto = true;

        for (double x = a; x <= b; x += (b - a) / 200.0) {
            double y  = funcion.evaluar(x);
            int px = (int)((x - xMin) * escalaX);
            int py = origenY - (int)(y * escalaY);
            if (primerPunto) { curva.moveTo(px, py); primerPunto = false; }
            else             { curva.lineTo(px, py); }
        }
        g2d.draw(curva);
    }
}