import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {

    private final ArrayList<OverworldTile> tiles;
    private final int[][] mapTileIDs;

    public Map(String mapFileDir){
        tiles = new ArrayList<>();
        mapTileIDs = new int[CONST.WORLD_MAX_COLS][CONST.WORLD_MAX_ROWS];
        readMap(mapFileDir);
        readTiles();
    }

    public OverworldTile getTile(int id){return tiles.get(id);}
    public int[][] getMap(){return mapTileIDs;};
    public int getTileID(int col, int row){return mapTileIDs[col][row];}

    public void draw(Graphics g, int playerX, int playerY){
        int colNum = 0;
        int rowNum = 0;
        while(rowNum < CONST.WORLD_MAX_ROWS && colNum < CONST.WORLD_MAX_COLS){
            //find overall position relative to world
            int worldX = colNum * CONST.TILE_SIZE;
            int worldY = rowNum * CONST.TILE_SIZE;

            //find position relative to screen (player)
            int drawX = worldX - playerX + CONST.PLAYER_SCREEN_X;
            int drawY = worldY - playerY + CONST.PLAYER_SCREEN_Y;

            //check if tile would be visible to player, draw if yes
            if((worldX + CONST.TILE_SIZE) > (playerX - CONST.PLAYER_SCREEN_X) &&
               (worldX - CONST.TILE_SIZE) < (playerX + CONST.PLAYER_SCREEN_X) &&
               (worldY + CONST.TILE_SIZE) > (playerY - CONST.PLAYER_SCREEN_Y) &&
               (worldY - CONST.TILE_SIZE) < (playerY + CONST.PLAYER_SCREEN_Y)) {
                g.drawImage(tiles.get(mapTileIDs[colNum][rowNum]).getImage(), drawX, drawY, null);
            }

            colNum++;

            if(colNum == CONST.WORLD_MAX_COLS){
                colNum = 0;
                rowNum++;
            }
        }
    }

    public void readMap(String mapFileDir){
        try {
            Scanner fr = new Scanner(new File(mapFileDir));
            int colNum = 0;
            int rowNum = 0;

            while(rowNum < CONST.WORLD_MAX_ROWS){
                //read one row of tiles
                String line = fr.nextLine();
                String[] nums = line.split(" ");
                while(colNum < CONST.WORLD_MAX_COLS){
                    int tileID = Integer.parseInt(nums[colNum]);
                    mapTileIDs[colNum][rowNum] = tileID;
                    colNum++;
                }

                if(colNum == CONST.WORLD_MAX_COLS){
                    colNum = 0;
                    rowNum++;
                }
            }
        } catch (FileNotFoundException e) {System.out.println("Map not found at " + mapFileDir);}
    }

    private void readTiles(){
        tiles.add(new OverworldTile("resources/grassTile.png", false));
        tiles.add(new OverworldTile("resources/treeTile.png", true));
    }

}
