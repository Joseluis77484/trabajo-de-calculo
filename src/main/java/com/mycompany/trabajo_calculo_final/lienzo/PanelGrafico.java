package com.mycompany.trabajo_calculo_final.lienzo;
//Trabajo realizado por: Kenyi y Sebastian
import javax.swing.*;

import com.mycompany.trabajo_calculo_final.metodos.*;

import java.awt.*;
import java.awt.geom.Path2D;

public class PanelGrafico extends JPanel {

    private Funcion funcion;
    private MetodoIntegracion metodo;
    private double a, b;
    private int n; 

    // Constantes de diseño: nada de "números mágicos" sueltos en el código
    private static final int MARGEN_Y_INFERIOR_PX = 30;
    private static final int MARGEN_Y_SUPERIOR_PX = 20;
    private static final double MARGEN_X_PORCENTAJE = 0.15; // 15% a cada lado del intervalo [a,b]
    private static final double FACTOR_HOLGURA_Y = 1.2;     // 20% de espacio extra arriba de la curva
    private static final Color COLOR_RELLENO = new Color(100, 150, 255, 120);

    public void setParametros(Funcion f, MetodoIntegracion m, double a, double b, int n) {
        this.funcion = f;
        this.metodo = m;
        this.a = a;
        this.b = b;
        this.n = n;
        repaint();
    }

    /**
     * Agrupa toda la información necesaria para convertir coordenadas
     * matemáticas (x, y) en coordenadas de píxeles en el panel.
     * Antes esto eran 6 variables sueltas dentro de paintComponent.
     */
    private static class Escala {
        double xMin, xMax;
        double escalaX, escalaY;
        int origenX, origenY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Llamar al método de la superclase para limpiar el panel antes de dibujar
        super.paintComponent(g);
        if (funcion == null) return;
        // Activar el antialiasing para que las líneas se vean suaves
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Calcular la escala y los parámetros de conversión de coordenadas
        Escala escala = calcularEscala();
        // Dibujar los ejes en negro
        dibujarEjes(g2d, escala);
        // Dibujar la aproximación (rectángulos o trapecios) en azul con relleno semitransparente
        dibujarAproximacion(g2d, escala);
        // Dibujar la curva real de la función en rojo
        dibujarCurvaReal(g2d, escala);
    }

    // ---------------------------------------------------------
    // Cálculo de escalas y conversión de coordenadas
    // ---------------------------------------------------------

    private Escala calcularEscala() {
        Escala e = new Escala();
        
        // Calcular los márgenes en X para que la gráfica no quede pegada a los bordes
        double margenX = (b - a) * MARGEN_X_PORCENTAJE;
        e.xMin = a - margenX;
        e.xMax = b + margenX;

        // Calcular el valor máximo de la función en [a, b] para determinar la escala en Y
        double maximoY = calcularMaximoY() * FACTOR_HOLGURA_Y;

        int ancho = getWidth();
        int alto = getHeight();

        // Calcular las escalas en X y Y para convertir coordenadas matemáticas a píxeles
        e.escalaX = ancho / (e.xMax - e.xMin);
        e.escalaY = (alto - MARGEN_Y_INFERIOR_PX - MARGEN_Y_SUPERIOR_PX) / maximoY;

        // El origen en píxeles se coloca en la parte inferior del panel, dejando un margen inferior
        e.origenX = (int) ((0 - e.xMin) * e.escalaX);
        e.origenY = alto - MARGEN_Y_INFERIOR_PX;

        return e;
    }

    /** Busca el valor máximo que alcanza la función en [a, b], muestreando 100 puntos. */
    private double calcularMaximoY() {
        double maximoY = 0;
        //El bucle recorre 100 puntos equidistantes entre a y b, evaluando la función en cada punto y actualizando el valor máximo encontrado.
        for (double x = a; x <= b; x += ((b - a) / 100.0)) {
            maximoY = Math.max(maximoY, funcion.evaluar(x));
        }
        return maximoY;
    }

    // Convierte una coordenada X matemática a coordenada de píxeles en el panel
    private int convertirX(double x, Escala e) {
        return (int) ((x - e.xMin) * e.escalaX);
    }

    // Convierte una coordenada Y matemática a coordenada de píxeles en el panel
    private int convertirY(double y, Escala e) {
        return e.origenY - (int) (y * e.escalaY);
    }

    // ---------------------------------------------------------
    // Dibujo
    // ---------------------------------------------------------

    // Dibuja los ejes X e Y en negro, usando la escala calculada
    private void dibujarEjes(Graphics2D g2d, Escala e) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, e.origenY, getWidth(), e.origenY);
        g2d.drawLine(e.origenX, 0, e.origenX, getHeight());
    }

    /** Decide, subintervalo por subintervalo, si dibujar rectángulo o trapecio. */
    private void dibujarAproximacion(Graphics2D g2d, Escala e) {
        double deltaX = (b - a) / n;

        for (int i = 0; i < n; i++) {
            double xIzq = a + i * deltaX;
            double xDer = a + (i + 1) * deltaX;

            if (metodo instanceof RiemannIzquierda) {
                dibujarRectangulo(g2d, e, xIzq, xDer);
            } else if (metodo instanceof ReglaTrapecio) {
                dibujarTrapecio(g2d, e, xIzq, xDer);
            }
        }
    }
    
    //Dibuja un rectángulo en el panel, usando la escala y coordenadas proporcionadas
    private void dibujarRectangulo(Graphics2D g2d, Escala e, double xIzq, double xDer) {
        int pxIzq = convertirX(xIzq, e);
        int pxDer = convertirX(xDer, e);
        int pAncho = pxDer - pxIzq;

        double y = funcion.evaluar(xIzq); // Riemann izquierda: altura tomada del extremo izquierdo
        int pAlto = (int) (y * e.escalaY);

        g2d.setColor(COLOR_RELLENO);
        g2d.fillRect(pxIzq, e.origenY - pAlto, pAncho, pAlto);
        g2d.setColor(Color.BLUE);
        g2d.drawRect(pxIzq, e.origenY - pAlto, pAncho, pAlto);
    }

    //Dibuja un trapecio en el panel, usando la escala y coordenadas proporcionadas
    private void dibujarTrapecio(Graphics2D g2d, Escala e, double xIzq, double xDer) {
        int pxIzq = convertirX(xIzq, e);
        int pxDer = convertirX(xDer, e);

        double yIzq = funcion.evaluar(xIzq);
        double yDer = funcion.evaluar(xDer);
        int pyIzq = convertirY(yIzq, e);
        int pyDer = convertirY(yDer, e);

        int[] xPoints = {pxIzq, pxDer, pxDer, pxIzq};
        int[] yPoints = {e.origenY, e.origenY, pyDer, pyIzq};

        g2d.setColor(COLOR_RELLENO);
        g2d.fillPolygon(xPoints, yPoints, 4);
        g2d.setColor(Color.BLUE);
        g2d.drawPolygon(xPoints, yPoints, 4);
    }

    /** Dibuja la curva exacta en rojo, muestreando 200 puntos para que se vea suave. */
    private void dibujarCurvaReal(Graphics2D g2d, Escala e) {
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        Path2D curva = new Path2D.Double();
        boolean primerPunto = true;

        for (double x = a; x <= b; x += (b - a) / 200.0) {
            double y = funcion.evaluar(x);
            int px = convertirX(x, e);
            int py = convertirY(y, e);

            if (primerPunto) {
                curva.moveTo(px, py);
                primerPunto = false;
            } else {
                curva.lineTo(px, py);
            }
        }
        g2d.draw(curva);
    }
}