package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.game.inventory.InventoryItem;
import com.jeremyklotz.jisland.game.inventory.Tool;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.LightSource;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Jeremy Klotz on 1/4/16
 */
public class World {
    private Tile[][] tiles;
    private int viewpointX;
    private int viewpointY;
    private LightSource fireLight;
    private Fire[] fires;
    private LinkedList<Tree> trees;
    private LinkedList<InventoryItem> fallenItems;

    public World(int tileWidth, int tileHeight) {
        tiles = new Tile[tileWidth][tileHeight];

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                tiles[x][y] = new StaticTile(Tile.TYPE_GRASS);
            }
        }

        setViewpoint(0, 0);

        fireLight = new LightSource(0, 0, Fire.FIRE_LIGHT_COLOR, Fire.FIRE_LIGHT_DISTANCE);
        trees = new LinkedList<>();
        fallenItems = new LinkedList<>();
    }

    public World(Tile[][] tiles, LinkedList<Tree> trees, Fire[] fires) {
        this.tiles = tiles;
        this.trees = trees;
        this.fires = fires;

        fireLight = new LightSource(0, 0, Fire.FIRE_LIGHT_COLOR, Fire.FIRE_LIGHT_DISTANCE);
        fallenItems = new LinkedList<>();

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

        parseTiles(worldPixels, width, height);

        fireLight = new LightSource(0, 0, Fire.FIRE_LIGHT_COLOR, Fire.FIRE_LIGHT_DISTANCE);

        generateTrees(0);

        setViewpoint(0, 0);

        fallenItems = new LinkedList<>();
        initFallenItems();
    }

    private void parseTiles(int[] worldPixels, int width, int height) {
        tiles = new Tile[width][height];

        ArrayList<Fire> fireArrayList = new ArrayList<>();

        int previewTileType = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = worldPixels[x + y * width];

                if (pixel == Tile.TYPE_FIRE) {
                    tiles[x][y] = new StaticTile(previewTileType);
                    fireArrayList.add(new Fire(x * Tile.TILE_SIZE, y * Tile.TILE_SIZE));
                } else if (pixel == Tile.TYPE_WATER) {
                    tiles[x][y] = new DynamicTile(pixel);
                } else {
                    tiles[x][y] = new StaticTile(pixel);
                }

                previewTileType = pixel;
            }
        }

        fires = new Fire[fireArrayList.size()];

        for (int i = 0; i < fireArrayList.size(); i++) {
            fires[i] = fireArrayList.get(i);
        }
    }

    public void generateTrees(int numTrees) {
        Random random = new Random();

        trees = new LinkedList<>();

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
                for (Iterator<Tree> it = trees.iterator(); it.hasNext();) {
                    Tree t = it.next();

                    if (t.bounds().intersects(tree.bounds())) {
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

            trees.add(new Tree(x, y));
        }
    }

    private void initFallenItems() {
        Random random = new Random();
        int x = random.nextInt(getTileWidth());
        int y = random.nextInt(getTileHeight());

        fallenItems.add(new Tool(Tool.TYPE_AXE, x, y));
    }

    public void update() {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                tiles[x][y].update();
            }
        }

        for (Fire fire : fires) {
            fire.update();
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

        renderFallenItems(bitmap);
    }

    private void renderFallenItems(Bitmap bitmap) {
        for (int i = 0; i < fallenItems.size(); i++) {
            int x = fallenItems.get(i).getFallenXOnMap();
            int y = fallenItems.get(i).getFallenYOnMap();

            fallenItems.get(i).render(bitmap, x - viewpointX, y - viewpointY);
        }
    }

    public void renderTrees(Bitmap bitmap) {
        for (Tree tree : trees) {
            int x = tree.getX() - viewpointX;
            int y = tree.getY() - viewpointY;

            if (x + Tree.TREE_WIDTH >= 0 && y + Tree.TREE_HEIGHT >= 0 &&
                    x <= bitmap.getWidth() && y <= bitmap.getHeight())
                tree.render(bitmap, x, y);
        }
    }

    public void renderLight(Bitmap bitmap) {
        for (Fire fire : fires) {
            int x = fire.getX() - viewpointX;
            int y = fire.getY() - viewpointY;

            fireLight.setX(x + Fire.FIRE_WIDTH / 2);
            fireLight.setY(y + Fire.FIRE_HEIGHT / 2);
            fireLight.render(bitmap);
        }
    }

    public void renderFire(Bitmap bitmap) {
        for (Fire fire : fires) {
            int x = fire.getX() - viewpointX;
            int y = fire.getY() - viewpointY;

            fire.render(bitmap, x, y);
        }
    }

    public LinkedList<Tree> getTrees() {
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

    public void addFallenItem(InventoryItem item) {
        fallenItems.add(item);
    }

    public LinkedList<InventoryItem> getFallenItems() {
        return fallenItems;
    }
}
