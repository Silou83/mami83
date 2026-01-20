/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.composants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

/**
 * Icône de spinner animé pour indiquer un chargement en cours.
 * Design moderne avec des arcs qui tournent.
 *
 * @author Association MAMI
 */
public class SpinnerIcon implements Icon {

    private final int size;
    private final Color primaryColor;
    private final Color secondaryColor;
    private final Timer animationTimer;
    
    private double angle = 0;
    private boolean running = false;
    private JComponent component;

    /**
     * Constructeur avec taille et couleur.
     *
     * @param size la taille de l'icône (largeur et hauteur)
     * @param color la couleur principale
     */
    public SpinnerIcon(int size, Color color) {
        this.size = size;
        this.primaryColor = color;
        this.secondaryColor = new Color(
                color.getRed(), 
                color.getGreen(), 
                color.getBlue(), 
                60
        );
        
        // Timer pour l'animation (60 FPS)
        animationTimer = new Timer(16, e -> {
            angle += 8;
            if (angle >= 360) {
                angle = 0;
            }
            if (component != null) {
                component.repaint();
            }
        });
    }

    /**
     * Constructeur avec taille par défaut.
     *
     * @param color la couleur principale
     */
    public SpinnerIcon(Color color) {
        this(24, color);
    }

    /**
     * Constructeur par défaut.
     */
    public SpinnerIcon() {
        this(24, new Color(70, 130, 180));
    }

    /**
     * Démarre l'animation du spinner.
     */
    public void start() {
        if (!running) {
            running = true;
            animationTimer.start();
        }
    }

    /**
     * Arrête l'animation du spinner.
     */
    public void stop() {
        running = false;
        animationTimer.stop();
        angle = 0;
    }

    /**
     * @return true si le spinner est en cours d'animation
     */
    public boolean isRunning() {
        return running;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        this.component = (JComponent) c;
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Activation de l'antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int radius = (size - 4) / 2;
        float strokeWidth = size / 8f;

        // Dessiner le cercle de fond (couleur secondaire)
        g2d.setColor(secondaryColor);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(new Ellipse2D.Double(
                centerX - radius,
                centerY - radius,
                radius * 2,
                radius * 2
        ));

        // Dessiner l'arc animé (couleur principale)
        g2d.setColor(primaryColor);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        Arc2D arc = new Arc2D.Double(
                centerX - radius,
                centerY - radius,
                radius * 2,
                radius * 2,
                angle,
                90,  // Longueur de l'arc (90 degrés)
                Arc2D.OPEN
        );
        g2d.draw(arc);

        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}
