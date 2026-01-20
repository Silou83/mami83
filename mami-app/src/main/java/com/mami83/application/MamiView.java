/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application;

import com.mami83.application.composants.BackgroundImagePanel;
import com.mami83.application.composants.Icons;
import com.mami83.application.composants.SpinnerIcon;
import com.mami83.application.composants.navigation.NavigationButton;
import com.mami83.application.composants.navigation.NavigationPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Vue principale de l'application Mami.
 * Contient le mainframe avec :
 * - En haut : la barre de navigation dynamique (icônes) + nom utilisateur à droite
 * - Au centre : le CardPanel pour afficher les différents panels
 * - En bas : la barre de statut avec spinner animé à droite
 * Le tout avec une image de fond.
 *
 * @author Association MAMI
 */
public class MamiView {

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    // Frame principale
    private final JFrame frame;

    // Panel principal avec image de fond
    private final BackgroundImagePanel mainPanel;

    // Barre de navigation (en haut)
    private NavigationPanel navigationPanel;
    private final JPanel topBar;

    // Panel central avec CardLayout pour afficher les différents écrans
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final Map<String, JComponent> cardMap = new HashMap<>();

    // Barre de statut (en bas)
    private final JPanel statusPanel;
    private final JLabel statusMessageLabel;      // Message à gauche
    private final JLabel connectionLabel;         // "Connecté à [BDD]" au centre
    private final SpinnerIcon spinnerIcon;        // Spinner animé à droite
    private final JLabel spinnerLabel;            // Label contenant le spinner

    // Informations utilisateur (en haut à droite)
    private final JLabel welcomeLabel;
    private final JLabel userNameLabel;
    private final JPanel userInfoTopPanel;

    // Boutons de navigation pré-enregistrés
    private NavigationButton quitterButton;
    private NavigationButton accueilButton;

    // Nom de la base de données
    private String databaseName = "Base de données";

    /**
     * Constructeur de la vue principale.
     */
    public MamiView() {
        // Création de la frame
        frame = new JFrame("Mami Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1024, 768));

        // Panel principal avec image de fond
        mainPanel = new BackgroundImagePanel(new BorderLayout());
        mainPanel.loadImage("/images/fond.png");
        mainPanel.setStyle(BackgroundImagePanel.Style.SCALED);

        // Initialisation des composants utilisateur (haut droite)
        welcomeLabel = new JLabel("Bonjour");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.PLAIN, 12f));
        welcomeLabel.setForeground(new Color(100, 100, 100));
        
        userNameLabel = new JLabel("");
        userNameLabel.setFont(userNameLabel.getFont().deriveFont(Font.BOLD, 14f));
        userNameLabel.setForeground(new Color(51, 51, 51));

