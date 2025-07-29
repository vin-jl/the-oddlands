import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

public abstract class OverworldSprite {
    private int worldX;
    private int worldY;
    private final ArrayList<BufferedImage> images;
    private final int width;
    private final int height;
    private int velX;
    private int velY;
    private Rectangle hitbox;
    private int curImage;
    boolean mirrored;

    public OverworldSprite(int worldX, int worldY, String imgDir){
        images = new ArrayList<>();
        this.worldX = worldX;
        this.worldY = worldY;
        this.velX = 0;
        this.velY = 0;
        this.curImage = 0;
        try{
            images.add(ImageIO.read(new File(imgDir)));
        }catch (IOException ex) {System.out.println("Sprite image not found at " + imgDir);}
        this.width = images.get(0).getWidth();
        this.height = images.get(0).getHeight();
        this.hitbox = new Rectangle(worldX, worldY, width, height);
        this.mirrored = false;
    }

    public OverworldSprite(int worldX, int worldY, String[] imgDir){
        images = new ArrayList<>();
        this.worldX = worldX;
        this.worldY = worldY;
        this.velX = 0;
        this.velY = 0;
        this.curImage = 0;
        for(String dir : imgDir){
            try{
                images.add(ImageIO.read(new File(dir)));
            }
            catch (IOException ex) {System.out.println("File not found!");}
        }
        this.width = images.get(0).getWidth();
        this.height = images.get(0).getHeight();
        this.hitbox = new Rectangle(worldX, worldY, width, height);
        this.mirrored = false;
    }

