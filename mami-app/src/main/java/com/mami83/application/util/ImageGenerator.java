/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utilitaire pour générer des images.
 * Peut être utilisé pour créer l'image de fond par défaut.
 *
 * @author Association MAMI
 */
public class ImageGenerator {

    /**
     * Génère une image de fond avec un dégradé.
     *
     * @param width largeur de l'image
     * @param height hauteur de l'image
     * @param topColor couleur du haut
     * @param bottomColor couleur du bas
     * @return l'image générée
     */
    public static BufferedImage createGradientBackground(int width, int height,
                                                          Color topColor, Color bottomColor) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Configuration pour un rendu de qualité
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Créer le dégradé
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
     * Génère l'image de fond par défaut de l'application.
     *
     * @return l'image de fond
     */
    public static BufferedImage createDefaultBackground() {
        return createGradientBackground(
                1920, 1080,
                new Color(240, 248, 255),  // AliceBlue
                new Color(176, 196, 222)   // LightSteelBlue
        );
    }

    /**
     * Point d'entrée pour générer l'image de fond.
     * Exécuter avec: java ImageGenerator [chemin_sortie]
     */
    public static void main(String[] args) {
        String outputPath = args.length > 0 ? args[0] : "fond.png";

        BufferedImage background = createDefaultBackground();

        try {
            ImageIO.write(background, "PNG", new File(outputPath));
            System.out.println("Image générée: " + outputPath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }
}
