package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.core.Input;

/**
 * Created by Jeremy Klotz on 5/25/16.
 */
public class SceneManager {
    private static Scene currentScene;
    private static Input input;

    private static Scene mainMenu;
    private static Scene gameEngine;
    private static Scene pauseMenu;
    private static Scene inventory;

    public static void init(Scene mainMenuScene, Scene gameEngineScene, Scene pauseMenuScene, Scene inventoryScene, Input inputHandler) {
        mainMenu = mainMenuScene;
        gameEngine = gameEngineScene;
        pauseMenu = pauseMenuScene;
        inventory = inventoryScene;
        input = inputHandler;

        currentScene = null;
    }

    public static void showScene(Scene scene) {
        if (currentScene != null)
            currentScene.dispose();

        currentScene = scene;
        currentScene.show();

        input.resetKeys();
    }

    public static void showMainMenu() {
        showScene(mainMenu);
    }

    public static void showGameScene() {
        showScene(gameEngine);
    }

    public static void showPauseMenu() {
        showScene(pauseMenu);
    }

    public static void showInvetory() {
        showScene(inventory);
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }
}
