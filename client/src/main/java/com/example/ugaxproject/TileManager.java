package com.example.ugaxproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GameView gv;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GameView gv) {
        this.gv = gv;
        tile = new Tile[8];    // Number of tile types in the game
        mapTileNum = new int[gv.maxWorldCol][gv.maxWorldRow];
        getTileImage();
        loadMap("/maps/map01.txt");
    }

    public void getTileImage() {
        for (int i = 0; i < tile.length; i++) {
            tile[i] = new Tile();
        }
        setup(0, "UGA_floor_tile_for_use", false);
        setup(1, "table_with_floor-1", true);
        setup(2, "table_with_floor-2", true);
        setup(3, "uga_chair_1", true);
        setup(4, "uga_chair_2", true);
        setup(5, "walls", true);
        setup(6, "mlh_table_for_use", true);
        setup(7, "powerup_for_use", false);

//        setup(3, "Grass4", false);
//        setup(4, "Grass5", false);
//        setup(5, "Grass6", false);
//        setup(6, "Grass7", false);
//        setup(7, "Grass8", false);
//        setup(8, "Grass9", false);
//        setup(9, "Grass10", false);
//        setup(10, "Grass11", false);
//        setup(11, "Grass12", false);
//        setup(12, "Grass13", false);
//        setup(13, "Stone1", true);
//        setup(14, "Stone2", true);
//        setup(15, "Stone3", true);
//        setup(16, "Stone4", true);
//        setup(17, "Stone5", true);
//        setup(18, "Stone6", true);
//        setup(19, "Stone7", true);
//        setup(20, "Stone8", true);
//        setup(21, "Stone9", true);
//        setup(22, "Water1", true);
//        setup(23, "StoneFloor2", false);
//        setup(24, "StoneFloor", false);

    }

    public void setup(int index, String imageName, boolean collision) {
        tile[index] = new Tile();
        tile[index].image = new Image(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
        tile[index].collision = collision;
    }

    public void loadMap(String filePath) {

        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gv.maxWorldCol && row < gv.maxWorldRow) {
                String line = br.readLine();

                while (col < gv.maxWorldCol) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gv.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void draw(GraphicsContext gc) {

        //g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gv.maxWorldCol && worldRow < gv.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * 128; //  * gv.tileSize
            int worldY = worldRow * 128;

            /*int screenX = worldX - (int) Player.worldX + gp.player.screenX;
            int screenY = worldY - (int) Player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > Player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < Player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > Player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < Player.worldY + gp.player.screenY)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }*/

//            float screenX = worldX - GameView.worldX + gv.screenX;
//            float screenY = worldY - GameView.worldY + gv.screenY;

            float screenX = worldX - GameView.worldX + 384;
            float screenY = worldY - GameView.worldY + 384;

//            float screenX = worldX - gv.startX + gv.screenX;
//            float screenY = worldY - gv.startY + gv.screenY;

//            if (worldX + 128 > GameView.worldX - gv.screenX &&
//                    worldX - 128 < GameView.worldX + gv.screenX &&
//                    worldY + 128 > GameView.worldY - gv.screenY &&
//                    worldY - 128 < GameView.worldY + gv.screenY)
            {
//                gc.drawImage(tile[tileNum].image, screenX, screenY, 128, 128);
                gc.drawImage(tile[tileNum].image, screenX, screenY, 128, 128);
            }
            worldCol++;

            if (worldCol == gv.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

    }

}

