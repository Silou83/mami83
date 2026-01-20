/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application;

import com.formdev.flatlaf.FlatLightLaf;
import com.mami83.application.page.ihm.AccueilConnexionPane;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée principal de l'application Mami.
 * Configure le look and feel et initialise la vue principale.
 *
 * @author Association MAMI
 */
public class MamiApp {

    private static MamiApp instance;
    private static MamiView mamiView;

    /**
     * Point d'entrée de l'application.
     */
    public static void main(String[] args) {
        // Configuration du Look and Feel FlatLaf
        try {
            FlatLightLaf.setup();
            
            // Personnalisations FlatLaf optionnelles
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du Look and Feel: " + e.getMessage());
        }

        // Lancement sur l'EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            instance = new MamiApp();
            instance.start();
        });
    }

    /**
     * @return l'instance unique de l'application
     */
    public static MamiApp getInstance() {
        return instance;
    }

    /**
     * @return la vue principale de l'application
     */
    public static MamiView getMamiView() {
        return mamiView;
    }

    /**
     * Démarre l'application.
     */
    private void start() {
        // Création de la vue principale
        mamiView = new MamiView();

        // Affichage du panel de connexion par défaut
        AccueilConnexionPane connexionPane = new AccueilConnexionPane();
        mamiView.addAndShow(connexionPane, "accueilConnexion");

        // Affichage de la fenêtre
        mamiView.show();
    }

    /**
     * Appelé après une connexion réussie.
     * Peut être utilisé pour initialiser des ressources supplémentaires.
     */
    public void connected() {
        // Afficher le bouton accueil dans la navigation
        mamiView.showNavigationButton("accueil");
        mamiView.setStatusMessage("Connecté");
    }

    /**
     * Quitte l'application proprement.
     */
    public void quit() {
        // Nettoyage si nécessaire
        System.exit(0);
    }
}
