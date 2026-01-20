# Mami Application - Structure Moderne JDK 25

## Description

Application de gestion Mami modernisée, compatible avec JDK 25.
Cette version utilise des technologies modernes tout en conservant l'architecture originale.

## Prérequis

- **JDK 25** ou supérieur
- **Maven 3.8+**

## Structure du Projet

```
mami-app/
├── pom.xml                                    # Configuration Maven
├── src/
│   └── main/
│       ├── java/
│       │   └── com/mami83/application/
│       │       ├── MamiApp.java               # Point d'entrée principal
│       │       ├── MamiView.java              # Vue principale (MainFrame)
│       │       ├── composants/
│       │       │   ├── BackgroundImagePanel.java   # Panel avec image de fond
│       │       │   └── navigation/
│       │       │       ├── NavigationButton.java   # Bouton de navigation
│       │       │       └── NavigationPanel.java    # Barre de navigation dynamique
│       │       ├── page/
│       │       │   └── ihm/
│       │       │       ├── AccueilConnexionPane.java  # Écran de connexion
│       │       │       └── AccueilPane.java           # Écran d'accueil
│       │       └── util/
│       │           ├── ImageGenerator.java    # Utilitaire de génération d'images
│       │           └── TaskExecutor.java      # Exécution de tâches avec progression
│       └── resources/
│           └── images/
│               └── fond.png                   # Image de fond (optionnelle)
```

## Architecture

### MamiView (MainFrame)
La vue principale contient 3 zones :

```
┌─────────────────────────────────────────────────────────────┐
│  [Quitter] [Accueil] [Module...]        Bonjour             │  <- TopBar
│                                         Prénom Nom          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                                                             │
│                     CARD PANEL                              │  <- Centre
│                (Affichage des modules)                      │
│                                                             │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  Prêt          [████████████  75%] Chargement des données...│  <- StatusBar
└─────────────────────────────────────────────────────────────┘
```

1. **Zone du haut (TopBar)** :
   - À gauche : Barre de navigation dynamique (pile de boutons)
   - À droite : Nom de l'utilisateur connecté

2. **Zone centrale (CardPanel)** :
   - Utilise un `CardLayout` pour switcher entre les écrans
   - Chaque module/écran est un panel ajouté au CardLayout

3. **Zone du bas (StatusBar)** :
   - Message de statut à gauche
   - Progress bar temps réel au centre
   - Affiche la progression de toutes les actions

### Système de Navigation

Le système de navigation fonctionne comme une pile (stack) :
- Chaque navigation ajoute un bouton dans la barre
- Cliquer sur un bouton revient à cet écran et supprime les suivants
- Les panels associés peuvent être automatiquement libérés

### Progress Bar Temps Réel

La progress bar s'affiche automatiquement lors de :
- La connexion (avec étapes détaillées)
- Le chargement des modules
- Toute tâche longue

```java
// Exemple d'utilisation avec TaskExecutor
TaskExecutor.executeWithProgress("Ma tâche", reporter -> {
    reporter.updateProgress(25, "Étape 1...");
    // ... travail ...
    reporter.updateProgress(50, "Étape 2...");
    // ... travail ...
    reporter.updateProgress(100, "Terminé");
    return resultat;
}, result -> {
    // Succès
}, error -> {
    // Erreur
});

// Ou directement via MamiView
MamiView view = MamiApp.getMamiView();
view.startTask("Chargement");
view.updateProgress(50, "En cours...");
view.endTask("Terminé");
```

## Dépendances

- **FlatLaf 3.4** : Look and Feel moderne
- **MigLayout 11.3** : Gestionnaire de layout flexible

## Compilation et Exécution

```bash
# Compilation
mvn clean compile

# Exécution
mvn exec:java -Dexec.mainClass="com.mami83.application.MamiApp"

# Création du JAR exécutable
mvn clean package

# Exécution du JAR
java --enable-preview -jar target/mami-app-2.0.0-SNAPSHOT.jar
```

## Connexion par défaut

Pour la démonstration :
- **Identifiant** : `admin`
- **Mot de passe** : `admin`

## Personnalisation

### Image de fond
Placez votre image `fond.png` dans `src/main/resources/images/`.
Si aucune image n'est trouvée, un dégradé par défaut est généré.

### Thème
Le thème peut être changé dans `MamiApp.java` :

```java
// Thème clair (par défaut)
FlatLightLaf.setup();

// Thème sombre
FlatDarkLaf.setup();

// Thème IntelliJ
FlatIntelliJLaf.setup();
```

## Extension

Pour ajouter un nouveau module :

1. Créer le panel du module dans `page/ihm/`
2. Créer un `NavigationButton` si nécessaire
3. Ajouter l'action dans `AccueilPane`

```java
// Dans AccueilPane
private void afficherMonModule() {
    MamiView view = MamiApp.getMamiView();
    if (!view.showPanel("monModule")) {
        MonModulePane panel = new MonModulePane();
        view.addAndShow(panel, "monModule");
    }
    view.showNavigationButton("monModule", "monModule");
}
```

## Licence

Copyright 2007-2025 Association MAMI - Tous droits réservés
