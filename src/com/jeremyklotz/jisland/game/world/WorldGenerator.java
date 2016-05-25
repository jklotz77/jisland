package com.jeremyklotz.jisland.game.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Jeremy Klotz on 1/15/16
 */
public class WorldGenerator {
    private static double FIRE_IN_TREE_PROBABILITY = 10;

    public static World generateWorld(int tileWidth, int tileHeight, int numLakes, int numForests, double fireProbability) {
        Tile[][] tiles = new Tile[tileWidth][tileHeight];
        LinkedList<Tree> trees;
        Fire[] fires;

        // Fill with grass
        for (int x = 0; x < tileWidth; x++) {
            for (int y = 0; y < tileHeight; y++) {
                tiles[x][y] = new StaticTile(Tile.TYPE_GRASS);
            }
        }

        generateLakes(tiles, numLakes);
        generateSand(tiles);
        trees = generateForests(tiles, numForests);
        fires = generateFire(tiles, trees, fireProbability);

        return new World(tiles, trees, fires);
    }

    private static void generateLakes(Tile[][] tiles, int numLakes) {
        Random random = new Random();

        for (int i = 0; i < numLakes; i++) {
            int lakeSize = random.nextInt(tiles.length * tiles[0].length / (numLakes * 2));

            generateLake(tiles, random.nextInt(tiles.length), random.nextInt(tiles[0].length), lakeSize, random);
        }
    }

    private static void generateLake(Tile[][] tiles, int x, int y, int remainingTiles, Random random) {
        if (remainingTiles == 0) return;

        int newX;
        int newY;

        do {
            newX = x + random.nextInt(3) - 1;
        } while (newX < 0 || newX >= tiles.length);

        do {
            newY = y + random.nextInt(3) - 1;
        } while (newY < 0 || newY >= tiles[0].length);

        tiles[newX][newY] = new DynamicTile(Tile.TYPE_WATER);

        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                if (dx + newX >= 0 && dx + newX < tiles.length && dy + newY >= 0 && dy + newY < tiles[0].length) {
                    tiles[newX + dx][newY + dy] = new DynamicTile(Tile.TYPE_WATER);
                }
            }
        }

        generateLake(tiles, newX, newY, remainingTiles - 1, random);
    }

    private static void generateSand(Tile[][] tiles) {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {

                if (tiles[x][y].getType() == Tile.TYPE_WATER) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {

                            if (x + dx >= 0 && y + dy >= 0 &&
                                    x + dx < tiles.length && y + dy < tiles[0].length) {

                                if (tiles[x + dx][y + dy].getType() != Tile.TYPE_WATER &&
                                        tiles[x + dx][y + dy].getType() != Tile.TYPE_SAND) {

                                    tiles[x + dx][y + dy] = new StaticTile(Tile.TYPE_SAND);
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private static LinkedList<Tree> generateForests(Tile[][] tiles, int numForests) {
        LinkedList<Tree> treesList = new LinkedList<Tree>();
        Random random = new Random();

        for (int i = 0; i < numForests; i++) {
            int forestSize = random.nextInt(tiles.length * tiles[0].length / (numForests * 2));

            generateForests(tiles, treesList, random.nextInt(tiles.length), random.nextInt(tiles[0].length), forestSize, random);
        }

        return treesList;
    }

    private static void generateForests(Tile[][] tiles, LinkedList<Tree> trees, int x, int y, int remainingTiles, Random random) {
        if (remainingTiles == 0) return;

        int newX;
        int newY;

        do {
            newX = x + (random.nextInt(3) - 1);
        } while (newX < 0 || newX >= tiles.length);

        do {
            newY = y + (random.nextInt(3) - 1);
        } while (newY < 0 || newY >= tiles[0].length);

        Tree newTree = new Tree(newX * Tile.TILE_SIZE, newY * Tile.TILE_SIZE);

        if (treeIsOnGrass(tiles, newTree)) {
            boolean add = true;

            for (Tree t : trees) {
                if (t.equals(newTree)) {
                    add = false;
                    break;
                }

                if (t.bounds().intersects(newTree.bounds())) {
                    add = false;
                    break;
                }
            }

            if (add) {
                trees.add(newTree);
            }
        }


        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                if (dx + newX >= 0 && dx + newX < tiles.length && dy + newY >= 0 && dy + newY < tiles[0].length) {
                    newTree = new Tree((newX + dx) * Tile.TILE_SIZE, (newY + dy) * Tile.TILE_SIZE);

                    if (treeIsOnGrass(tiles, newTree)) {
                        boolean add = true;

                        for (Tree t : trees) {
                            if (t.equals(newTree)) {
                                add = false;
                                break;
                            }

                            if (t.bounds().intersects(newTree.bounds())) {
                                add = false;
                                break;
                            }
                        }

                        if (add) {
                            trees.add(newTree);
                        }
                    }
                }
            }
        }

        generateForests(tiles, trees, newX, newY, remainingTiles - 1, random);
    }

    private static boolean treeIsOnGrass(Tile[][] tiles, Tree tree) {
        int x = tree.getX() / Tile.TILE_SIZE;
        int y = tree.getY() / Tile.TILE_SIZE;

        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                if (x + dx >= 0 && y + dy >= 0 && x + dx < tiles.length && y + dy < tiles[0].length) {
                    if (tiles[x + dx][y + dy].getType() != Tile.TYPE_GRASS)
                        return false;
                }
            }
        }

        return true;
    }

    private static Fire[] generateFire(Tile[][] tiles, LinkedList<Tree> trees, double fireProbability) {
        Random random = new Random();
        ArrayList<Fire> fireArrayList = new ArrayList<>();

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y].getType() == Tile.TYPE_GRASS) {
                    double rand = random.nextDouble();

                    double actualProbability = fireProbability;

                    Rectangle fireBounds = new Rectangle(x * Tile.TILE_SIZE, y * Tile.TILE_SIZE, Fire.FIRE_WIDTH, Fire.FIRE_HEIGHT);

                    for (Tree t : trees) {
                        if (t.bounds().intersects(fireBounds)) {
                            actualProbability *= FIRE_IN_TREE_PROBABILITY;
                            break;
                        }
                    }

                    if (rand < actualProbability) {
                        fireArrayList.add(new Fire(x * Tile.TILE_SIZE, y * Tile.TILE_SIZE));
                    }
                }
            }
        }

        Fire[] fires = new Fire[fireArrayList.size()];

        for (int i = 0; i < fireArrayList.size(); i++) {
            fires[i] = fireArrayList.get(i);
        }

        return fires;
    }
}
