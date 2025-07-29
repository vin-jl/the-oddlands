public class OverworldEnemy extends OverworldSprite{
    BattleEnemy enemy;
    public OverworldEnemy(int worldX, int worldY, String[] imgDir, BattleEnemy enemy){
        super(worldX, worldY, imgDir);
        this.enemy = enemy;
    }

    public BattleEnemy getEnemy(){return this.enemy;}

    private void chasePlayer(OverworldPlayer player){
        if(onPlayerScreen(player.getWorldX(), player.getWorldY())){
            if(player.getWorldX() + CONST.TILE_SIZE/2 > super.getWorldX()){
                super.setVelX(1);
                mirrored = false;
            }else if(player.getWorldX() + CONST.TILE_SIZE/2 < super.getWorldX()){
                super.setVelX(-1);
                mirrored = true;
            }

            if(player.getWorldY() + CONST.TILE_SIZE/2 > super.getWorldY()){
                super.setVelY(1);
            }else if(player.getWorldY() + CONST.TILE_SIZE/2 < super.getWorldY()){
                super.setVelY(-1);
            }
        }
    }

    public void update(Overworld ow, OverworldPlayer player){
        super.update(ow);
        chasePlayer(player);
        super.nextImage();
    }
}
