import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class BattleEntity implements Comparable<BattleEntity>{
    private final String name;
    private int hp;
    private int mana;
    private int atk;
    private int def;
    private int spd;
    private int luck;
    private final int maxHp;
    private final int maxMana;
    private final int baseAtk;
    private final int baseDef;
    private final int baseSpd;
    private final int baseLuck;
    private BufferedImage image;
    private ArrayList<BattleEntity> targets;
    private boolean alive;

    public BattleEntity(String name, int hp, int mana, int atk, int def, int spd, int luck, String imgDir){
        this.name = name;
        this.hp = maxHp = hp;
        this.mana = maxMana = mana;
        this.atk = baseAtk = atk;
        this.def = baseDef = def;
        this.spd = baseSpd = spd;
        this.luck = baseLuck = luck;
        targets = new ArrayList<>();
        alive = true;
        try{
            image = ImageIO.read(new File(imgDir));
        } catch (IOException e) {System.out.println("Battle image not found at " + imgDir);}
    }

    public String getName(){return name;}
    public BufferedImage getImage(){return image;}
    public int getHp(){return hp;}
    public int getMana(){return mana;}
    public int getAtk(){return atk;}
    public int getDef(){return def;}
    public int getSpd(){return spd;}
    public int getLuck(){return luck;}
    public int getMaxHp(){return maxHp;}
    public int getMaxMana(){return maxMana;}
    public int getBaseAtk(){return baseAtk;}
    public int getBaseDef(){return baseDef;}
    public int getBaseSpd(){return baseSpd;}
    public int getBaseLuck(){return baseLuck;}
    public ArrayList<BattleEntity> getTargets() {return targets;}
    public void clearTargets(){targets.clear();}

    public boolean isAlive(){return alive;}
    public void setAlive(boolean alive){this.alive = alive;}

    public void takeDamage(int dmg){if(hp-dmg <=0){hp = 0; alive = false;}else{hp -= dmg;}}
    public void setHp(int hp){
        this.hp = hp;
        if(this.hp > maxHp){this.hp = maxHp;}
        else if(this.hp <= 0){this.hp = 0; alive = false;}
    }
    public void setMana(int mana){this.mana = mana; if(this.mana > maxMana){this.mana = maxMana;}}
    public void setAtk(int atk){this.atk = Math.max(atk, 0);}
    public void setDef(int def){this.def = Math.max(def, 0);}
    public void setSpd(int spd){this.spd = Math.max(spd, 0);}
    public void setLuck(int luck){this.luck = Math.max(luck, 0);}

    public void addTarget(BattleEntity e){targets.add(e);}
    public boolean crit(BattleEntity e){
        int chance = this.luck - e.getLuck();
        Random rd = new Random();
        return rd.nextInt(100)+1 <= chance;
    }

    @Override
    public int compareTo(BattleEntity o) {return o.spd - this.spd;} //reverse the comparison so largest speed goes first
    public abstract void fullReset();


}
