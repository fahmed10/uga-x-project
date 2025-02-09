package com.example.ugaxproject;

import entities.Player;

public class CollisionChecker {

    GameView gv;

    public CollisionChecker(GameView gv) {
        this.gv = gv;
    }

    public boolean checkTile(Player player)
    {
        double worldX = GameView.worldX;
        double worldY = GameView.worldY;

        int entityLeftWorldX = (int)worldX + player.solidArea.x;
        int entityRightWorldX = (int)worldX + player.solidArea.x + player.solidArea.width;
        int entityTopWorldY = (int)worldY + player.solidArea.y;
        int entityBottomWorldY = (int)worldY + player.solidArea.y + player.solidArea.height;

        int entityLeftCol = entityLeftWorldX/ 128;
        int entityRightCol = entityRightWorldX/ 128;
        int entityTopRow = entityTopWorldY/ 128;
        int entityBottomRow = entityBottomWorldY/ 128;

        int tileNum1, tileNum2, tileNum3;

            switch (player.direction)
            {
                case Direction.UP:
                    entityTopRow = (int) (entityTopWorldY - player.speed) / 128;
                    tileNum1 = gv.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gv.tileManager.mapTileNum[entityRightCol][entityTopRow];
                    if (gv.tileManager.tile[tileNum1].collision || gv.tileManager.tile[tileNum2].collision) {
                        player.yCollisionOn = true;
                    }
                    break;
                case Direction.DOWN:
                    entityBottomRow = (int) (entityBottomWorldY + player.speed)/ 128;
                    tileNum1 = gv.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gv.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                    if (gv.tileManager.tile[tileNum1].collision || gv.tileManager.tile[tileNum2].collision) {
                        player.yCollisionOn = true;
                    }
                    break;
                case Direction.LEFT:
                    entityLeftCol = (int) (entityLeftWorldX - player.speed)/ 128;
                    tileNum1 = gv.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gv.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                    if (gv.tileManager.tile[tileNum1].collision || gv.tileManager.tile[tileNum2].collision) {
                        player.xCollisionOn = true;
                    }
                    break;
                case Direction.RIGHT:
                    entityRightCol = (int) (entityRightWorldX + player.speed)/ 128;
                    tileNum1 = gv.tileManager.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gv.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                    if (gv.tileManager.tile[tileNum1].collision || gv.tileManager.tile[tileNum2].collision) {
                        player.xCollisionOn = true;
                    }
                    break;
//                case "upLeft":
//                    entityTopRow = (int) (entityTopWorldY - player.speed)/ 128;
//                    entityLeftCol = (int) (entityLeftWorldX - player.speed)/ 128;
//                    tileNum1 = gv.tileManager.mapTileNum[entityRightCol][entityTopRow];
//                    tileNum2 = gv.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
//                    tileNum3 = gv.tileManager.mapTileNum[entityLeftCol][entityTopRow];
//                    if (gv.tileManager.tile[tileNum1].collision) {
//                        player.yCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum2].collision) {
//                        player.xCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum3].collision &&
//                            !gv.tileManager.tile[tileNum1].collision && !gv.tileManager.tile[tileNum2].collision) {
//                        player.yCollisionOn = true;
//                        player.xCollisionOn = true;
//                    }
//                    break;
//                case "upRight":
//                    entityTopRow = (int)  (entityTopWorldY - player.speed)/ 128;
//                    entityRightCol = (int)  (entityRightWorldX + player.speed)/ 128;
//                    tileNum1 = gv.tileManager.mapTileNum[entityLeftCol][entityTopRow];
//                    tileNum2 = gv.tileManager.mapTileNum[entityRightCol][entityBottomRow];
//                    tileNum3 = gv.tileManager.mapTileNum[entityRightCol][entityTopRow];
//                    if (gv.tileManager.tile[tileNum1].collision) {
//                        player.yCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum2].collision) {
//                        player.xCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum3].collision &&
//                            !gv.tileManager.tile[tileNum1].collision && !gv.tileManager.tile[tileNum2].collision) {
//                        player.yCollisionOn = true;
//                        player.xCollisionOn = true;
//                    }
//                    break;
//                case "downLeft":
//                    entityBottomRow = (int)  (entityBottomWorldY + player.speed)/ 128;
//                    entityLeftCol = (int)  (entityLeftWorldX - player.speed)/ 128;
//                    tileNum1 = gv.tileManager.mapTileNum[entityRightCol][entityBottomRow];
//                    tileNum2 = gv.tileManager.mapTileNum[entityLeftCol][entityTopRow];
//                    tileNum3 = gv.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
//                    if (gv.tileManager.tile[tileNum1].collision) {
//                        player.yCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum2].collision) {
//                        player.xCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum3].collision &&
//                            !gv.tileManager.tile[tileNum1].collision && !gv.tileManager.tile[tileNum2].collision) {
//                        player.yCollisionOn = true;
//                        player.xCollisionOn = true;
//                    }
//                    break;
//                case "downRight":
//                    entityBottomRow = (int)  (entityBottomWorldY + player.speed)/ 128;
//                    entityRightCol = (int)  (entityRightWorldX + player.speed)/ 128;
//                    tileNum1 = gv.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
//                    tileNum2 = gv.tileManager.mapTileNum[entityRightCol][entityTopRow];
//                    tileNum3 = gv.tileManager.mapTileNum[entityRightCol][entityBottomRow];
//                    if (gv.tileManager.tile[tileNum1].collision) {
//                        player.yCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum2].collision) {
//                        player.xCollisionOn = true;
//                    }
//                    if (gv.tileManager.tile[tileNum3].collision &&
//                            !gv.tileManager.tile[tileNum1].collision && !gv.tileManager.tile[tileNum2].collision) {
//                        player.yCollisionOn = true;
//                        player.xCollisionOn = true;
//                    }
//                    break;
            }
        return player.xCollisionOn || player.yCollisionOn;
    }

}