/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.composants.navigation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Panel de navigation dynamique qui gère une pile de boutons.
 * Permet de naviguer de façon hiérarchique dans l'application.
 * Version moderne compatible JDK 25.
 *
 * @author Association MAMI
 */
public class NavigationPanel extends JPanel implements ActionListener {

    // Map des boutons enregistrés dans la barre de navigation
    private final Map<String, NavigationButton> buttons = new HashMap<>();
    
    // Pile des boutons affichés dans la barre de navigation
    private final Deque<NavigationButton> navigation = new ArrayDeque<>();
    
    // Callback pour la suppression des panels
    private Consumer<String> panelRemover;

    /**
     * Constructeur du panel de navigation.
     */
    public NavigationPanel() {
        initComponents();
    }

    /**
     * Initialise les composants du panel.
     */
    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
        setOpaque(false);
    }

    /**
     * Définit le callback pour la suppression des panels.
     *
     * @param panelRemover le consumer appelé pour supprimer un panel
     */
    public void setPanelRemover(Consumer<String> panelRemover) {
        this.panelRemover = panelRemover;
        // Met à jour tous les boutons existants
        buttons.values().forEach(btn -> btn.setPanelRemover(panelRemover));
    }

    /**
     * Ajoute un bouton de navigation avec son nom d'enregistrement par défaut.
     *
     * @param button le bouton à ajouter
     */
    public void addNavigationButton(NavigationButton button) {
        addNavigationButton(button, button.getRegisterName());
    }

    /**
     * Ajoute un bouton de navigation avec un nom spécifique.
     *
     * @param button le bouton à ajouter
     * @param name le nom d'enregistrement
     */
    public void addNavigationButton(NavigationButton button, String name) {
        buttons.put(name, button);
        button.addActionListener(this);
        button.setPanelRemover(panelRemover);
    }

    /**
     * Supprime un bouton de navigation.
     *
     * @param name le nom du bouton à supprimer
     */
    public void removeNavigationButton(String name) {
        if (buttons.containsKey(name)) {
            NavigationButton button = buttons.get(name);
            button.removeActionListener(this);
            buttons.remove(name);
        }
    }

    /**
     * Affiche un bouton dans la barre de navigation avec un panel à libérer.
     *
     * @param name le nom du bouton à afficher
     * @param panelToFree le nom du panel à libérer lors du retour
     */
    public void showButton(String name, String panelToFree) {
        NavigationButton button = buttons.get(name);
        if (button == null) {
            System.err.println("NavigationPanel: Bouton non trouvé: " + name);
            return;
        }

        if (panelToFree != null) {
            button.setCardPanelToFree(panelToFree);
        }

        // Affichage du bouton dans la barre de navigation
        add(button);
        button.setVisible(true);

        // Ajout dans la pile de navigation
        navigation.push(button);

        // Rafraîchit l'affichage
        revalidate();
        repaint();
    }

    /**
     * Affiche un bouton dans la barre de navigation.
     *
     * @param name le nom du bouton à afficher
     */
    public void showButton(String name) {
        showButton(name, null);
    }

    /**
     * Cache un bouton par son nom.
     *
     * @param name le nom du bouton à cacher
     */
    public void hideButton(String name) {
        NavigationButton button = buttons.get(name);
        if (button != null) {
            hideButton(button);
        }
    }

    /**
     * Cache un bouton et tous ceux qui le suivent dans la pile.
     *
     * @param button le bouton à cacher
     */
    public void hideButton(NavigationButton button) {
        // Retrait du bouton ainsi que de tous ceux qui le suivent
        NavigationButton tmp;
        while (!navigation.isEmpty()) {
            tmp = navigation.pop();
            tmp.setVisible(false);
            remove(tmp);
            tmp.doFree();
            
            if (tmp == button) {
                break;
            }
        }

        // Rafraîchit l'affichage
        revalidate();
        repaint();
    }

    /**
     * Retourne le nombre de boutons actuellement affichés.
     *
     * @return le nombre de boutons dans la pile de navigation
     */
    public int getNavigationDepth() {
        return navigation.size();
    }

    /**
     * Vérifie si un bouton est enregistré.
     *
     * @param name le nom du bouton
     * @return true si le bouton existe
     */
    public boolean hasButton(String name) {
        return buttons.containsKey(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof NavigationButton button) {
            hideButton(button);
        }
    }
}
