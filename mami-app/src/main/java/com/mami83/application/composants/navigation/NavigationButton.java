/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.composants.navigation;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Bouton de navigation personnalisé pour la barre de navigation.
 * Affiche une icône avec un texte en dessous.
 * Version moderne compatible JDK 25.
 *
 * @author Association MAMI
 */
public class NavigationButton extends JButton {

    private String registerName = "";
    private String cardPanelToFree;
    private Consumer<String> panelRemover;

    /**
     * Constructeur avec nom d'enregistrement.
     *
     * @param registerName le nom utilisé pour enregistrer ce bouton
     */
    public NavigationButton(String registerName) {
        this();
        this.registerName = registerName;
    }

    /**
     * Constructeur avec nom et texte.
     *
     * @param registerName le nom utilisé pour enregistrer ce bouton
     * @param text le texte à afficher
     */
    public NavigationButton(String registerName, String text) {
        this(registerName);
        setText(text);
    }

    /**
     * Constructeur avec nom, texte et icône.
     *
     * @param registerName le nom utilisé pour enregistrer ce bouton
     * @param text le texte à afficher
     * @param icon l'icône à afficher
     */
    public NavigationButton(String registerName, String text, Icon icon) {
        this(registerName, text);
        setIcon(icon);
    }

    /**
     * Constructeur par défaut.
     */
    public NavigationButton() {
        initStyle();
    }

    /**
     * Initialise le style du bouton.
     */
    private void initStyle() {
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        // Dimensions pour les boutons avec icônes
        Dimension size = new Dimension(70, 70);
        setMaximumSize(size);
        setMinimumSize(size);
        setPreferredSize(size);

        // Police plus petite pour le texte sous l'icône
        setFont(getFont().deriveFont(Font.PLAIN, 10f));
        setForeground(new Color(60, 60, 60));

        // Curseur main
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Effet de survol
        addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalFg;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                originalFg = getForeground();
                setForeground(new Color(30, 100, 180));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setForeground(originalFg != null ? originalFg : new Color(60, 60, 60));
            }
        });
    }

    /**
     * Définit le callback pour supprimer un panel.
     *
     * @param panelRemover le consumer qui sera appelé pour supprimer un panel
     */
    public void setPanelRemover(Consumer<String> panelRemover) {
        this.panelRemover = panelRemover;
    }

    /**
     * @return le nom d'enregistrement du bouton
     */
    public String getRegisterName() {
        return registerName;
    }

    /**
     * @param registerName le nom d'enregistrement à définir
     */
    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    /**
     * @return le nom du panel à libérer
     */
    public String getCardPanelToFree() {
        return cardPanelToFree;
    }

    /**
     * @param cardPanelToFree le nom du panel à libérer lors du retour
     */
    public void setCardPanelToFree(String cardPanelToFree) {
        this.cardPanelToFree = cardPanelToFree;
    }

    /**
     * Libère le panel associé s'il existe.
     */
    public void doFree() {
        if (cardPanelToFree != null && panelRemover != null) {
            panelRemover.accept(cardPanelToFree);
            cardPanelToFree = null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fond au survol
        if (getModel().isRollover()) {
            g2.setColor(new Color(200, 220, 240, 100));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);
        }
        
        // Fond pressé
        if (getModel().isPressed()) {
            g2.setColor(new Color(180, 200, 220, 150));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
}
