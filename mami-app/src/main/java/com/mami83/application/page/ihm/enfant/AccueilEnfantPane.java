/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.page.ihm.enfant;

import com.mami83.application.MamiApp;
import com.mami83.application.MamiView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Panel d'accueil du module Enfants.
 * Affiche les différentes actions disponibles pour la gestion des enfants.
 *
 * @author Association MAMI
 */
public class AccueilEnfantPane extends JPanel {

    /**
     * Constructeur du panel d'accueil Enfants.
     */
    public AccueilEnfantPane() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // Panel central avec les boutons d'action
        JPanel actionPanel = createActionPanel();

        // Centrer le panel d'actions
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(actionPanel, gbc);
    }

    /**
     * Crée le panel contenant les boutons d'action du module Enfants.
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new MigLayout(
                "wrap 3, insets 20, gap 15",
                "[150!][150!][150!]",
                "[150!][150!]"
        ));
        panel.setOpaque(false);

        // Titre du module
        JLabel titleLabel = new JLabel("Module Enfants");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        titleLabel.setForeground(new Color(52, 152, 219));
        panel.add(titleLabel, "span 3, center, gapbottom 20");

        // Bouton Rechercher un enfant
        JButton rechercherButton = createModuleButton(
                "Rechercher",
                "Rechercher un enfant",
                new Color(52, 152, 219)
        );
        rechercherButton.addActionListener(e -> rechercherEnfant());
        panel.add(rechercherButton);

        // Bouton Nouvel enfant
        JButton nouveauButton = createModuleButton(
                "Nouveau",
                "Créer une fiche enfant",
                new Color(46, 204, 113)
        );
        nouveauButton.addActionListener(e -> nouvelEnfant());
        panel.add(nouveauButton);

        // Bouton Liste des enfants
        JButton listeButton = createModuleButton(
                "Liste",
                "Voir tous les enfants",
                new Color(155, 89, 182)
        );
        listeButton.addActionListener(e -> listeEnfants());
        panel.add(listeButton);

        // Bouton Inscriptions
        JButton inscriptionsButton = createModuleButton(
                "Inscriptions",
                "Gérer les inscriptions",
                new Color(241, 196, 15)
        );
        inscriptionsButton.addActionListener(e -> gererInscriptions());
        panel.add(inscriptionsButton);

        // Bouton Présences
        JButton presencesButton = createModuleButton(
                "Présences",
                "Saisie des présences",
                new Color(230, 126, 34)
        );
        presencesButton.addActionListener(e -> saisirPresences());
        panel.add(presencesButton);

        // Bouton Statistiques
        JButton statsButton = createModuleButton(
                "Statistiques",
                "Rapports et stats",
                new Color(149, 165, 166)
        );
        statsButton.addActionListener(e -> afficherStatistiques());
        panel.add(statsButton);

        return panel;
    }

    /**
     * Crée un bouton de module stylisé.
     */
    private JButton createModuleButton(String title, String description, Color baseColor) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Couleur de fond
                Color bgColor = isEnabled() ? baseColor : Color.LIGHT_GRAY;
                if (getModel().isPressed()) {
                    bgColor = bgColor.darker();
                } else if (getModel().isRollover()) {
                    bgColor = bgColor.brighter();
                }

                // Dessiner le fond avec coins arrondis
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Dessiner le titre
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont().deriveFont(Font.BOLD, 16f));
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(title);
                g2d.drawString(title, (getWidth() - titleWidth) / 2, getHeight() / 2 - 10);

                // Dessiner la description
                g2d.setFont(getFont().deriveFont(Font.PLAIN, 11f));
                fm = g2d.getFontMetrics();
                int descWidth = fm.stringWidth(description);
                g2d.drawString(description, (getWidth() - descWidth) / 2, getHeight() / 2 + 15);

                g2d.dispose();
            }
        };

        button.setPreferredSize(new Dimension(150, 150));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    // ==================== ACTIONS ====================

    private void rechercherEnfant() {
        MamiView view = MamiApp.getMamiView();
        view.setStatusMessage("Recherche d'un enfant...");
        // TODO: Implémenter la recherche d'enfant
        JOptionPane.showMessageDialog(this,
                "Fonctionnalité de recherche en cours de développement",
                "Rechercher un enfant",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void nouvelEnfant() {
        MamiView view = MamiApp.getMamiView();
        view.setStatusMessage("Création d'une nouvelle fiche enfant...");
        // TODO: Implémenter la création d'un nouvel enfant
        JOptionPane.showMessageDialog(this,
                "Fonctionnalité de création en cours de développement",
                "Nouvel enfant",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void listeEnfants() {
        MamiView view = MamiApp.getMamiView();
        view.setStatusMessage("Chargement de la liste des enfants...");
        // TODO: Implémenter la liste des enfants
        JOptionPane.showMessageDialog(this,
                "Fonctionnalité de liste en cours de développement",
                "Liste des enfants",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void gererInscriptions() {
        MamiView view = MamiApp.getMamiView();
        view.setStatusMessage("Gestion des inscriptions...");
        // TODO: Implémenter la gestion des inscriptions
        JOptionPane.showMessageDialog(this,
                "Fonctionnalité d'inscriptions en cours de développement",
                "Inscriptions",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void saisirPresences() {
        MamiView view = MamiApp.getMamiView();
        view.setStatusMessage("Saisie des présences...");
        // TODO: Implémenter la saisie des présences
        JOptionPane.showMessageDialog(this,
                "Fonctionnalité de présences en cours de développement",
                "Présences",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void afficherStatistiques() {
        MamiView view = MamiApp.getMamiView();
        view.setStatusMessage("Chargement des statistiques...");
        // TODO: Implémenter les statistiques
        JOptionPane.showMessageDialog(this,
                "Fonctionnalité de statistiques en cours de développement",
                "Statistiques",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
