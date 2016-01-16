package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.game.Tool;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.LightSource;
import com.jeremyklotz.jisland.graphics.SpriteSheet;
import com.jeremyklotz.jisland.utils.ColorUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jeremy Klotz on 1/4/16
 */
public class World {
    private static final int FIRE_LIGHT_COLOR = ColorUtils.createColor(150, 0, 0);
    private static final int FIRE_LIGHT_DISTANCE = 20;

    private Tile[][] tiles;
    private int[][] litTiles;
    private int viewpointX;
    private int viewpointY;
    private LightSource fireLight;
    private Tree[] trees;
    private ArrayList<Tool> fallenTools;
    private ArrayList<Integer> fallenToolCoordinates;

    public World(int tileWidth, int tileHeight) {
        tiles = new Tile[tileWidth][tileHeight];

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                tiles[x][y] = new StaticTile(Tile.TYPE_GRASS);
            }
        }

        setViewpoint(0, 0);

        fireLight = new LightSource(0, 0, FIRE_LIGHT_COLOR, FIRE_LIGHT_DISTANCE);
        litTiles = new int[0][0];
        trees = new Tree[0];
        fallenTools = new ArrayList<>();
        fallenToolCoordinates = new ArrayList<>();
    }

    public World(Tile[][] tiles, Tree[] trees, int[][] litTiles) {
        this.tiles = tiles;
        this.trees = trees;
        this.litTiles = litTiles;

        fireLight = new LightSource(0, 0, FIRE_LIGHT_COLOR, FIRE_LIGHT_DISTANCE);
        fallenTools = new ArrayList<>();
        fallenToolCoordinates = new ArrayList<>();

        setViewpoint(0, 0);
    }

    public World(String imgPath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(World.class.getResourceAsStream(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage useableImage = null;

        if (image.getType() != BufferedImage.TYPE_INT_RGB) {
            useableImage = new BufferedImage(image.getWidth(),
                    image.getHeight(), BufferedImage.TYPE_INT_RGB);
            useableImage.getGraphics().drawImage(image, 0, 0, null);
        } else
            useableImage = image;


        int[] worldPixels = ((DataBufferInt) useableImage.getRaster().getDataBuffer()).getData();

        int width = image.getWidth();
        int height = image.getHeight();

        int numLitTiles = parseTiles(worldPixels, width, height);

        initLitTiles(numLitTiles);

        generateTrees(0);

        setViewpoint(0, 0);

        fallenTools = new ArrayList<>();
        fallenToolCoordinates = new ArrayList<>();
        initFallenTools();
    }

    private int parseTiles(int[] worldPixels, int width, int height) {
        tiles = new Tile[width][height];

        int numLitTiles = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = worldPixels[x + y * width];

                if (pixel == Tile.TYPE_FIRE || pixel == Tile.TYPE_WATER) {
                    tiles[x][y] = new DynamicTile(pixel);

                    if (pixel == Tile.TYPE_FIRE)
                        numLitTiles++;
                } else {
                    tiles[x][y] = new StaticTile(pixel);
                }
            }
        }

        return numLitTiles;
    }

    private void initLitTiles(int numLitTiles) {
        litTiles = new int[numLitTiles][2];

        int currentLitTile = 0;

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                Tile t = tiles[x][y];

                if (t.getType() == Tile.TYPE_FIRE) {
                    litTiles[currentLitTile][0] = x;
                    litTiles[currentLitTile][1] = y;
                    currentLitTile++;
                }
            }
        }

        fireLight = new LightSource(0, 0, FIRE_LIGHT_COLOR, FIRE_LIGHT_DISTANCE);
    }

    public void generateTrees(int numTrees) {
        Random random = new Random();

        trees = new Tree[numTrees];

        for (int i = 0; i < numTrees; i++) {
            int x;
            int y;
            boolean regenerate;

            // No duplicated Tree coordinates
            do {
                regenerate = false;

                x = random.nextInt(getTileWidth() - 1) * Tile.TILE_SIZE;
                y = random.nextInt(getTileHeight() - 1) * Tile.TILE_SIZE;

                Tree tree = new Tree(x, y);

                // Intersection with other trees
                for (int t = 0; t < i; t++) {
                    if (trees[t].bounds().intersects(tree.bounds())) {
                        regenerate = true;
                        break;
                    }
                }

                // Trees only grown on grass
                for (int w = 0; w < tiles.length; w++) {
                    for (int h = 0; h < tiles.length; h++) {
                        Tile t = tiles[w][h];
                        if (t.getType() != Tile.TYPE_GRASS) {
                            Rectangle bounds = new Rectangle(w * Tile.TILE_SIZE, h * Tile.TILE_SIZE,
                                    Tile.TILE_SIZE, Tile.TILE_SIZE);

                            if (bounds.intersects(tree.bounds())) {
                                regenerate = true;
                                break;
                            }
                        }
                    }
                }

            } while (regenerate);

            trees[i] = new Tree(x, y);
        }
    }

    private void initFallenTools() {
        Random random = new Random();
        int x = random.nextInt(getTileWidth());
        int y = random.nextInt(getTileHeight());

        fallenTools.add(new Tool(Tool.TYPE_AXE));
        fallenToolCoordinates.add(x);
        fallenToolCoordinates.add(y);
    }

    public void update() {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                tiles[x][y].update();
            }
        }
    }

    public void render(Bitmap bitmap) {
        int startTileX = viewpointX / SpriteSheet.SPRITE_SIZE;
        int startTileY = viewpointY / SpriteSheet.SPRITE_SIZE;

        int x = (startTileX * SpriteSheet.SPRITE_SIZE) - viewpointX;
        int startY = (startTileY * SpriteSheet.SPRITE_SIZE) - viewpointY;

        int y = startY;

        for (int tileX = startTileX; tileX <= startTileX + bitmap.getWidth() / SpriteSheet.SPRITE_SIZE; tileX++) {
            for (int tileY = startTileY; tileY <= startTileY + bitmap.getHeight() / SpriteSheet.SPRITE_SIZE + 1; tileY++) {
                if (tileX >= 0 && tileY >= 0 && tileX < tiles.length && tileY < tiles[0].length)
                    tiles[tileX][tileY].render(bitmap, x, y);


                y += SpriteSheet.SPRITE_SIZE;
            }

            y = startY;
            x += SpriteSheet.SPRITE_SIZE;
        }

        renderFallenTools(bitmap);
    }

    private void renderFallenTools(Bitmap bitmap) {
        for (int i = 0; i < fallenTools.size(); i++) {
            int xi = i * 2;
            int yi = i * 2 + 1;

            int x = fallenToolCoordinates.get(xi);
            int y = fallenToolCoordinates.get(yi);

            fallenTools.get(i).render(bitmap, x - viewpointX, y - viewpointY);
        }
    }

    public void renderTrees(Bitmap bitmap) {
        for (int i = 0; i < trees.length; i++) {
            Tree tree = trees[i];

            int x = tree.getX() - viewpointX;
            int y = tree.getY() - viewpointY;

            tree.render(bitmap, x, y);
        }
    }

    public void renderLight(Bitmap bitmap) {
        for (int i = 0; i < litTiles.length; i++) {
            int x = litTiles[i][0];
            int y = litTiles[i][1];

            fireLight.setX(x * Tile.TILE_SIZE - viewpointX + Tile.TILE_SIZE / 2);
            fireLight.setY(y * Tile.TILE_SIZE - viewpointY + Tile.TILE_SIZE / 2);
            fireLight.render(bitmap);
        }
    }

    public Tree[] getTrees() {
        return trees;
    }

    public int getViewpointX() {
        return viewpointX;
    }

    public int getViewpointY() {
        return viewpointY;
    }

    public void setViewpoint(int x, int y) {
        this.viewpointX = x;
        this.viewpointY = y;
    }

    public int getTileWidth() {
        return tiles.length;
    }

    public int getTileHeight() {
        return tiles[0].length;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public ArrayList<Tool> getFallenTools() {
        return fallenTools;
    }

    public ArrayList<Integer> getFallenToolCoordinates() {
        return fallenToolCoordinates;
    }
}
