package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.game.inventory.InventoryItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Jeremy Klotz on 1/7/17
 */
public class WorldIO {
    public static void saveWorldToPath(World world, String path) throws IOException {
        Path file = Paths.get(path);
        ArrayList<String> lines = new ArrayList<String>();
        
        lines.addAll(exportTiles(world.getTiles()));
        lines.addAll(exportTrees(world.getTrees()));
        lines.addAll(exportFires(world.getFires()));
        lines.addAll(exportItems(world.getFallenItems()));
        
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
    
    private static ArrayList<String> exportTiles(Tile[][] tiles) {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("# Width");
        lines.add(tiles[0].length + "");
        lines.add("# Height");
        lines.add(tiles.length + "");

        lines.add("# Tiles");
        
        for (int y = 0; y < tiles[0].length; y++) {
            StringBuilder lineBuilder = new StringBuilder();

            int count = 0;
            int referenceTileX = 0;
            
            for (int x = 0; x < tiles.length; x++) {
                if (tiles[x][y].getType() == tiles[referenceTileX][y].getType()) {
                    count++;
                    
                    if (x == tiles.length - 1) {
                        lineBuilder.append(tiles[referenceTileX][y].getType());
    
                        if (count > 1)
                            lineBuilder.append("*").append(count);
                    }
                } else {
                    lineBuilder.append(tiles[referenceTileX][y].getType());
                    
                    if (count > 1)
                        lineBuilder.append("*").append(count).append(",");
                    else if (x < tiles.length - 1)
                        lineBuilder.append(",");
                    
                    
                    count = 1;
                    referenceTileX = x;
                    
                    if (x == tiles.length - 1)
                        lineBuilder.append(tiles[referenceTileX][y].getType());
                }
            }
            
            lines.add(lineBuilder.toString());
        }
        
        return lines;
    }
    
    private static ArrayList<String> exportTrees(LinkedList<Tree> trees) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("# Trees");
        lines.add(trees.size() + "");
        
        StringBuilder treeBuilder = new StringBuilder();

        int i = 0;
        for (Tree tree : trees) {
            treeBuilder.append(tree.getX()).append(",").append(tree.getY());
        
            if (i < trees.size() - 1)
                treeBuilder.append(",");
        
            i++;
        }
    
        lines.add(treeBuilder.toString());
        
        return lines;
    }
    
    private static ArrayList<String> exportFires(Fire[] fires) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("# Fires");
        lines.add(fires.length + "");
    
        StringBuilder fireBuilder = new StringBuilder();

        for (int i = 0; i < fires.length; i++) {
            fireBuilder.append(fires[i].getX()).append(",").append(fires[i].getY());
        
            if (i < fires.length - 1)
                fireBuilder.append(",");
        }
    
        lines.add(fireBuilder.toString());
        
        return lines;
    }
    
    private static ArrayList<String> exportItems(LinkedList<InventoryItem> items) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("# Items");
        
        StringBuilder lineBuilder = new StringBuilder();

        // TODO Save items to world save file

        return lines;
    }
    
    public static World loadWorldFromFile(String path) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(path));

        String[] worldSaveLines = parseWorldSave(in);

        int width = Integer.parseInt(worldSaveLines[0]);
        int height = Integer.parseInt(worldSaveLines[1]);

        Tile[][] tiles = new Tile[width][height];
        LinkedList<Tree> trees = new LinkedList<>();
        Fire[] fires;
    
        int currentLine = 2;

        // Tiles
        for (int y = 0; y < height; y++) {
            String line = worldSaveLines[currentLine];
            int x = 0;
    
            String[] tileTypes = line.split(",");
            
            for (String type : tileTypes) {
                int count;
                int t;
                
                if (type.contains("*")) {
                    count = Integer.parseInt(type.substring(type.indexOf("*") + 1, type.length()));
                    t = Integer.parseInt(type.substring(0, type.indexOf("*")));
                } else {
                    count = 1;
                    t = Integer.parseInt(type);
                }
                
                for (int i = 0; i < count; i++) {
                    Tile tile;
                    
                    switch (t) {
                        case Tile.TYPE_WATER:
                        case Tile.TYPE_FIRE:
                            tile = new DynamicTile(t);
                            break;
                        default:
                            tile = new StaticTile(t);
                            break;
                    }
                    
                    tiles[x][y] = tile;
                    
                    x++;
                }
            }

            currentLine++;
        }
        
        // Trees
        int numTrees = Integer.parseInt(worldSaveLines[currentLine++]);

        String[] treeCoordinates = worldSaveLines[currentLine++].split(",");

        for (int i = 0; i < numTrees; i++) {
            int x = Integer.parseInt(treeCoordinates[i * 2]);
            int y = Integer.parseInt(treeCoordinates[i * 2 + 1]);
            
            trees.add(new Tree(x, y));
        }
        
        // Fires
        int numFires = Integer.parseInt(worldSaveLines[currentLine++]);
        fires = new Fire[numFires];
        String[] fireCoordinates = worldSaveLines[currentLine++].split(",");

        for (int i = 0; i < numFires; i++) {
            int x = Integer.parseInt(fireCoordinates[i * 2]);
            int y = Integer.parseInt(fireCoordinates[i * 2 + 1]);
            
            fires[i] = new Fire(x, y);
        }
        
        // TODO Load items from world save file
        
        return new World(tiles, trees, fires);
    }

    private static String[] parseWorldSave(BufferedReader in) throws IOException {
        String line;
        ArrayList<String> lines = new ArrayList<>();

        while ((line = in.readLine()) != null) {
           if (!line.substring(0, 1).equals("#"))
               lines.add(line);
        }

        String[] array = new String[lines.size()];

        for (int i = 0; i < array.length; i++)
            array[i] = lines.get(i);

        return array;
    }
}
