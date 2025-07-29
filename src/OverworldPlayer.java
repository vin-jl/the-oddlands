import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.image.BufferedImage;

/**
 * @author Vincent Lin
 */

public class OverworldPlayer extends OverworldSprite{
    private int interacting;
    private boolean inDialogue;
    private Item equippedItem;
    private final BufferedImage[][] playerImages;
    private LinkedList<Item> itemInventory;
    private int direction;
    private int curCycle;
    private int nextFrameTime;


    public OverworldPlayer(int worldX, int worldY, String[] imgDir){
        super(worldX, worldY, imgDir[9]);
        curCycle = 0;
        direction = 3;
        playerImages = new BufferedImage[4][3];
        int i = 0;
        int j = 0;
        for (String s : imgDir) {
            if (j >= playerImages[0].length) {
                j = 0;
                i++;
            }
            try {
                playerImages[i][j] = ImageIO.read(new File(s));
            } catch (IOException e) {System.out.println("Player image at " + s + " not found!");}
            j++;
        }
        this.itemInventory = new LinkedList<>();
        this.equippedItem = null;
        this.interacting = 0;
        this.nextFrameTime = 0;
        this.inDialogue = false;
    }

    //sets the dialogue tag
    public void startDialogue(){this.inDialogue = true;}
    public void stopDialogue(){this.inDialogue = false;}

    //makes the player "interact" for a set amount of frames
    public void interact(){this.interacting = CONST.PLAYER_INTERACT_WINDOW;}

    //returns true if the player has pressed the interact button recently
    public boolean isInteracting(){return this.interacting != 0;}

    //stops other interactions
    public void stopInteracting(){this.interacting = 0;}

    //item management
    public void addItem(Item i){itemInventory.add(i);}
    public LinkedList<Item> getItemInventory(){return itemInventory;}
    public Item getEquippedItem(){return equippedItem;}
    public void removeEquippedItem(){equippedItem = null;}
    public void setEquippedItem(int idx){
        if(equippedItem != null){addItem(equippedItem);}
        equippedItem = itemInventory.get(idx);
        itemInventory.remove(idx);
    }

    @Override
    public void update(Overworld ow){
        super.update(ow);
        //change player image based on velocity
        //down image @ index 2
        if(nextFrameTime == 0) {
            if (curCycle == 0 || curCycle == 2) {
                curCycle = 1;
            } else {
                curCycle = 2;
            }
            nextFrameTime = 10;
        }
        if(super.getVelX() == 0 && super.getVelY() == 0){
            curCycle = 0;
        }else{
            if (super.getVelY() > 0) {
                direction = 2;
                //up image @ index 0
            } else if (super.getVelY() < 0) {
                direction = 0;
            }
            //right image @ index 3
            if (super.getVelX() > 0) {
                direction = 3;
                //left image @ index 1
            } else if (super.getVelX() < 0) {
                direction = 1;
            }
        }
        super.setImage(playerImages[direction][curCycle]);
        //reduce the number of frames of "interaction" left
        if(interacting > 0){
            interacting--;
        }
        //reduce the number of frames till next anim frame
        if(nextFrameTime > 0){
            nextFrameTime--;
        }
    }

    public void draw(Graphics g){super.drawAt(g, CONST.PLAYER_SCREEN_X, CONST.PLAYER_SCREEN_Y);}

    public void move(OverworldDirection dir){
        if(!inDialogue){
            //direction player is currently moving to check collisions
            switch (dir) {
                case UP: {
                    super.setVelY(-CONST.PLAYER_SPEED);
                    break;
                }
                case LEFT: {
                    super.setVelX(-CONST.PLAYER_SPEED);
                    break;
                }
                case DOWN: {
                    super.setVelY(CONST.PLAYER_SPEED);
                    break;
                }
                case RIGHT: {
                    super.setVelX(CONST.PLAYER_SPEED);
                    break;
                }
            }
        }
    }

    public void stopMove(String axis){
        switch (axis) {
            case "y": {
                super.setVelY(0);
                break;
            }
            case "x": {
                super.setVelX(0);
                break;
            }
        }
        curCycle = 0;
        nextFrameTime = 0;
    }

}
