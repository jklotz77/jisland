package com.jeremyklotz.jisland.game.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jeremy Klotz on 1/15/16
 */
public class WorldGenerator {
    public static World generateWorld(int tileWidth, int tileHeight, int numLakes, int numTrees, double fireProbability) {
        Tile[][] tiles = new Tile[tileWidth][tileHeight];
        Tree[] trees;
        int[][] litTiles;

        // Fill with grass
        for (int x = 0; x < tileWidth; x++) {
            for (int y = 0; y < tileHeight; y++) {
                tiles[x][y] = new StaticTile(Tile.TYPE_GRASS);
            }
        }

        generateLakes(tiles, numLakes);
        generateSand(tiles);
        trees = generateTrees(tiles, numTrees);
        generateFire(tiles, fireProbability);
        litTiles = generateLitTilesArray(tiles);

        return new World(tiles, trees, litTiles);
    }

    private static void generateLakes(Tile[][] tiles, int numLakes) {
        Random random = new Random();

        for (int i = 0; i < numLakes; i++) {
            int lakeSize = random.nextInt(tiles.length * tiles[0].length / (numLakes * 2));

            generateLake(tiles, random.nextInt(tiles.length) + 1, random.nextInt(tiles[0].length) + 1, lakeSize, random);
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

    private static Tree[] generateTrees(Tile[][] tiles, int numTrees) {
        Random random = new Random();

        Tree[] trees = new Tree[numTrees];

        for (int i = 0; i < numTrees; i++) {
            int x;
            int y;
            boolean regenerate;

            // No duplicated Tree coordinates
            do {
                regenerate = false;

                x = random.nextInt(tiles.length - 1) * Tile.TILE_SIZE;
                y = random.nextInt(tiles[0].length - 1) * Tile.TILE_SIZE;

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

        return trees;
    }

    private static void generateFire(Tile[][] tiles, double fireProbability) {
        Random random = new Random();

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y].getType() == Tile.TYPE_GRASS) {
                    double rand = random.nextDouble();

                    if (rand < fireProbability) {
                        tiles[x][y] = new DynamicTile(Tile.TYPE_FIRE);
                    }
                }
            }
        }
    }

    private static int[][] generateLitTilesArray(Tile[][] tiles) {
        ArrayList<Integer> xCoordinates = new ArrayList<>();
        ArrayList<Integer> yCoorindates = new ArrayList<>();

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                Tile tile = tiles[x][y];

                if (tile.getType() == Tile.TYPE_FIRE) {
                    xCoordinates.add(x);
                    yCoorindates.add(y);
                }
            }
        }

        int[][] litTiles = new int[xCoordinates.size()][2];

        for (int i = 0; i < xCoordinates.size(); i++) {
            litTiles[i][0] = xCoordinates.get(i);
            litTiles[i][1] = yCoorindates.get(i);
        }

        return litTiles;
    }
}
