import java.awt.*;

public class OverworldObject extends OverworldSprite{
    private final String objectType;
    private final boolean isCollectable;
    private OverworldObject linkedTarget;
    private Item objectItem;
    private Rectangle interactBox;

    public OverworldObject(int worldX, int worldY, String imgDir, String objectType, boolean isCollectable, Item objectItem) {
        super(worldX, worldY, imgDir);
        super.setHitbox(new Rectangle(worldX + 1, worldY + 1, getWidth() - 2, getHeight() - 2));
        interactBox = new Rectangle(worldX - 2, worldY - 2, getWidth() + 4, getHeight() + 4);
        this.objectType = objectType;
        this.isCollectable = isCollectable;
        this.objectItem = objectItem;
    }

    public OverworldObject(int worldX, int worldY, String[] imgDir, String objectType, boolean isCollectable, Item objectItem) {
        super(worldX, worldY, imgDir);
        super.setHitbox(new Rectangle(worldX + 1, worldY + 1, getWidth() - 2, getHeight() - 2));
        interactBox = new Rectangle(worldX - 2, worldY - 2, getWidth() + 4, getHeight() + 4);
        this.objectType = objectType;
        this.isCollectable = isCollectable;
        this.objectItem = objectItem;
    }

    public boolean isCollidable(){return objectType.equals("door");}
    public String getObjectType(){return objectType;}
    public void setLinkedTarget(OverworldObject o){this.linkedTarget = o;}

    public Rectangle getInteractBox(){return interactBox;}

    public OverworldObject interact(OverworldPlayer p){
        //collectable items
        if(isCollectable){
            p.addItem(this.objectItem);
            return this;
        }

        //other items
        switch(objectType){
            case "button": {
                super.setCurImage(1);
                return linkedTarget;
            }
            case "door": {
                if(p.getEquippedItem() == null){return null;}
                if(p.getEquippedItem().equals(CONST.ITEM_KEY)){
                    return this;
                }
                return null;
            }
        }
        return null;
    }

}
