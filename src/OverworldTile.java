public class OverworldTile extends OverworldSprite{

    private final boolean collidable;

    public OverworldTile(String imgDir, boolean collidable) {
        super(0, 0, imgDir);
        this.collidable = collidable;
    }

    public boolean isCollidable(){return collidable;}

}
