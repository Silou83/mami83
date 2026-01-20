/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.page.ihm;

import com.mami83.application.MamiApp;
import com.mami83.application.MamiView;
import com.mami83.application.composants.Icons;
import com.mami83.application.composants.navigation.NavigationButton;
import com.mami83.application.util.TaskExecutor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel d'accueil principal après connexion.
 * Affiche les différents modules accessibles selon les permissions.
 *
 * @author Association MAMI
 */
public class AccueilPane extends JPanel {

    // Permissions d'accès (à connecter à un vrai système de permissions)
    private boolean accesEnfant = true;
    private boolean accesParents = true;
    private boolean accesSalaries = true;
    private boolean accesAdministration = true;
    private boolean accesExtranet = true;

    /**
     * Constructeur du panel d'accueil.
     */
    public AccueilPane() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // Panel central avec les boutons
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
     * Crée le panel contenant les boutons d'action.
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new MigLayout(
                "wrap 3, insets 20, gap 15",
                "[150!][150!][150!]",
                "[150!][150!]"
        ));
        panel.setOpaque(false);

        // Bouton Enfants
        JButton enfantButton = createModuleButton(
                "Enfants",
                "Gestion des enfants",
                new Color(52, 152, 219)
        );
        enfantButton.setEnabled(accesEnfant);
        enfantButton.addActionListener(e -> afficherAccueilEnfant());
        panel.add(enfantButton);

        // Bouton Parents
        JButton parentButton = createModuleButton(
                "Parents",
                "Gestion des parents",
                new Color(46, 204, 113)
        );
        parentButton.setEnabled(accesParents);
        parentButton.addActionListener(e -> afficherAccueilParent());
        panel.add(parentButton);

        // Bouton Salariés
        JButton salarieButton = createModuleButton(
                "Salariés",
                "Gestion des salariés",
                new Color(155, 89, 182)
        );
        salarieButton.setEnabled(accesSalaries);
        salarieButton.addActionListener(e -> afficherAccueilSalarie());
        panel.add(salarieButton);

        // Bouton Administration
        JButton adminButton = createModuleButton(
                "Administration",
                "Configuration système",
                new Color(241, 196, 15)
        );
        adminButton.setEnabled(accesAdministration);
        adminButton.addActionListener(e -> afficherAccueilAdministration());
        panel.add(adminButton);

        // Bouton Extranet
        JButton extranetButton = createModuleButton(
                "Extranet",
                "Accès externe",
                new Color(230, 126, 34)
        );
        extranetButton.setEnabled(accesExtranet);
        extranetButton.addActionListener(e -> afficherExtranet());
        panel.add(extranetButton);

        // Bouton Mes Identifiants
        JButton identifiantsButton = createModuleButton(
                "Mes Identifiants",
                "Gérer mon compte",
                new Color(149, 165, 166)
        );
        identifiantsButton.addActionListener(e -> afficherMesIdentifiants());
        panel.add(identifiantsButton);

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

    // ==================== ACTIONS DE NAVIGATION ====================

    private void afficherAccueilEnfant() {
        loadModuleWithProgress("Module Enfants", "accueilEnfant", new Color(52, 152, 219), 
                               "Enfants", Icons.childIcon());
    }

    private void afficherAccueilParent() {
        loadModuleWithProgress("Module Parents", "accueilParent", new Color(46, 204, 113), 
                               "Parents", Icons.familyIcon());
    }

    private void afficherAccueilSalarie() {
        loadModuleWithProgress("Module Salariés", "accueilSalarie", new Color(155, 89, 182), 
                               "Salariés", Icons.employeeIcon());
    }

    private void afficherAccueilAdministration() {
        loadModuleWithProgress("Module Administration", "accueilAdministration", new Color(241, 196, 15), 
                               "Admin", Icons.adminIcon());
    }

    private void afficherExtranet() {
        loadModuleWithProgress("Module Extranet", "accueilExtranet", new Color(230, 126, 34), 
                               "Extranet", Icons.globeIcon());
    }

    private void afficherMesIdentifiants() {
        loadModuleWithProgress("Mes Identifiants", "mesIdentifiants", new Color(149, 165, 166), 
                               "Identifiants", Icons.keyIcon());
    }

    /**
     * Charge un module avec affichage du spinner.
     */
    private void loadModuleWithProgress(String moduleName, String panelName, Color color, 
                                         String buttonText, Icon buttonIcon) {
        MamiView view = MamiApp.getMamiView();
        
        // Si le panel existe déjà, juste l'afficher
        if (view.hasPanel(panelName)) {
            view.showPanel(panelName);
            return;
        }

        // Charger avec spinner
        TaskExecutor.executeWithProgress("Chargement " + moduleName, reporter -> {
            // Simulation du chargement
            reporter.updateProgress(20, "Initialisation...");
            Thread.sleep(100);
            
            reporter.updateProgress(50, "Chargement des ressources...");
            Thread.sleep(100);
            
            reporter.updateProgress(80, "Création de l'interface...");
            JPanel panel = createPlaceholderPanel(moduleName, color);
            Thread.sleep(100);
            
            return panel;
        }, panel -> {
            // Callback de succès
            view.addAndShow(panel, panelName);
            addNavigationButtonIfNeeded(panelName, buttonText, buttonIcon, panelName);
        }, error -> {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement du module: " + error.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        });
    }

    /**
     * Ajoute un bouton de navigation avec icône s'il n'existe pas déjà.
     */
    private void addNavigationButtonIfNeeded(String buttonName, String buttonText, 
                                              Icon buttonIcon, String panelToFree) {
        MamiView view = MamiApp.getMamiView();
        
        if (!view.getNavigationPanel().hasButton(buttonName)) {
            NavigationButton button = new NavigationButton(buttonName, buttonText, buttonIcon);
            button.addActionListener(e -> view.showPanel("accueil"));
            view.addNavigationButton(button);
        }
        
        view.showNavigationButton(buttonName, panelToFree);
    }

    /**
     * Crée un panel placeholder pour les modules non encore implémentés.
     */
    private JPanel createPlaceholderPanel(String moduleName, Color color) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        JLabel label = new JLabel(moduleName);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 32f));
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subLabel = new JLabel("Module en cours de développement");
        subLabel.setForeground(Color.GRAY);
        subLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentPanel.add(label, BorderLayout.CENTER);
        contentPanel.add(subLabel, BorderLayout.SOUTH);

        panel.add(contentPanel);
        return panel;
    }

    // ==================== GETTERS POUR LES PERMISSIONS ====================

    public boolean isAccesEnfant() {
        return accesEnfant;
    }

    public boolean isAccesParents() {
        return accesParents;
    }

    public boolean isAccesSalaries() {
        return accesSalaries;
    }

    public boolean isAccesAdministration() {
        return accesAdministration;
    }

    public boolean isAccesExtranet() {
        return accesExtranet;
    }
}
