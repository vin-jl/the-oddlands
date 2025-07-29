import java.util.*;
public class BattleSkill {
    private final String name;
    private final String description;
    private final BattleSkillType type;
    private final BattleTarget target; //skill type, see BattleTarget Enum
    private final String[] targetedStats;
    private final int[] statValues;
    private final double damageMultiplier;
    private final int manaCost;


    public BattleSkill(String name, String description, BattleSkillType type, BattleTarget target, String[] targetedStats, int[] statValues, double damageMultiplier, int manaCost){
        this.name = name;
        this.description = description;
        this.type = type;
        this.target = target;
        this.targetedStats = targetedStats;
        this.statValues = statValues;
        this.damageMultiplier = damageMultiplier;
        this.manaCost = manaCost;
    }

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public BattleSkillType getType(){
        return type;
    }
    public BattleTarget getTarget(){
        return target;
    }
    public String[] getTargetedStats(){
        return targetedStats;
    }
    public int[] getValues(){
        return statValues;
    }
    public int getCost(){
        return manaCost;
    }

    public String use(BattleEntity user, ArrayList<BattleEntity> targets){
        if (type == BattleSkillType.ATTACK){ return attackSkill(user, targets); }
        else if (type == BattleSkillType.SUPPORT){ return supportSkill(user, targets); }
        else {return debuffSkill(user, targets); }
    }

    public String attackSkill(BattleEntity user, ArrayList<BattleEntity> targets){
        String message = "";
        for (BattleEntity enemy : targets){

            //everyone loves a bit of randomness in their damage
            Random rd = new Random();
            int dmg = rd.nextInt((user.getAtk()/10) + 1) - rd.nextInt((user.getAtk()/10) + 1);

            //check if will crit, ignore defence if yes
            if (user.crit(enemy)){dmg += (int)(damageMultiplier*user.getAtk()); message += "A CRITICAL HIT! ";}
            else{dmg += (int)(damageMultiplier*user.getAtk() - enemy.getDef());}
            enemy.takeDamage(dmg);

            // message if one target
            if (targets.size() == 1){
                BattleEntity a = targets.get(0);
                if (dmg < 0){dmg = 0;}
                message += user.getName() + " used " + this.name + "! " + a.getName() + " took " + dmg + " damage!";
            }
        }

        //give self buffs if buffing ability
        if (target == BattleTarget.ENEMY_AND_SELF) {
            applyStatChanges(user, true);
        }
        //if enemy uses an attack that hits all characters
        if (target == BattleTarget.ENEMY_ALL){
            message += user.getName() + " used " + this.name + "! All allies took damage!";
        }
        return message;
    }

    public String supportSkill(BattleEntity user, ArrayList<BattleEntity> targets){
        String message = user.getName() + " used " + this.name;
        //targets will always be a BattleCharacter as BattleEnemies does not have any support abilities
        for (BattleEntity ally : targets){
            //apply buffs each alive target
            if (ally.isAlive()){
                applyStatChanges(ally, true);
            }
        }

        //all allies (only one stat changed)
        if (target == BattleTarget.ALLY_ALL){
            message += "! All allies " + targetedStats[0].toUpperCase() + " increased by " + statValues[0] + "!";
        }

        //one ally
        // if there is only one ally and that ally is dead
        else if (targets.size()==1 && !targets.get(0).isAlive()){
            message += ", but " + targets.get(0).getName() + " is knocked out...";
        }

        //if the skill targets 2 stats
        else if (targetedStats.length == 2){
            BattleEntity target = targets.get(0);
            message += "! " + target.getName() + "'s " + targetedStats[0].toUpperCase() + " and " + targetedStats[1].toUpperCase() + " increased by " + statValues[0] + "!";
        }

        //if the skill boosts all stats
        else if (targetedStats.length > 2){
            BattleEntity target = targets.get(0);
            message += "! All of " + target.getName() + "'s stats increased!";
        }

        //if the character boosts 1 stat for 1 character
        else{
            BattleEntity target = targets.get(0);
            message += "! " + target.getName() + "'s " + targetedStats[0].toUpperCase() + " increased by " + statValues[0] + "!";
        }
        return message;
    }

    public String debuffSkill(BattleEntity user, ArrayList<BattleEntity> targets){
        //do a bit of damage to the targets
        int curAtk = user.getAtk();
        user.setAtk(curAtk/3);
        String message = attackSkill(user, targets);
        user.setAtk(curAtk);

        //loop through all targets and debuff
        for (BattleEntity target : targets){
            applyStatChanges(target, false);
        }

        //each debuff skill targets a max of 2 stats
        if (targetedStats.length > 1){
            message += " Their " + targetedStats[0].toUpperCase() + " and " + targetedStats[1].toUpperCase() + " dropped!";
        }
        else {
            message += " Their " + targetedStats[0].toUpperCase() + " dropped by " + statValues[0] + "!";
        }
        return message;
    }

    private void applyStatChanges(BattleEntity target, boolean isBuff){
        for (int i = 0; i < targetedStats.length; i++) {
            String stat = targetedStats[i];
            int value = statValues[i];
            //if stat changes are applied as debuff
            if(!isBuff){value = -value;}
            switch (stat) {
                case "atk": {
                    target.setAtk(target.getAtk() + value);
                    break;
                }
                case "mana": {
                    target.setMana(target.getMana() + value);
                    break;
                }
                case "hp": {
                    target.setHp(target.getHp() + value);
                    break;
                }
                case "def": {
                    target.setDef(target.getDef() + value);
                    break;
                }
                case "spd": {
                    target.setSpd(target.getSpd() + value);
                    break;
                }
                case "luck": {
                    target.setLuck(target.getLuck() + value);
                    break;
                }
            }
        }
    }

}
