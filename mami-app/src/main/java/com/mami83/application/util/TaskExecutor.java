/*
 * Mami Application
 * Copyright 2007-2025 Association MAMI
 * Tous droits réservés
 */
package com.mami83.application.util;

import com.mami83.application.MamiApp;
import com.mami83.application.MamiView;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Utilitaire pour exécuter des tâches en arrière-plan avec affichage
 * de la progression en temps réel dans la barre de statut.
 *
 * @author Association MAMI
 */
public class TaskExecutor {

    /**
     * Exécute une tâche simple avec progression indéterminée.
     *
     * @param taskName le nom de la tâche
     * @param task la tâche à exécuter
     * @param onSuccess callback appelé en cas de succès
     * @param <T> le type de retour de la tâche
     */
    public static <T> void executeIndeterminate(String taskName, Callable<T> task, Consumer<T> onSuccess) {
        executeIndeterminate(taskName, task, onSuccess, null);
    }

    /**
     * Exécute une tâche simple avec progression indéterminée.
     *
     * @param taskName le nom de la tâche
     * @param task la tâche à exécuter
     * @param onSuccess callback appelé en cas de succès
     * @param onError callback appelé en cas d'erreur
     * @param <T> le type de retour de la tâche
     */
    public static <T> void executeIndeterminate(String taskName, Callable<T> task, 
                                                  Consumer<T> onSuccess, Consumer<Exception> onError) {
        MamiView view = MamiApp.getMamiView();
        view.startIndeterminateTask(taskName);

        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    view.endIndeterminateTask("Terminé");
                    if (onSuccess != null) {
                        onSuccess.accept(result);
                    }
                } catch (Exception e) {
                    view.endTaskWithError(e.getMessage());
                    if (onError != null) {
                        onError.accept(e);
                    }
                }
            }
        };
        worker.execute();
    }

    /**
     * Exécute une tâche avec progression connue.
     *
     * @param taskName le nom de la tâche
     * @param task la tâche à exécuter (reçoit un ProgressReporter)
     * @param onSuccess callback appelé en cas de succès
     * @param <T> le type de retour de la tâche
     */
    public static <T> void executeWithProgress(String taskName, ProgressTask<T> task, Consumer<T> onSuccess) {
        executeWithProgress(taskName, task, onSuccess, null);
    }

    /**
     * Exécute une tâche avec progression connue.
     *
     * @param taskName le nom de la tâche
     * @param task la tâche à exécuter (reçoit un ProgressReporter)
     * @param onSuccess callback appelé en cas de succès
     * @param onError callback appelé en cas d'erreur
     * @param <T> le type de retour de la tâche
     */
    public static <T> void executeWithProgress(String taskName, ProgressTask<T> task,
                                                 Consumer<T> onSuccess, Consumer<Exception> onError) {
        MamiView view = MamiApp.getMamiView();
        view.startTask(taskName);

        SwingWorker<T, Integer> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.execute(new ProgressReporter() {
                    @Override
                    public void updateProgress(int percent) {
                        publish(percent);
                    }

                    @Override
                    public void updateProgress(int percent, String message) {
                        view.updateProgress(percent, message);
                    }
                });
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int latestProgress = chunks.get(chunks.size() - 1);
                view.updateProgress(latestProgress);
            }

            @Override
            protected void done() {
                try {
                    T result = get();
                    view.endTask("Terminé");
                    if (onSuccess != null) {
                        onSuccess.accept(result);
                    }
                } catch (Exception e) {
                    view.endTaskWithError(e.getMessage());
                    if (onError != null) {
                        onError.accept(e);
                    }
                }
            }
        };
        worker.execute();
    }

    /**
     * Exécute une tâche simple sans retour avec progression.
     *
     * @param taskName le nom de la tâche
     * @param task la tâche à exécuter
     * @param onComplete callback appelé à la fin
     */
    public static void executeWithProgress(String taskName, ProgressRunnable task, Runnable onComplete) {
        executeWithProgress(taskName, reporter -> {
            task.run(reporter);
            return null;
        }, result -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    /**
     * Interface pour les tâches avec progression.
     *
     * @param <T> le type de retour
     */
    @FunctionalInterface
    public interface ProgressTask<T> {
        T execute(ProgressReporter reporter) throws Exception;
    }

    /**
     * Interface pour les tâches sans retour avec progression.
     */
    @FunctionalInterface
    public interface ProgressRunnable {
        void run(ProgressReporter reporter) throws Exception;
    }

    /**
     * Interface pour reporter la progression.
     */
    public interface ProgressReporter {
        /**
         * Met à jour la progression.
         *
         * @param percent le pourcentage (0-100)
         */
        void updateProgress(int percent);

        /**
         * Met à jour la progression avec un message.
         *
         * @param percent le pourcentage (0-100)
         * @param message le message à afficher
         */
        void updateProgress(int percent, String message);
    }

    /**
     * Exemple d'utilisation avec une tâche simulée.
     */
    public static void demonstrateUsage() {
        // Exemple 1: Tâche indéterminée
        executeIndeterminate("Chargement des données", () -> {
            Thread.sleep(2000); // Simulation
            return "Données chargées";
        }, result -> {
            System.out.println("Résultat: " + result);
        });

        // Exemple 2: Tâche avec progression
        executeWithProgress("Traitement des fichiers", reporter -> {
            for (int i = 0; i <= 100; i += 10) {
                reporter.updateProgress(i, "Fichier " + (i / 10) + "/10");
                Thread.sleep(200); // Simulation
            }
            return "Traitement terminé";
        }, result -> {
            System.out.println("Résultat: " + result);
        });
    }
}