    public int getWorldX(){return this.worldX;}
    public int getWorldY(){return this.worldY;}
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}
    public int getVelX(){return this.velX;}
    public int getVelY(){return this.velY;}
    public int getCurImage(){return this.curImage;}
    public BufferedImage getImage(){return images.get(0);}
    public ArrayList<BufferedImage> getImages(){return this.images;}
    public Rectangle getHitbox(){return this.hitbox;}
    public void nextImage(){if(images.size() > 1){if(curImage != images.size()-1){curImage++;}else{curImage=0;}}}
    public void setHitbox(Rectangle hitbox){this.hitbox = hitbox;}
    public void setCurImage(int n){this.curImage = n;}
    public void setImage(BufferedImage image){this.images.set(0, image); this.curImage = 0;}
    public void setWorldX(int worldX){this.worldX = worldX; this.hitbox.x = worldX;}
    public void setWorldY(int worldY){this.worldY = worldY; this.hitbox.y = worldY;}
    public void setVelX(int vel){this.velX = vel;}
    public void setVelY(int vel){this.velY = vel;}

    //draws the sprite if the x and y pos is on player screen
    public void draw(Graphics g, int playerX, int playerY){
        //find position relative to screen (player)
        int drawX = worldX - playerX + CONST.PLAYER_SCREEN_X;
        int drawY = worldY - playerY + CONST.PLAYER_SCREEN_Y;

        //check if sprite would be visible to player, draw if yes
        if(onPlayerScreen(playerX, playerY)) {
            if(!mirrored) {
                g.drawImage(images.get(curImage), drawX, drawY, null);
            }else{
                g.drawImage(images.get(curImage), drawX+width, drawY, -width, height, null);
            }
        }
    }

    //draws the image at a new x and y position (used mostly for drawing player)
    public void drawAt(Graphics g, int xpos, int ypos){g.drawImage(images.get(curImage), xpos, ypos, null);}

    public boolean onPlayerScreen(int playerX, int playerY){
        return ((worldX + CONST.TILE_SIZE) > (playerX - CONST.PLAYER_SCREEN_X)) &&
            ((worldX - CONST.TILE_SIZE) < (playerX + CONST.PLAYER_SCREEN_X)) &&
            ((worldY + CONST.TILE_SIZE) > (playerY - CONST.PLAYER_SCREEN_Y)) &&
            ((worldY - CONST.TILE_SIZE) < (playerY + CONST.PLAYER_SCREEN_Y));}

    private void move(Overworld ow){
        //get x and y positions of hitbox
        int hitboxLeftX = this.hitbox.x;
        int hitboxRightX = this.hitbox.x + this.hitbox.width;
        int hitboxTopY = this.hitbox.y;
        int hitboxBottomY = this.hitbox.y + this.hitbox.height;

        //get row and col of tiles that will be collided with
        int hitboxColLeft = (hitboxLeftX + this.velX)/CONST.TILE_SIZE;
        int hitboxColRight = (hitboxRightX + this.velX)/CONST.TILE_SIZE;
        int hitboxRowTop = (hitboxTopY + this.velY)/CONST.TILE_SIZE;
        int hitboxRowBottom = (hitboxBottomY + this.velY)/CONST.TILE_SIZE;

        /*
        2 separate possible collidable tiles checked at a time; the player can only collide with 2 tiles at a time
        if the tile is a collidable type, and the next move would make the entity go into the wall, set the entity's
        x and y positions to right up against the tile
        */

        int tileID1;
        int tileID2;

        //moving right
        if(this.velX > 0){
            tileID1 = ow.getMap().getTileID(hitboxColRight, hitboxRowTop);
            tileID2 = ow.getMap().getTileID(hitboxColRight, hitboxRowBottom);

            boolean collideTopTile = ow.getMap().getTile(tileID1).isCollidable();
            boolean collideBottomTile = ow.getMap().getTile(tileID2).isCollidable();

            if(collideTopTile || collideBottomTile){
                this.velX = 0;
                this.worldX = (hitboxColRight) * CONST.TILE_SIZE - this.hitbox.width - 1;
                this.hitbox.x = (hitboxColRight) * CONST.TILE_SIZE - this.hitbox.width - 1;
            }
            collideObjects(ow.getObjects(), OverworldDirection.RIGHT);
        }

        //moving left
        else if(this.velX < 0) {
            tileID1 = ow.getMap().getTileID(hitboxColLeft, hitboxRowTop);
            tileID2 = ow.getMap().getTileID(hitboxColLeft, hitboxRowBottom);

            boolean collideTopTile = ow.getMap().getTile(tileID1).isCollidable();
            boolean collideBottomTile = ow.getMap().getTile(tileID2).isCollidable();

            if(collideTopTile || collideBottomTile){
                this.velX = 0;
                this.worldX = (hitboxColLeft + 1) * CONST.TILE_SIZE;
                this.hitbox.x = (hitboxColLeft + 1) * CONST.TILE_SIZE;
            }
            collideObjects(ow.getObjects(), OverworldDirection.LEFT);
        }

        //moving down
        if(this.velY > 0){
            tileID1 = ow.getMap().getTileID(hitboxColLeft, hitboxRowBottom);
            tileID2 = ow.getMap().getTileID(hitboxColRight, hitboxRowBottom);

            boolean collideLeftTile = ow.getMap().getTile(tileID1).isCollidable();
            boolean collideRightTile = ow.getMap().getTile(tileID2).isCollidable();

            if(collideLeftTile || collideRightTile){
                this.velY = 0;
                this.worldY = (hitboxRowTop+1) * CONST.TILE_SIZE - this.hitbox.height - 1;
                this.hitbox.y = (hitboxRowTop+1) * CONST.TILE_SIZE - this.hitbox.height - 1;
            }
            collideObjects(ow.getObjects(), OverworldDirection.DOWN);
        }

        //moving up
        else if(this.velY < 0){
            tileID1 = ow.getMap().getTileID(hitboxColLeft, hitboxRowTop);
            tileID2 = ow.getMap().getTileID(hitboxColRight, hitboxRowTop);

            boolean collideLeftTile = ow.getMap().getTile(tileID1).isCollidable();
            boolean collideRightTile = ow.getMap().getTile(tileID2).isCollidable();

            if(collideLeftTile || collideRightTile){
                this.velY = 0;
                this.worldY = (hitboxRowBottom) * CONST.TILE_SIZE;
                this.hitbox.y = (hitboxRowBottom) * CONST.TILE_SIZE;
            }
            collideObjects(ow.getObjects(), OverworldDirection.UP);
        }

        this.worldX += this.velX;
        this.hitbox.x += this.velX;

        this.worldY += this.velY;
        this.hitbox.y += this.velY;
    }

    public void update(Overworld ow){
        move(ow);
    }

    //check if colliding with another rectangle (hitbox)
    public boolean collide(Rectangle o){
        if(this.hitbox.y > o.y+o.height ||
                this.hitbox.y+this.hitbox.height < o.y){
            return false;
        }
        return this.hitbox.x <= o.x + o.width &&
                this.hitbox.x + this.hitbox.width >= o.x;
    }

    //check if collide with any objects (hitbox going into other hitbox) like doors
    public void collideObjects(LinkedList<OverworldObject> objects, OverworldDirection dir){
        for(OverworldObject o : objects){
            if(o.isCollidable() && o.collide(this.getHitbox())){
                switch(dir){
                    case UP :{
                        this.velY = 0;
                        this.worldY = o.getHitbox().y + o.getHitbox().height+1;
                        this.hitbox.y = o.getHitbox().y + o.getHitbox().height+1;
                        break;
                    }
                    case DOWN :{
                        this.velY = 0;
                        this.worldY = o.getHitbox().y - this.hitbox.height-1;
                        this.hitbox.y = o.getHitbox().y - this.hitbox.height-1;
                        break;
                    }
                    case LEFT :{
                        this.velX = 0;
                        this.worldX = o.getHitbox().x + o.getHitbox().width+1;
                        this.hitbox.x = o.getHitbox().x + o.getHitbox().width+1;
                        break;
                    }
                    case RIGHT :{
                        this.velX = 0;
                        this.worldX = o.getHitbox().x - this.hitbox.width-1;
                        this.hitbox.x = o.getHitbox().x - this.hitbox.width-1;
                        break;
                    }
                }
            }
        }
    }

}
