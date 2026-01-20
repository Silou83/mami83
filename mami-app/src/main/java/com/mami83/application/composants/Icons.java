/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.composants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Classe utilitaire fournissant des icônes vectorielles génériques.
 * Les icônes sont dessinées en code pour éviter les dépendances externes.
 *
 * @author Association MAMI
 */
public class Icons {

    private static final int DEFAULT_SIZE = 32;
    private static final Color DEFAULT_COLOR = new Color(70, 70, 70);

    /**
     * Crée une icône de sortie/quitter (porte avec flèche).
     */
    public static Icon exitIcon() {
        return exitIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon exitIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int m = size / 8; // marge
                int w = size - 2 * m;
                int h = size - 2 * m;
                
                // Porte (rectangle ouvert à droite)
                g2.drawLine(x + m + w/2, y + m, x + m, y + m);
                g2.drawLine(x + m, y + m, x + m, y + m + h);
                g2.drawLine(x + m, y + m + h, x + m + w/2, y + m + h);
                
                // Flèche vers la droite
                int arrowY = y + size/2;
                int arrowStartX = x + m + w/3;
                int arrowEndX = x + m + w;
                g2.drawLine(arrowStartX, arrowY, arrowEndX, arrowY);
                g2.drawLine(arrowEndX - w/4, arrowY - w/4, arrowEndX, arrowY);
                g2.drawLine(arrowEndX - w/4, arrowY + w/4, arrowEndX, arrowY);
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône de maison/accueil.
     */
    public static Icon homeIcon() {
        return homeIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon homeIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int m = size / 6;
                int cx = x + size / 2;
                int top = y + m;
                int bottom = y + size - m;
                int left = x + m;
                int right = x + size - m;
                int roofPeak = y + m;
                int roofBase = y + size / 2 - m/2;
                
                // Toit
                Path2D roof = new Path2D.Double();
                roof.moveTo(left - m/2, roofBase);
                roof.lineTo(cx, roofPeak);
                roof.lineTo(right + m/2, roofBase);
                g2.draw(roof);
                
                // Corps de la maison
                g2.drawRect(left + m/2, roofBase, right - left - m, bottom - roofBase);
                
                // Porte
                int doorW = (right - left) / 3;
                int doorH = (bottom - roofBase) / 2;
                g2.drawRect(cx - doorW/2, bottom - doorH, doorW, doorH);
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône d'enfant/personne petite.
     */
    public static Icon childIcon() {
        return childIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon childIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int cx = x + size / 2;
                int headRadius = size / 6;
                
                // Tête
                g2.draw(new Ellipse2D.Double(cx - headRadius, y + size/6, headRadius * 2, headRadius * 2));
                
                // Corps
                int bodyTop = y + size/6 + headRadius * 2;
                int bodyBottom = y + size - size/4;
                g2.drawLine(cx, bodyTop, cx, bodyBottom);
                
                // Bras
                int armY = bodyTop + (bodyBottom - bodyTop) / 3;
                g2.drawLine(cx - size/4, armY, cx + size/4, armY);
                
                // Jambes
                g2.drawLine(cx, bodyBottom, cx - size/5, y + size - size/8);
                g2.drawLine(cx, bodyBottom, cx + size/5, y + size - size/8);
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône de famille/parents.
     */
    public static Icon familyIcon() {
        return familyIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon familyIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Personne gauche (plus grande)
                int p1x = x + size / 3;
                drawPerson(g2, p1x, y + size/8, size/5);
                
                // Personne droite (plus grande)
                int p2x = x + 2 * size / 3;
                drawPerson(g2, p2x, y + size/8, size/5);
                
                // Petit au milieu (enfant)
                int childX = x + size / 2;
                drawPerson(g2, childX, y + size/3, size/7);
                
                g2.dispose();
            }
            
            private void drawPerson(Graphics2D g2, int cx, int topY, int headSize) {
                // Tête
                g2.draw(new Ellipse2D.Double(cx - headSize/2, topY, headSize, headSize));
                // Corps
                int bodyTop = topY + headSize;
                int bodyLen = headSize * 2;
                g2.drawLine(cx, bodyTop, cx, bodyTop + bodyLen);
                // Jambes
                g2.drawLine(cx, bodyTop + bodyLen, cx - headSize/2, bodyTop + bodyLen + headSize);
                g2.drawLine(cx, bodyTop + bodyLen, cx + headSize/2, bodyTop + bodyLen + headSize);
            }
            
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône de salarié/employé (personne avec cravate).
     */
    public static Icon employeeIcon() {
        return employeeIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon employeeIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int cx = x + size / 2;
                int headRadius = size / 5;
                
                // Tête
                g2.draw(new Ellipse2D.Double(cx - headRadius, y + size/8, headRadius * 2, headRadius * 2));
                
                // Épaules/Corps (rectangle arrondi)
                int bodyTop = y + size/8 + headRadius * 2 + 2;
                int bodyWidth = size / 2;
                int bodyHeight = size / 2;
                g2.draw(new RoundRectangle2D.Double(
                        cx - bodyWidth/2, bodyTop, 
                        bodyWidth, bodyHeight, 
                        8, 8
                ));
                
                // Cravate
                g2.drawLine(cx, bodyTop, cx, bodyTop + bodyHeight/2);
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône d'administration (engrenage).
     */
    public static Icon adminIcon() {
        return adminIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon adminIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int cx = x + size / 2;
                int cy = y + size / 2;
                int outerR = size / 2 - 4;
                int innerR = size / 4;
                int teeth = 8;
                
                // Engrenage
                Path2D gear = new Path2D.Double();
                for (int i = 0; i < teeth * 2; i++) {
                    double angle = Math.PI * 2 * i / (teeth * 2);
                    int r = (i % 2 == 0) ? outerR : outerR - 4;
                    double px = cx + r * Math.cos(angle);
                    double py = cy + r * Math.sin(angle);
                    if (i == 0) {
                        gear.moveTo(px, py);
                    } else {
                        gear.lineTo(px, py);
                    }
                }
                gear.closePath();
                g2.draw(gear);
                
                // Cercle intérieur
                g2.draw(new Ellipse2D.Double(cx - innerR/2, cy - innerR/2, innerR, innerR));
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône de globe/extranet.
     */
    public static Icon globeIcon() {
        return globeIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon globeIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int m = 4;
                int d = size - 2 * m;
                
                // Cercle principal
                g2.draw(new Ellipse2D.Double(x + m, y + m, d, d));
                
                // Ligne horizontale centrale
                g2.drawLine(x + m, y + size/2, x + size - m, y + size/2);
                
                // Ligne verticale centrale
                g2.drawLine(x + size/2, y + m, x + size/2, y + size - m);
                
                // Ellipse verticale (méridien)
                g2.draw(new Ellipse2D.Double(x + size/2 - d/4, y + m, d/2, d));
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône de clé/identifiants.
     */
    public static Icon keyIcon() {
        return keyIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon keyIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int headR = size / 4;
                int headCx = x + size - headR - 4;
                int headCy = y + headR + 4;
                
                // Tête de la clé (cercle)
                g2.draw(new Ellipse2D.Double(headCx - headR, headCy - headR, headR * 2, headR * 2));
                g2.draw(new Ellipse2D.Double(headCx - headR/2, headCy - headR/2, headR, headR));
                
                // Tige
                int shaftY = headCy;
                g2.drawLine(x + 4, shaftY, headCx - headR, shaftY);
                
                // Dents
                g2.drawLine(x + size/4, shaftY, x + size/4, shaftY + size/6);
                g2.drawLine(x + size/4 + 6, shaftY, x + size/4 + 6, shaftY + size/8);
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée une icône générique (carré avec points).
     */
    public static Icon genericIcon() {
        return genericIcon(DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public static Icon genericIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = createGraphics(g);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int m = 6;
                g2.draw(new RoundRectangle2D.Double(x + m, y + m, size - 2*m, size - 2*m, 6, 6));
                
                // Points
                int dotSize = 4;
                int spacing = (size - 2*m) / 3;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        g2.fill(new Ellipse2D.Double(
                                x + m + spacing + i * spacing - dotSize/2,
                                y + m + spacing + j * spacing - dotSize/2,
                                dotSize, dotSize
                        ));
                    }
                }
                
                g2.dispose();
            }
            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }

    /**
     * Crée un Graphics2D configuré pour un rendu de qualité.
     */
    private static Graphics2D createGraphics(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }
}
