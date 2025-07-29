import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class BattleEnemy extends BattleEntity{
    private final BattleSkill[] skills;

    public BattleEnemy(String name, int hp, int mana, int atk, int def, int spd, int luck, String imgDir, BattleSkill[] skills){
        super(name, hp, mana, atk, def, spd, luck, imgDir);
        this.skills = skills;
    }
    //random act each turn based on rng
    public String act(LinkedList<BattleCharacter> targets) {
        Random rd = new Random();
        int choice = rd.nextInt(10) + 1; //returns value from 1-10
        if(choice < 4){return basicAttack(targets);}
        else if(choice <= 8){return useSkill(targets);}
        else if(choice == 9){return buff();}
        else{return heal();}
    }

    public String buff(){
        int buff = super.getBaseAtk()/5;
        super.setAtk(super.getAtk() + buff);
        super.setLuck(super.getLuck() + 5);
        super.setSpd(super.getSpd() + 5);
        return "The enemy " + super.getName() + " buffed themself! Their atk, luck, and spd increased!";
    }

    public String heal(){
        int heal = super.getMaxHp()/5;
        super.setHp(super.getHp() + heal);
        super.setDef(super.getDef() + 5);
        return "The enemy " + super.getName() + " healed themself, recovering " + heal + " hp! Their def increased!";
    }

    public String basicAttack(LinkedList<BattleCharacter> targets) {
        //select a target for the basic attack
        selectTarget(targets);
        BattleEntity target = super.getTargets().get(0);
        String message = "";

        //everyone loves a bit of randomness in their damage
        Random rd = new Random();
        int variance = rd.nextInt((super.getAtk()/10) + 1) - rd.nextInt((super.getAtk()/10) + 1);

        //calculates damage
        int dmg = super.getAtk() - target.getDef() + variance;
        if(target.getDef() > dmg){dmg = 0;}
        else if(crit(target)){dmg = super.getAtk(); message += "A CRITICAL HIT! ";}

        //enemy takes damage, returns battle message
        target.takeDamage(dmg);
        message += this.getName() + " attacked " + target.getName() + "! It dealt " + dmg + " damage!";
        return message;
    }

    public String useSkill(LinkedList<BattleCharacter> targets) {
        //chooses a random skill from its skill list
        Random rd = new Random();
        int rand = rd.nextInt(skills.length);
        BattleSkill skill = skills[rand];
        System.out.println("chose skill " + skill.getName()); //debug

        //if one target, select a target. if target all, add all into targets
        if(skill.getTarget() == BattleTarget.ENEMY_ONE){ //one enemy
            selectTarget(targets);
        }else if(skill.getTarget() == BattleTarget.ENEMY_ALL){ //all enemies
            this.getTargets().addAll(targets);
        }

        //return battle message and use ability
        return skill.use(this, this.getTargets());
    }

    //uses random to select a target
    private void selectTarget(LinkedList<BattleCharacter> targets){
        Random rd = new Random();
        int rand = rd.nextInt(targets.size());
        BattleEntity target = targets.get(rand);
        this.getTargets().add(target);
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(super.getImage(), x, y, null);
        //draw hp
        g.setColor(Color.GRAY);
        g.fillRect(CONST.FRAME_WIDTH/2 - 80, 50, 160, 20);
        g.setColor(Color.RED);
        //get percentage of hp
        double a = (double) super.getHp() / super.getMaxHp();
        //scale the percentage to the bar width
        int b = (int) Math.round(a * 160);
        g.fillRect(CONST.FRAME_WIDTH/2 - 80, 50, b, 20);
        g.setColor(Color.WHITE);
        g.drawRect(CONST.FRAME_WIDTH/2 - 80, 50, 160, 20);
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
    }

}