        userInfoTopPanel = new JPanel();
        userInfoTopPanel.setOpaque(false);
        userInfoTopPanel.setLayout(new BoxLayout(userInfoTopPanel, BoxLayout.Y_AXIS));
        userInfoTopPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 20));
        userInfoTopPanel.setVisible(false);

        // === BARRE DU HAUT ===
        topBar = createTopBar();

        // === PANEL CENTRAL (CardLayout) ===
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // === BARRE DE STATUT ===
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setPreferredSize(new Dimension(0, 36));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        statusPanel.setBackground(new Color(250, 250, 250));

        // Message de statut à gauche
        statusMessageLabel = new JLabel("Prêt");
        statusMessageLabel.setFont(statusMessageLabel.getFont().deriveFont(12f));
        statusMessageLabel.setForeground(new Color(80, 80, 80));

        // Label de connexion au centre
        connectionLabel = new JLabel("");
        connectionLabel.setFont(connectionLabel.getFont().deriveFont(Font.ITALIC, 11f));
        connectionLabel.setForeground(new Color(100, 100, 100));
        connectionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Spinner animé à droite
        spinnerIcon = new SpinnerIcon(20, new Color(70, 130, 180));
        spinnerLabel = new JLabel(spinnerIcon);
        spinnerLabel.setVisible(false);

        // Panel gauche
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(statusMessageLabel);

        // Panel droit avec spinner
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(spinnerLabel);

        statusPanel.add(leftPanel, BorderLayout.WEST);
        statusPanel.add(connectionLabel, BorderLayout.CENTER);
        statusPanel.add(rightPanel, BorderLayout.EAST);

        // === ASSEMBLAGE ===
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);

        // Configuration de la navigation
        setupNavigation();
    }

    /**
     * Crée la barre du haut contenant la navigation et le nom utilisateur.
     */
    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 85));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Panel de navigation à gauche
        navigationPanel = new NavigationPanel();
        navigationPanel.setPanelRemover(this::removePanel);

        // Panel d'info utilisateur à droite
        welcomeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        userNameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        userInfoTopPanel.add(welcomeLabel);
        userInfoTopPanel.add(Box.createVerticalStrut(2));
        userInfoTopPanel.add(userNameLabel);

        panel.add(navigationPanel, BorderLayout.WEST);
        panel.add(userInfoTopPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Configure les boutons de navigation par défaut.
     */
    private void setupNavigation() {
        // Bouton Quitter avec icône
        quitterButton = new NavigationButton("quitter", "Quitter", Icons.exitIcon());
        quitterButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Voulez-vous vraiment quitter l'application ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        navigationPanel.addNavigationButton(quitterButton);

        // Bouton Accueil avec icône
        accueilButton = new NavigationButton("accueil", "Accueil", Icons.homeIcon());
        accueilButton.addActionListener(e -> showPanel("accueil"));
        navigationPanel.addNavigationButton(accueilButton);

        // Afficher le bouton quitter par défaut
        navigationPanel.showButton("quitter");
    }

    // ==================== GESTION DES PANELS ====================

    /**
     * Affiche un panel par son nom.
     *
     * @param name le nom du panel
     * @return true si le panel existe et a été affiché
     */
    public boolean showPanel(String name) {
        if (cardMap.containsKey(name)) {
            cardLayout.show(cardPanel, name);
            return true;
        }
        return false;
    }

    /**
     * Ajoute un panel au CardLayout.
     *
     * @param panel le panel à ajouter
     * @param name le nom unique du panel
     * @return true si l'ajout a réussi
     */
    public boolean addPanel(JComponent panel, String name) {
        if (cardMap.containsKey(name)) {
            return false;
        }
        cardMap.put(name, panel);
        cardPanel.add(panel, name);
        return true;
    }

    /**
     * Ajoute et affiche immédiatement un panel.
     *
     * @param panel le panel à ajouter
     * @param name le nom unique du panel
     * @return true si l'opération a réussi
     */
    public boolean addAndShow(JComponent panel, String name) {
        if (addPanel(panel, name)) {
            return showPanel(name);
        }
        return false;
    }

    /**
     * Supprime un panel du CardLayout.
     *
     * @param name le nom du panel à supprimer
     */
    public void removePanel(String name) {
        JComponent panel = cardMap.get(name);
        if (panel != null) {
            cardPanel.remove(panel);
            cardMap.remove(name);
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }

    /**
     * Vérifie si un panel existe.
     *
     * @param name le nom du panel
     * @return true si le panel existe
     */
    public boolean hasPanel(String name) {
        return cardMap.containsKey(name);
    }

    // ==================== GESTION DE LA NAVIGATION ====================

    /**
     * Ajoute un bouton à la barre de navigation.
     *
     * @param button le bouton à ajouter
     */
    public void addNavigationButton(NavigationButton button) {
        navigationPanel.addNavigationButton(button);
    }

    /**
     * Affiche un bouton de navigation.
     *
     * @param name le nom du bouton
     */
    public void showNavigationButton(String name) {
        navigationPanel.showButton(name);
    }

    /**
     * Affiche un bouton de navigation avec un panel à libérer.
     *
     * @param name le nom du bouton
     * @param panelToFree le nom du panel à libérer lors du retour
     */
    public void showNavigationButton(String name, String panelToFree) {
        navigationPanel.showButton(name, panelToFree);
    }

    // ==================== GESTION DU STATUT ET SPINNER ====================

    /**
     * Définit le message de la barre de statut.
     *
     * @param message le message à afficher
     */
    public void setStatusMessage(String message) {
        SwingUtilities.invokeLater(() -> statusMessageLabel.setText(message));
    }

    /**
     * Définit le nom de la base de données et affiche "Connecté à [nom]".
     *
     * @param dbName le nom de la base de données
     */
    public void setDatabaseName(String dbName) {
        this.databaseName = dbName;
        SwingUtilities.invokeLater(() -> 
            connectionLabel.setText("Connecté à " + dbName)
        );
    }

    /**
     * @return le nom de la base de données
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Démarre le spinner (indicateur de chargement).
     *
     * @param message le message de statut à afficher
     */
    public void startLoading(String message) {
        SwingUtilities.invokeLater(() -> {
            statusMessageLabel.setText(message);
            spinnerIcon.start();
            spinnerLabel.setVisible(true);
        });
    }

    /**
     * Démarre le spinner sans changer le message.
     */
    public void startLoading() {
        SwingUtilities.invokeLater(() -> {
            spinnerIcon.start();
            spinnerLabel.setVisible(true);
        });
    }

    /**
     * Arrête le spinner.
     *
     * @param message le message de statut à afficher
     */
    public void stopLoading(String message) {
        SwingUtilities.invokeLater(() -> {
            spinnerIcon.stop();
            spinnerLabel.setVisible(false);
            statusMessageLabel.setText(message);
        });
    }

    /**
     * Arrête le spinner sans changer le message.
     */
    public void stopLoading() {
        SwingUtilities.invokeLater(() -> {
            spinnerIcon.stop();
            spinnerLabel.setVisible(false);
        });
    }

    /**
     * Démarre une tâche avec affichage du spinner.
     *
     * @param taskName le nom de la tâche
     */
    public void startTask(String taskName) {
        startLoading(taskName + "...");
    }

    /**
     * Met à jour le message pendant une tâche.
     *
     * @param progress ignoré (compatibilité)
     * @param message le message à afficher
     */
    public void updateProgress(int progress, String message) {
        SwingUtilities.invokeLater(() -> {
            if (message != null && !message.isEmpty()) {
                statusMessageLabel.setText(message);
            }
        });
    }

    /**
     * Met à jour le message pendant une tâche.
     *
     * @param progress ignoré (compatibilité)
     */
    public void updateProgress(int progress) {
        // Pas d'affichage de pourcentage avec le spinner
    }

    /**
     * Termine une tâche.
     *
     * @param successMessage le message de succès
     */
    public void endTask(String successMessage) {
        stopLoading(successMessage);
    }

    /**
     * Termine une tâche en erreur.
     *
     * @param errorMessage le message d'erreur
     */
    public void endTaskWithError(String errorMessage) {
        SwingUtilities.invokeLater(() -> {
            spinnerIcon.stop();
            spinnerLabel.setVisible(false);
            statusMessageLabel.setText("Erreur: " + errorMessage);
            statusMessageLabel.setForeground(new Color(180, 60, 60));
            
            // Remettre la couleur normale après 3 secondes
            Timer resetTimer = new Timer(3000, e -> {
                statusMessageLabel.setForeground(new Color(80, 80, 80));
            });
            resetTimer.setRepeats(false);
            resetTimer.start();
        });
    }

    /**
     * Démarre une tâche indéterminée.
     *
     * @param taskName le nom de la tâche
     */
    public void startIndeterminateTask(String taskName) {
        startTask(taskName);
    }

    /**
     * Termine une tâche indéterminée.
     *
     * @param successMessage le message de succès
     */
    public void endIndeterminateTask(String successMessage) {
        endTask(successMessage);
    }

    /**
     * @deprecated Utiliser startLoading/stopLoading à la place
     */
    @Deprecated
    public void setProgressBarVisible(boolean visible) {
        if (visible) {
            startLoading();
        } else {
            stopLoading();
        }
    }

    /**
     * @deprecated Pas d'effet avec le spinner
     */
    @Deprecated
    public void setProgressValue(int value) {
        // Pas d'effet avec le spinner
    }

    /**
     * @deprecated Pas d'effet avec le spinner
     */
    @Deprecated
    public void setProgressIndeterminate(boolean indeterminate) {
        // Le spinner est toujours "indéterminé"
    }

    /**
     * Affiche les informations de l'utilisateur connecté (en haut à droite).
     *
     * @param prenom le prénom de l'utilisateur
     * @param nom le nom de l'utilisateur
     */
    public void afficherDonneesPersonnelles(String prenom, String nom) {
        SwingUtilities.invokeLater(() -> {
            welcomeLabel.setText("Bonjour");
            userNameLabel.setText(prenom + " " + nom);
            userInfoTopPanel.setVisible(true);
        });
    }

    /**
     * Cache les informations utilisateur (déconnexion).
     */
    public void cacherDonneesPersonnelles() {
        SwingUtilities.invokeLater(() -> {
            userNameLabel.setText("");
            userInfoTopPanel.setVisible(false);
            connectionLabel.setText("");
        });
    }

    // ==================== ACCESSEURS ====================

    /**
     * @return la frame principale
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * @return le panel principal avec l'image de fond
     */
    public BackgroundImagePanel getMainPanel() {
        return mainPanel;
    }

    /**
     * @return le panel de navigation
     */
    public NavigationPanel getNavigationPanel() {
        return navigationPanel;
    }

    /**
     * @return le panel central (CardLayout)
     */
    public JPanel getCardPanel() {
        return cardPanel;
    }

    /**
     * @return l'icône du spinner
     */
    public SpinnerIcon getSpinnerIcon() {
        return spinnerIcon;
    }

    /**
     * @return true si le spinner est actif
     */
    public boolean isLoading() {
        return spinnerIcon.isRunning();
    }

    /**
     * Affiche la fenêtre principale.
     */
    public void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ==================== PROPERTY CHANGE SUPPORT ====================

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
