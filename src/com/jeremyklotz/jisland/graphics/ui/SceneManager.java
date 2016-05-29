package com.jeremyklotz.jisland.graphics.ui;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Jeremy Klotz on 5/25/16.
 */
public class SceneManager {
    private static Scene currentScene;
    private static Queue<Scene> nextScenes;

    public static void init() {
        nextScenes = new LinkedList<>();
        currentScene = null;
    }

    public static void showScene(Scene scene) {
        if (currentScene != null)
            currentScene.dispose();

        currentScene = scene;
    }

    public static void queueNextScene(Scene nextScene) {
        nextScenes.add(nextScene);
    }

    public static void nextScene() {
        currentScene = nextScenes.poll();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }
}
