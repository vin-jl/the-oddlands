import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BattleCharacter extends BattleEntity{
    private boolean ready; //true if character has acted during the turn
    private BattleAction actionChoice;
    private final BattleSkill[] skills;
    private int skillChoice;

    public BattleCharacter(String name, int hp, int mana, int atk, int def, int spd, int luck, String imgDir, BattleSkill[] skills){
        super(name, hp, mana, atk, def, spd, luck, imgDir);
        this.skills = skills;
        ready = false;
        actionChoice = BattleAction.ATTACK;
        skillChoice = 0;
    }

    public void setReady(boolean ready){this.ready = ready;}
    public boolean isReady(){return ready;}
    public void setSkillChoice(int n){skillChoice = n; ready = true;}
    public void setActionChoice(BattleAction ba){actionChoice = ba; if(ba != BattleAction.SKILL){ready = true;}}
    public BattleSkill[] getSkills(){return skills;}

    //act depending on set actionChoice
    public String act() {
        if(actionChoice == BattleAction.ATTACK){return basicAttack(super.getTargets());}
        else if(actionChoice == BattleAction.SKILL && skillChoice != 0){return useSkill(super.getTargets());}
        else if(actionChoice == BattleAction.DEFEND){return defend();}
        return "shouldn't get here!";
    }

    public String basicAttack(ArrayList<BattleEntity> targets) {
        String message = "";
        BattleEntity target = targets.get(0); //only can be one enemy at a time

        //everyone loves a bit of randomness in their damage
        Random rd = new Random();
        int variance = rd.nextInt((super.getAtk()/10) + 1) - rd.nextInt((super.getAtk()/10) + 1);

        //calculates damage
        int dmg = super.getAtk() - target.getDef() + variance;
        if(target.getDef() > dmg){dmg = 0;}
        else if(crit(target)){dmg = super.getAtk(); message += "A CRITICAL HIT! ";}

        //make target take damage and return battle message
        target.takeDamage(dmg);
        super.setMana(super.getMana()+10);
        message += this.getName() + " attacked the enemy! It dealt " + dmg + " damage!";
        return message;
    }

    public String useSkill(ArrayList<BattleEntity> targets) {
        super.setMana(super.getMana() - skills[skillChoice-1].getCost());
        return skills[skillChoice-1].use(this, targets);
    }

    public String defend(){
        super.setMana(super.getMana() + 50);
        super.setHp(super.getHp() + 30);
        return super.getName() + " defended! They gained 30 hp and 50 mana!";
    }

    //reset all choices for end of each turn
    public void resetChoices(){
        actionChoice = BattleAction.ATTACK;
        skillChoice = 0;
        super.clearTargets();
        ready = false;
    }

    public void fullReset(){
        super.clearTargets();
        super.setAlive(true);
        super.setHp(super.getMaxHp());
        super.setMana(super.getMaxMana());
        super.setAtk(super.getBaseAtk());
        super.setDef(super.getBaseDef());
        super.setLuck(super.getBaseLuck());
        super.setSpd(super.getBaseSpd());
        resetChoices();
    }


    public void drawImage(Graphics g, int x, int y) {
        g.drawImage(super.getImage(), x, y, null);
    }

    //draw hp bar with numbers
    public void drawHp(Graphics g, int x, int y, int size){
        g.setColor(Color.GRAY);
        g.fillRect(x, y, size, 10);
        g.setColor(Color.RED);
        double a = (double)super.getHp()/super.getMaxHp();
        int b = (int)Math.round(a*size);
        g.fillRect(x, y, b, 10);
        g.setColor(Color.WHITE);
        g.drawString(super.getHp()+"/"+super.getMaxHp(), x+5, y+7);
    }

    //draw mana bar with numbers
    public void drawMana(Graphics g, int x, int y, int size){
        g.setColor(Color.GRAY);
        g.fillRect(x, y, size, 10);
        g.setColor(Color.BLUE);
        double a = (double)super.getMana()/super.getMaxMana();
        int b = (int)Math.round(a*size);
        g.fillRect(x, y, b, 10);
        g.setColor(Color.WHITE);
        g.drawString(super.getMana()+"/"+super.getMaxMana(), x+5, y+8);
    }

}
