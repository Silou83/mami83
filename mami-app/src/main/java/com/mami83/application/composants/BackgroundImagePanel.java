/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.composants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Panel avec image de fond redimensionnable.
 * Remplace JXImagePanel de SwingX pour une version moderne.
 *
 * @author Association MAMI
 */
public class BackgroundImagePanel extends JPanel {

    /**
     * Mode de rendu de l'image.
     */
    public enum Style {
        /** Centre l'image sans redimensionnement */
        CENTERED,
        /** Étire l'image pour remplir le panel */
        SCALED,
        /** Répète l'image en mosaïque */
        TILED,
        /** Adapte l'image en conservant les proportions */
        SCALED_KEEP_ASPECT_RATIO
    }

    private BufferedImage backgroundImage;
    private Style style = Style.SCALED;
    private float alpha = 1.0f;

    /**
     * Constructeur par défaut.
     */
    public BackgroundImagePanel() {
        setOpaque(false);
    }

    /**
     * Constructeur avec layout.
     *
     * @param layout le gestionnaire de layout
     */
    public BackgroundImagePanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    /**
     * Charge une image depuis les ressources.
     * Si l'image n'est pas trouvée, génère une image de fond par défaut.
     *
     * @param resourcePath le chemin de la ressource
     */
    public void loadImage(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                backgroundImage = ImageIO.read(is);
                repaint();
            } else {
                System.err.println("Image non trouvée: " + resourcePath + " - Utilisation du fond par défaut");
                backgroundImage = createDefaultBackground();
                repaint();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
            backgroundImage = createDefaultBackground();
            repaint();
        }
    }

    /**
     * Crée une image de fond par défaut avec un dégradé.
     *
     * @return l'image de fond générée
     */
    private BufferedImage createDefaultBackground() {
        int width = 1920;
        int height = 1080;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Configuration pour un rendu de qualité
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Créer un dégradé doux
        Color topColor = new Color(240, 248, 255);    // AliceBlue
        Color bottomColor = new Color(176, 196, 222); // LightSteelBlue
        
        GradientPaint gradient = new GradientPaint(
                0, 0, topColor,
                0, height, bottomColor
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();
        return image;
    }

    /**
     * Définit l'image de fond.
     *
     * @param image l'image à afficher
     */
    public void setBackgroundImage(BufferedImage image) {
        this.backgroundImage = image;
        repaint();
    }

    /**
     * @return l'image de fond actuelle
     */
    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Définit le style de rendu.
     *
     * @param style le style de rendu
     */
    public void setStyle(Style style) {
        this.style = style;
        repaint();
    }

    /**
     * @return le style de rendu actuel
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Définit la transparence de l'image.
     *
     * @param alpha valeur entre 0.0 (transparent) et 1.0 (opaque)
     */
    public void setAlpha(float alpha) {
        this.alpha = Math.max(0.0f, Math.min(1.0f, alpha));
        repaint();
    }

    /**
     * @return la transparence actuelle
     */
    public float getAlpha() {
        return alpha;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        
        // Active l'antialiasing et le rendu de qualité
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
                             RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);

        // Applique la transparence
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = backgroundImage.getWidth();
        int imgHeight = backgroundImage.getHeight();

        switch (style) {
            case CENTERED -> {
                int x = (panelWidth - imgWidth) / 2;
                int y = (panelHeight - imgHeight) / 2;
                g2d.drawImage(backgroundImage, x, y, null);
            }
            case SCALED -> {
                g2d.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, null);
            }
            case TILED -> {
                for (int x = 0; x < panelWidth; x += imgWidth) {
                    for (int y = 0; y < panelHeight; y += imgHeight) {
                        g2d.drawImage(backgroundImage, x, y, null);
                    }
                }
            }
            case SCALED_KEEP_ASPECT_RATIO -> {
                double scaleX = (double) panelWidth / imgWidth;
                double scaleY = (double) panelHeight / imgHeight;
                double scale = Math.min(scaleX, scaleY);
                
                int scaledWidth = (int) (imgWidth * scale);
                int scaledHeight = (int) (imgHeight * scale);
                int x = (panelWidth - scaledWidth) / 2;
                int y = (panelHeight - scaledHeight) / 2;
                
                g2d.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, null);
            }
        }

        g2d.dispose();
    }
}
