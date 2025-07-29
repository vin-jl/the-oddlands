import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Battle {
    LinkedList<BattleCharacter> aliveCharacters;
    BattleCharacter[] allCharacters;
    int curCharacter;
    BattleEnemy enemy;
    PriorityQueue<BattleEntity> turnOrder;

    BattleStage status;
    BattleAction menuChoice;
    int skillChoice;
    int targetChoice;

    boolean nextDialogue;
    String actionDialogue;
    boolean lastDialogue;

    private boolean battleComplete;

    //the reason each character is passed into here instead of resetting is because I want to add persistent characters over battles later
    public Battle(BattleCharacter[] characters, BattleEnemy enemy){
        allCharacters = characters;
        aliveCharacters = new LinkedList<>();
        aliveCharacters.addAll(Arrays.asList(characters));
        this.enemy = enemy;
        turnOrder = new PriorityQueue<>();
        curCharacter = 1;

        status = BattleStage.SELECTION;
        menuChoice = BattleAction.ATTACK;
        skillChoice = 0;
        targetChoice = 0;
        nextDialogue = false;
        lastDialogue = false;
        actionDialogue = "";
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, CONST.FRAME_WIDTH, CONST.FRAME_HEIGHT);
        //if not in stats screen, draw enemy
        if (status != BattleStage.STATS){
            enemy.draw(g, CONST.FRAME_WIDTH/2 - (enemy.getImage().getWidth()/2), 100);
        }else {
            displayStats(g);
        }

        if (status != BattleStage.STATS && status != BattleStage.BATTLE){
            mainMenu(g);
        }

        switch(status){
            case SKILL: {skillMenu(g, allCharacters[curCharacter-1]); break;}
            case TARGET: {targetMenu(g); break;}
            case BATTLE: {battlePhase(g); break;}
            case WIN:
            case LOSE: {
                for(BattleCharacter c : allCharacters){
                    c.fullReset();
                }
                enemy.fullReset();
                battleComplete = true;
                break;
            }
        }
    }

    public void update(){
        // checks for dead characters
        for (int i = 0; i < aliveCharacters.size(); i++){
            if (!aliveCharacters.get(i).isAlive()){
                aliveCharacters.remove(i);
                i--;
            }
        }
    }

    public boolean isBattleComplete(){return battleComplete;}

    private Font setFont(boolean isTitle, boolean isBold, int size){
        int bold;
        if(isBold) {bold = Font.BOLD;}
        else{bold = Font.PLAIN;}
        if(isTitle){
            return new Font("Arial Black", bold, size);
        }else{
            return new Font("Georgia", bold, size);
        }
    }

    private void mainMenu(Graphics g){
        int x = 5;

        // loops through all characters and draws image, hp/mana bars, and main selection options
        for (int i = 0; i < 4; i++){
            BattleCharacter character = allCharacters[i];

            // if this character is the active character
            if (i == curCharacter - 1){
                // draw boxes
                g.setColor(Color.BLACK);
                g.fillRect(x, 400, 316, 205);
                g.setColor(Color.WHITE);
                g.drawRect(x, 400, 316, 205);

                //draw character portrait
                character.drawImage(g, x, 315);

                //draw name
                g.setFont(setFont(true, true, 20));
                g.setColor(Color.WHITE);
                g.drawString((i+1) + " | "+character.getName().toUpperCase(), x+90, 380);

                //draw hp, mana
                g.setFont(setFont(false, false, 10));
                character.drawHp(g, x + 30, 420, 120);
                character.drawMana(g, x + 165, 420, 120);

                //draw main menu options
                g.setFont(setFont(false, false, 24));
                g.setColor(Color.WHITE);
                g.drawString("attack", x+60, 490);
                g.drawString("skills", x+200, 490);
                g.drawString("stats", x+70, 550);
                g.drawString("defend", x+190, 550);
                drawMenuTriangle(g, x + 40);

                x += 316;
            }

            // if this character is not the active character
            else{
                //draw box
                g.setColor(Color.BLACK);
                g.fillRect(x, 455, 158, 150);
                g.setColor(Color.WHITE);
                g.drawRect(x, 455, 158, 150);

                //draw name and number
                g.setFont(setFont(true, true, 16));
                g.drawString((i+1) + " | " + character.getName().toUpperCase(), x+8, 440);

                //draw character portrait
                character.drawImage(g, x+40, 470);

                //draw hp, mana
                g.setFont(setFont(false, false, 10));
                character.drawHp(g, x+35, 560, 90);
                character.drawMana(g, x+35, 580, 90);

                //draw player status, green circle if ready, gray if not
                if (character.isReady()){g.setColor(Color.GREEN);}
                else{g.setColor(Color.GRAY);}
                g.fillOval(x+112, 465, 15, 15);
                x += 158;
            }
        }
    }

    private void drawMenuTriangle(Graphics g, int x){
        g.setColor(Color.WHITE);
        switch(menuChoice){
            case ATTACK:
                g.fillPolygon(new int[]{x, x-5, x-5}, new int[]{482, 477, 487}, 3);
                break;
            case SKILL:
                g.fillPolygon(new int[]{x+130, x+125, x+125}, new int[]{482, 477, 487}, 3);
                break;
            case STATS:
                g.fillPolygon(new int[]{x, x-5, x-5}, new int[]{540, 545, 535}, 3);
                break;
            case DEFEND:
                g.fillPolygon(new int[]{x+130, x+125, x+125}, new int[]{543, 548, 538}, 3);
                break;
        }
    }


    private void skillMenu(Graphics g, BattleCharacter character){
        int x = 540;
        g.setColor(Color.BLACK);
        g.fillRect(x, 20, 240, 335);
        g.setColor(Color.WHITE);
        g.drawRect(x, 20, 240, 335);
        BattleSkill[] skills = character.getSkills();
        drawSkillTriangle(g, x+25);

        for (int i = 0; i < 4; i++){
            int y = i*75;
            //write skill name
            g.setFont(setFont(false, true, 15));
            g.drawString(skills[i].getName(), x+40, y+60);

            //write skill description
            g.setFont(setFont(false, false, 13));
            Scanner s = new Scanner(skills[i].getDescription());
            String line = "";
            int lineNumber = 0;
            while (s.hasNext()){ // split into multiple lines if too long
                line += s.next() + " ";
                if (line.length()>25 || !s.hasNext()){
                    g.drawString(line, x+40, y+lineNumber+80);
                    line = "";
                    lineNumber += 15;
                }
            }

            // write mana cost, white if has enough mana, red if not enough mana
            if (allCharacters[curCharacter-1].getMana() < skills[i].getCost()){g.setColor(Color.RED);}
            g.drawString(Integer.toString(skills[i].getCost()), x+190, y+60);
        }
    }

    private void drawSkillTriangle(Graphics g, int x){
        g.fillPolygon(new int[]{x, x-5, x-5}, new int[]{70+((skillChoice-1)*75), 65+((skillChoice-1)*75), 75+((skillChoice-1)*75)}, 3);
    }


    private void displayStats(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(30,50,452,284);

        //draw stats of each character
        for (int i = 0; i < 4; i++){
            BattleCharacter character = allCharacters[i];
            int x = i*205;
            int y = 155;
            g.setColor(Color.WHITE);
            g.setFont(setFont(false, false, 10));
            character.drawImage(g, x+50, y);

            character.drawHp(g,x+48,y+105,86);
            character.drawMana(g,x+48,y+130,86);

            g.setFont(setFont(false, false, 20));
            g.drawString("atk - "+character.getAtk(), x+58, y+183);
            g.drawString("def - "+character.getDef(), x+58, y+213);
            g.drawString("spd - "+character.getSpd(), x+58, y+243);
            g.drawString("luck - "+character.getLuck(), x+58, y+273);

            // draw red/blue arrows if stats are buffed or debuffed
            if (character.getAtk() > character.getBaseAtk()){g.setColor(Color.RED); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+180, y+175, y+180}, 3);}
            else if (character.getAtk() < character.getBaseAtk()){g.setColor(Color.BLUE); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+175, y+180, y+175}, 3);}
            if (character.getDef() > character.getBaseDef()){g.setColor(Color.RED); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+210, y+205, y+210}, 3);}
            else if (character.getDef() < character.getBaseDef()){g.setColor(Color.BLUE); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+205, y+210, y+205}, 3);}
            if (character.getSpd() > character.getBaseSpd()){g.setColor(Color.RED); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+240, y+235, y+240}, 3);}
            else if (character.getSpd() < character.getBaseSpd()){g.setColor(Color.BLUE); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+235, y+240, y+235}, 3);}
            if (character.getLuck() > character.getBaseLuck()){g.setColor(Color.RED); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+270, y+265, y+270}, 3);}
            else if (character.getLuck() < character.getBaseLuck()){g.setColor(Color.BLUE); g.fillPolygon(new int[]{50+x, 46+x, 42+x}, new int[]{y+265, y+270, y+265}, 3);}
        }

    }

    private void targetMenu(Graphics g){
        g.setFont(setFont(false, false, 12));
        //draw box
        int x = (CONST.FRAME_WIDTH/2)-210;
        int y = 220;
        g.setColor(Color.BLACK);
        g.fillRect(x, y, 420, 120);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, 420, 120);
        g.drawString("Select Target", x+175, y+13);

        //draw character portraits
        for (int i = 0; i < 4; i++){
            allCharacters[i].drawImage(g, x + (i*100) + 20, y+20);
        }

        //draw square around selected person
        g.drawRect(x + ((targetChoice-1)*100) + 17, y+17, 86, 86);
    }


    private void battlePhase(Graphics g){
        //draw characters
        int x = 4;
        for (BattleCharacter allCharacter : allCharacters) {
            //draw box
            g.setColor(Color.BLACK);
            g.fillRect(x, 455, 198, 150);
            g.setColor(Color.WHITE);
            g.drawRect(x, 455, 198, 150);

            //draw name and number
            g.setFont(setFont(true, true, 16));

            //draw character portrait
            allCharacter.drawImage(g, x+60, 470);

            //draw hp, mana
            g.setFont(setFont(false, false, 10));
            allCharacter.drawHp(g, x+55, 560, 90);
            allCharacter.drawMana(g, x+55, 580, 90);
            x += 198;
        }

        //if there are more characters waiting to act (priorityqueue sorted by speed)
        if(!turnOrder.isEmpty()){
            if(nextDialogue){
                //if next dialogue option is pressed, poll next character and act
                BattleEntity cur = turnOrder.poll();
                if(turnOrder.isEmpty()){lastDialogue = true;}
                //if the current character was knocked out before their turn started, don't act
                if(!cur.isAlive()){
                    if(cur instanceof  BattleCharacter) {
                        actionDialogue = cur.getName() + " is knocked out...";
                    }else{
                        actionDialogue = cur.getName() + " was knocked out!";
                        turnOrder.clear();
                        lastDialogue = true;
                    }
                //alive and enemy
                }else if (cur instanceof BattleEnemy){
                    actionDialogue = ((BattleEnemy) cur).act(aliveCharacters);
                //alive and ally
                }else {
                    actionDialogue = ((BattleCharacter) cur).act();
                }
                nextDialogue = false;
            }else{
                battleDialogue(g);
            }
        }

        if(lastDialogue){
            if(!nextDialogue){
                battleDialogue(g);
            }else {
                //if lost (all characters dead)
                if (aliveCharacters.isEmpty()){status = BattleStage.LOSE;}
                //else if win (enemy dead)
                else if(!enemy.isAlive()){status = BattleStage.WIN;}
                else {
                    resetTurn();
                }
            }
        }
    }

    private void resetTurn(){
        //reset choices
        actionDialogue = "";
        menuChoice = BattleAction.ATTACK;
        status = BattleStage.SELECTION;
        lastDialogue = false;
        nextDialogue = false;
        enemy.clearTargets();
        curCharacter = 0;
        for (int i = 0; i < allCharacters.length; i++) {
            allCharacters[i].resetChoices();
            //switch to next alive character
            if (curCharacter == 0 && allCharacters[i].isAlive()) {
                switchCharacter(i + 1);
            }
        }
    }

    private void battleDialogue(Graphics g){
        g.setFont(setFont(false, false, 20));
        int x = (CONST.FRAME_WIDTH/2) - 250;
        int y = 260;

        //draw text box
        g.setColor(Color.BLACK);
        g.fillRect(x, y, 500, 130);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, 500, 130);

        Scanner s = new Scanner(actionDialogue);
        String line = "";
        int lineNumber = 0;

        // split dialogue into multiple lines
        while (s.hasNext()){
            line = line + s.next() + " ";
            if (line.length() > 46 || !s.hasNext()){
                g.drawString(line, x+10, y+lineNumber + 30);
                line = "";
                lineNumber += 25;
            }
        }
    }

    private void switchCharacter(int nextCharacter){
        //switch character and reset choices
        status = BattleStage.SELECTION;
        curCharacter = nextCharacter;
        menuChoice = BattleAction.ATTACK;
        skillChoice = 0;
        targetChoice = 0;
        allCharacters[curCharacter-1].resetChoices();
    }

    private void nextCharacter(){
        //after an action is selected for a character (attack/defend/skill), checks for any unready characters to automatically switch to
        for (int i = 0; i < 4; i++){
            if (allCharacters[i].isAlive() && !allCharacters[i].isReady()){// switch if character is alive and has not selected an action
                switchCharacter(i+1);
                break;
            }
        }

        //check if all alive characters are ready
        int count = 0;
        for (BattleCharacter c : aliveCharacters) {
            if (c.isReady()) {
                count++;
            }
        }
        //enter battle phase if all ready
        if (count == aliveCharacters.size()){
            status = BattleStage.BATTLE;
            curCharacter = 0;
            turnOrder.addAll(aliveCharacters);
            turnOrder.add(enemy);
            nextDialogue = true;
        }
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch(code){
            //change selected option (selection, skill, target)
            case KeyEvent.VK_UP: {
                if(status == BattleStage.SKILL){
                    if(skillChoice!= 1){skillChoice--;}
                }
                else if(status == BattleStage.SELECTION){
                    if(menuChoice == BattleAction.STATS){menuChoice = BattleAction.ATTACK;}
                    else if(menuChoice == BattleAction.DEFEND){menuChoice = BattleAction.SKILL;}
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                if(status == BattleStage.SKILL){
                    if(skillChoice != 4){skillChoice++;}
                }
                else if(status == BattleStage.SELECTION){
                    if(menuChoice == BattleAction.ATTACK){menuChoice = BattleAction.STATS;}
                    else if(menuChoice == BattleAction.SKILL){menuChoice = BattleAction.DEFEND;}
                }
                break;
            }
            case KeyEvent.VK_RIGHT: {
                if(status == BattleStage.SELECTION){
                    if(menuChoice == BattleAction.ATTACK){menuChoice = BattleAction.SKILL;}
                    else if(menuChoice == BattleAction.STATS){menuChoice = BattleAction.DEFEND;}
                }
                else if(status == BattleStage.TARGET){
                    //find next possible target to the right that is alive
                    for(int i=targetChoice; i<4; i++){
                        //next target
                        if(allCharacters[i].isAlive()){
                            targetChoice = i+1;
                            break;
                        }
                    }
                }
                break;
            }
            case KeyEvent.VK_LEFT: {
                if(status == BattleStage.SELECTION){
                    if(menuChoice == BattleAction.SKILL){menuChoice = BattleAction.ATTACK;}
                    else if(menuChoice == BattleAction.DEFEND){menuChoice = BattleAction.STATS;}
                }
                else if(status == BattleStage.TARGET){
                    for (int i=targetChoice; i>1; i--){
                        //prev target
                        if (allCharacters[i-2].isAlive()){
                            targetChoice = i-1;
                            break;
                        }
                    }
                }
                break;
            }
            //different behaviour for interact key depending on what menu is currently open
            case KeyEvent.VK_Z: {
                if(status == BattleStage.SELECTION){
                    switch(menuChoice){
                        case ATTACK: {
                            allCharacters[curCharacter-1].setActionChoice(menuChoice);
                            allCharacters[curCharacter-1].addTarget(enemy);
                            nextCharacter();
                            break;
                        }
                        case SKILL: {
                            allCharacters[curCharacter-1].setActionChoice(menuChoice);
                            status = BattleStage.SKILL;
                            skillChoice = 1;
                            break;
                        }
                        case STATS: {
                            status = BattleStage.STATS;
                            break;
                        }
                        case DEFEND: {
                            allCharacters[curCharacter-1].setActionChoice(menuChoice);
                            nextCharacter();
                            break;
                        }
                    }
                }

                else if(status == BattleStage.SKILL){
                    BattleCharacter cur = allCharacters[curCharacter-1];

                    //check if has enough mana
                    if (cur.getMana() > cur.getSkills()[skillChoice-1].getCost()){
                        cur.setSkillChoice(skillChoice);

                        //one ally
                        if (cur.getSkills()[skillChoice-1].getTarget() == BattleTarget.ALLY_ONE){
                            status = BattleStage.TARGET;

                            // loop through all characters and set the first alive one as the initial target
                            for (int i = 0; i < 4; i++){
                                if (allCharacters[i].isAlive()){
                                    targetChoice = i + 1;
                                    break;
                                }
                            }
                        }

                        //all allies
                        else if (cur.getSkills()[skillChoice-1].getTarget() == BattleTarget.ALLY_ALL){
                            //add each alive character
                            for (BattleCharacter c : aliveCharacters){
                                cur.addTarget(c);
                            }
                            nextCharacter();
                        }

                        //self
                        else if (cur.getSkills()[skillChoice-1].getTarget() == BattleTarget.SELF){
                            cur.addTarget(allCharacters[curCharacter-1]);
                            nextCharacter();
                        }

                        //enemy and enemy and self skills
                        else{
                            cur.addTarget(enemy);
                            nextCharacter();
                        }
                    }
                }
                else if(status == BattleStage.TARGET) {
                    allCharacters[curCharacter-1].addTarget(allCharacters[targetChoice-1]);
                    nextCharacter();
                }
                else if(status == BattleStage.BATTLE) {
                    nextDialogue = true;
                }
                break;
            }
            case KeyEvent.VK_X: {
                if(status == BattleStage.SKILL || status == BattleStage.STATS) {
                    status = BattleStage.SELECTION;
                }
                break;
            }
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4: { //switch to each character and reset choice if alive and pressing 1-4
                int num = Character.getNumericValue(code);
                if(allCharacters[num-1].isAlive() && status == BattleStage.SELECTION) {
                    switchCharacter(num);
                }
                break;
            }
        }
    }
}
