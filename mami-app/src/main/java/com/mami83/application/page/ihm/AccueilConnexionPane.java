/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.page.ihm;

import com.mami83.application.MamiApp;
import com.mami83.application.MamiView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Year;

/**
 * Panel de connexion à l'application.
 * Version moderne compatible JDK 25.
 *
 * @author Association MAMI
 */
public class AccueilConnexionPane extends JPanel {

    private final JTextField loginTextField;
    private final JPasswordField passwordField;
    private final JButton validerButton;
    private final JLabel messageLabel;

    /**
     * Constructeur du panel de connexion.
     */
    public AccueilConnexionPane() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // Panel central de connexion
        JPanel loginPanel = createLoginPanel();

        // Labels
        JLabel loginLabel = new JLabel("Identifiant :");
        loginLabel.setFont(loginLabel.getFont().deriveFont(Font.BOLD, 14f));

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(Font.BOLD, 14f));

        // Champs de saisie
        loginTextField = new JTextField(20);
        loginTextField.setFont(loginTextField.getFont().deriveFont(14f));
        loginTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        passwordField = new JPasswordField(20);
        passwordField.setFont(passwordField.getFont().deriveFont(14f));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connecter();
                }
            }
        });

        // Bouton de connexion
        validerButton = new JButton("Connexion");
        validerButton.setFont(validerButton.getFont().deriveFont(Font.BOLD, 14f));
        validerButton.setPreferredSize(new Dimension(150, 50));
        validerButton.addActionListener(e -> connecter());

        // Message d'erreur (initialement caché)
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Label copyright
        int currentYear = Year.now().getValue();
        JLabel copyrightLabel = new JLabel("Copyright © 2007-" + currentYear + " Association MAMI");
        copyrightLabel.setForeground(Color.GRAY);

        // Lien mot de passe oublié
        JLabel forgotPasswordLabel = new JLabel("<html><u>Mot de passe oublié ?</u></html>");
        forgotPasswordLabel.setForeground(new Color(0, 102, 204));
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onForgotPassword();
            }
        });

        // Construction du panel avec MigLayout
        loginPanel.setLayout(new MigLayout(
                "wrap 2, insets 30, gap 10",
                "[right][200, fill]",
                "[]10[]10[]20[]10[]10[]"
        ));

        // Logo ou titre
        JLabel titleLabel = new JLabel("Mami Application");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        titleLabel.setForeground(new Color(51, 51, 51));
        loginPanel.add(titleLabel, "span 2, center, gapbottom 20");

        // Sous-titre
        JLabel subtitleLabel = new JLabel("Connectez-vous à votre espace");
        subtitleLabel.setForeground(Color.GRAY);
        loginPanel.add(subtitleLabel, "span 2, center, gapbottom 20");

        // Champs
        loginPanel.add(loginLabel);
        loginPanel.add(loginTextField);

        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        // Bouton
        loginPanel.add(validerButton, "span 2, center, gaptop 10");

        // Message d'erreur
        loginPanel.add(messageLabel, "span 2, center");

        // Mot de passe oublié
        loginPanel.add(forgotPasswordLabel, "span 2, center, gaptop 10");

        // Copyright
        loginPanel.add(copyrightLabel, "span 2, center, gaptop 30");

        // Ajouter le panel de login au centre
        add(loginPanel, new GridBagConstraints());
    }

    /**
     * Crée le panel central de connexion avec un fond semi-transparent.
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond semi-transparent avec coins arrondis
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Bordure légère
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Tente la connexion avec les identifiants saisis.
     */
    private void connecter() {
        String login = loginTextField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (login.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        // Désactiver les contrôles pendant la connexion
        setControlsEnabled(false);
        messageLabel.setText("Connexion en cours...");
        messageLabel.setForeground(Color.GRAY);

        // Utiliser la progress bar de MamiView
        MamiView view = MamiApp.getMamiView();
        view.startTask("Connexion");

        SwingWorker<Boolean, Integer> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Étape 1: Vérification des identifiants
                publish(20);
                Thread.sleep(300);
                
                // Étape 2: Authentification
                publish(50);
                Thread.sleep(300);
                
                // TODO: Remplacer par la vraie authentification
                // return Profil.login(login, encryptedPassword);
                boolean authenticated = "admin".equals(login) && "admin".equals(password);
                
                if (!authenticated) {
                    return false;
                }
                
                // Étape 3: Chargement du profil
                publish(70);
                Thread.sleep(200);
                
                // Étape 4: Initialisation
                publish(90);
                Thread.sleep(200);
                
                return true;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int progress = chunks.get(chunks.size() - 1);
                view.updateProgress(progress, getProgressMessage(progress));
            }

            private String getProgressMessage(int progress) {
                return switch (progress) {
                    case 20 -> "Vérification des identifiants...";
                    case 50 -> "Authentification...";
                    case 70 -> "Chargement du profil...";
                    case 90 -> "Initialisation...";
                    default -> "Connexion...";
                };
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        view.endTask("Connexion réussie");
                        onLoginSuccess(login);
                    } else {
                        view.endTaskWithError("Identifiants incorrects");
                        onLoginFailure();
                    }
                } catch (Exception e) {
                    view.endTaskWithError(e.getMessage());
                    showError("Erreur de connexion: " + e.getMessage());
                    setControlsEnabled(true);
                }
            }
        };
        worker.execute();
    }

    /**
     * Appelé lors d'une connexion réussie.
     */
    private void onLoginSuccess(String login) {
        MamiView view = MamiApp.getMamiView();
        MamiApp.getInstance().connected();

        // Afficher le nom de l'utilisateur en haut à droite
        // TODO: Remplacer par les vraies données du profil
        String prenom = login.substring(0, 1).toUpperCase() + login.substring(1);
        view.afficherDonneesPersonnelles(prenom, "Utilisateur");

        // Définir le nom de la base de données (au centre de la status bar)
        // TODO: Remplacer par le vrai nom de la BDD depuis la connexion
        view.setDatabaseName("MAMI_PROD");

        // Afficher l'accueil
        AccueilPane accueilPane = new AccueilPane();
        view.addAndShow(accueilPane, "accueil");

        // Cacher ce panel
        setVisible(false);
    }

    /**
     * Appelé lors d'un échec de connexion.
     */
    private void onLoginFailure() {
        showError("Identifiant ou mot de passe incorrect");
        setControlsEnabled(true);
        passwordField.setText("");
        passwordField.requestFocus();
    }

    /**
     * Affiche un message d'erreur.
     */
    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(Color.RED);
    }

    /**
     * Active ou désactive les contrôles du formulaire.
     */
    private void setControlsEnabled(boolean enabled) {
        loginTextField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        validerButton.setEnabled(enabled);
    }

    /**
     * Appelé lors du clic sur "Mot de passe oublié".
     */
    private void onForgotPassword() {
        JOptionPane.showMessageDialog(
                this,
                "Veuillez contacter l'administrateur pour réinitialiser votre mot de passe.",
                "Mot de passe oublié",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
