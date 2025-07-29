import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class Overworld extends JPanel implements KeyListener {

    OverworldPlayer player;
    Map map;
    ArrayList<OverworldNPC> npcs;
    OverworldNPC interactingNPC;
    LinkedList<OverworldObject> objects;
    LinkedList<OverworldEnemy> enemies;
    private int freezeEnemies;
    private boolean inMenu;
    private boolean menuPopout;
    private int menuChoice;
    private int itemChoice;
    private Battle curBattle;
    BattleCharacter basil;
    BattleCharacter aubrey;
    BattleCharacter kel;
    BattleCharacter hero;
    BattleCharacter[] characters;

    private final NPCDialogue startingDialogue;
    private boolean started;

    public Overworld(){
        player = new OverworldPlayer(444, 2784, CONST.PLAYER_IMAGE_DIRECTORIES);
        map = new Map("resources/world_map.txt");
        player.addItem(CONST.ITEM_PENGUIN);
        freezeEnemies = 0;
        loadObjects();
        loadNPCs();
        loadEnemies();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        inMenu = false;
        menuPopout = false;
        menuChoice = 1;
        itemChoice = 1;

        basil = CONST.BASIL;
        aubrey = CONST.AUBREY;
        kel = CONST.KEL;
        hero = CONST.HERO;
        characters = new BattleCharacter[]{basil, aubrey, kel, hero};
        curBattle = null;

        started = false;
        startingDialogue = new NPCDialogue("resources/startingDialogue.txt");
    }

    public Map getMap(){return map;}
    public LinkedList<OverworldObject> getObjects(){return objects;}

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(curBattle == null) {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, CONST.FRAME_WIDTH, CONST.FRAME_HEIGHT);
            int playerX = player.getWorldX();
            int playerY = player.getWorldY();

            //draw map
            map.draw(g, playerX, playerY);

            //draw objects
            for (OverworldObject o : objects) {
                o.draw(g, playerX, playerY);
            }

            //draw npcs
            for (OverworldNPC n : npcs) {
                n.draw(g, playerX, playerY);
            }

            //draw enemies
            for (OverworldEnemy e : enemies) {
                e.draw(g, playerX, playerY);
            }

            //draw player and any dialogue boxes
            player.draw(g);

            if (interactingNPC != null && interactingNPC.inDialogue()) {
                interactingNPC.drawDialogue(g);
            }

            if (inMenu) {
                openMenu(g);
            }

            if(!started){startingDialogue.drawDialogue(g);}
        }else{
            curBattle.draw(g);
        }

    }

    public void update(){
        if(!started){return;} //nothing updates before dismissing the starting dialogue box
        if(curBattle == null) {
            player.update(this);
            if(freezeEnemies > 0){freezeEnemies--;} //decrement frozen enemies
            System.out.println("———————————————————————————\nworldX: " + player.getWorldX() + " , worldY: " + player.getWorldY() + "\nworldCol: " + player.getWorldX() / CONST.TILE_SIZE + ", worldRow: " + player.getWorldY() / CONST.TILE_SIZE + "\n       freeze: " + freezeEnemies);

            //check enemies
            for (OverworldEnemy e : enemies) {
                if (e.getWorldX() == Integer.MAX_VALUE) {
                    enemies.remove(e);
                    break;
                }
                //if enemies arent frozen, update and move
                if(freezeEnemies == 0){e.update(this, player);}
                //if encounter enemy, start battle
                if (player.collide(e.getHitbox())) {
                    e.setWorldX(Integer.MAX_VALUE);
                    curBattle = new Battle(characters, e.getEnemy());
                }
            }

            if (interactingNPC != null && !interactingNPC.inDialogue()) {
                interactingNPC = null;
                player.stopDialogue();
            }

            if (player.isInteracting()) {
                //check npcs
                for (OverworldNPC n : npcs) {
                    //if currently in dialogue
                    if (n.inDialogue()) {
                        n.nextLine();
                        player.stopInteracting();
                        //no current dialogue playing
                    } else if (player.collide(n.getHitbox())) {
                        n.startDialogue();
                        player.startDialogue();
                        this.interactingNPC = n;
                        player.stopInteracting();
                        player.stopMove("x");
                        player.stopMove("y");
                    }
                }

                //check objects
                for (OverworldObject o : objects) {
                    if (player.collide(o.getInteractBox())) {
                        OverworldObject removed = o.interact(player);
                        if (removed != null) {
                            objects.remove(removed);
                            player.removeEquippedItem();
                            break;
                        }
                    }
                }
            }
        }else{
            curBattle.update();
            if(curBattle.isBattleComplete()){
                curBattle = null;
                freezeEnemies = 100;
            }
        }
    }

    private void openMenu(Graphics g){
        // paint a translucent square to dim the background while in the menu
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0,0,CONST.FRAME_WIDTH,CONST.FRAME_HEIGHT);

        // paint main menu options (characters, key bindings, exit)
        g.setColor(Color.BLACK);
        g.fillRect(45,60,250,400);
        g.setColor(Color.WHITE);
        g.drawRect(45,60,250,400);
        g.fillPolygon(new int[]{70, 65, 65}, new int[]{68+(menuChoice*35), 63+(menuChoice*35), 73+(menuChoice*35)}, 3);
        Font font = new Font("Georgia", Font.BOLD, 20);
        g.setFont(font);
        g.drawString("CHARACTERS", 90, 110);
        g.drawString("INVENTORY", 90, 145);
        g.drawString("KEY BINDINGS", 90, 180);
        g.drawString("EXIT GAME", 90, 215);
        if(menuPopout){
            switch (menuChoice){
                case 1: { //characters
                    g.setColor(Color.BLACK);
                    g.fillRect(400,60,265,420);
                    g.setColor(Color.WHITE);

                    for (int i = 0; i < characters.length; i++){
                        BattleCharacter character = characters[i];
                        int x = 460;
                        int y = 100 + i*100;

                        // draw images
                        character.drawImage(g, 420, 80+i*100);

                        // draw names
                        font = new Font("Georgia", Font.BOLD, 20);
                        g.setFont(font);
                        g.drawString(character.getName().toUpperCase(), 519, y);

                        // draw stats
                        font = new Font("Serif", Font.PLAIN, 16);
                        g.setFont(font);
                        g.drawString("hp: "+ character.getMaxHp(), x+60, y+20);
                        g.drawString("mana: "+ character.getMaxMana(), x+60, y+35);
                        g.drawString("atk: "+ character.getBaseAtk(), x+60, y+50);
                        g.drawString("def: "+ character.getBaseDef(), x+130, y+20);
                        g.drawString("spd: "+ character.getBaseSpd(), x+130, y+35);
                        g.drawString("luck: "+ character.getBaseLuck(), x+130, y+50);
                    }
                    break;
                }
                case 2: { //inventory
                    font = new Font("Serif", Font.BOLD, 20);
                    g.setFont(font);
                    g.setColor(Color.BLACK);
                    g.fillRect(400,60,250,400);
                    g.setColor(Color.WHITE);
                    Item equipped = player.getEquippedItem();
                    LinkedList<Item> itemInventory = player.getItemInventory();

                    //equipped
                    g.drawString("Eqp. -", 420, 95);
                    if(equipped != null) {g.drawString(equipped.getName(), 480, 95);}
                    else{g.drawString("<EMPTY>", 480, 95);}
                    g.drawString("——————————", 425, 125);

                    //inventory
                    for(int i=1; i<=9; i++) {
                        int y = 155 + (35 * (i - 1));
                        g.drawString(Integer.toString(i) + " -", 450, y);
                        if (i <= itemInventory.size()) {
                            g.drawString(itemInventory.get(i - 1).getName(), 480, y);
                        } else {
                            g.drawString("<EMPTY>", 480, y);
                        }
                    }

                    //selection triangle
                    if(!itemInventory.isEmpty()){g.fillPolygon(new int[]{425, 420, 420}, new int[]{113+(itemChoice*35), 108+(itemChoice*35), 118+(itemChoice*35)}, 3);};
                    break;
                }
                case 3: { //key binds
                    font = new Font("Serif", Font.PLAIN, 20);
                    g.setFont(font);
                    g.setColor(Color.BLACK);
                    g.fillRect(400,60,225, 150);
                    g.setColor(Color.WHITE);
                    g.drawString("Z   -   confirm", 445, 105);
                    g.drawString("X   -   cancel", 445, 140);
                    g.drawString("C   -   menu", 445, 175);
                    break;
                }
                case 4: {
                    System.exit(0);
                }
            }
        }
    }

    private void loadNPCs(){
        npcs = new ArrayList<>();
        npcs.add(new OverworldNPC(510, 2784, "resources/penguin_npc.png", "resources/npc_dialogue_1.txt"));
        npcs.add(new OverworldNPC(CONST.TILE_SIZE * 25, CONST.TILE_SIZE * 86, "resources/penguin_npc.png", "resources/npc_dialogue_2.txt"));
        npcs.add(new OverworldNPC(CONST.TILE_SIZE * 33, CONST.TILE_SIZE * 86, "resources/penguin_npc.png", "resources/npc_dialogue_3.txt"));
        npcs.add(new OverworldNPC(CONST.TILE_SIZE * 41, CONST.TILE_SIZE * 86, "resources/penguin_npc.png", "resources/npc_dialogue_4.txt"));
        npcs.add(new OverworldNPC(CONST.TILE_SIZE * 62, CONST.TILE_SIZE * 89, "resources/penguin_npc.png", "resources/npc_dialogue_5.txt"));
    }

    private void loadObjects(){
        objects = new LinkedList<>();
        objects.add(new OverworldObject(CONST.TILE_SIZE * 27, CONST.TILE_SIZE * 87, "resources/bush_door.png", "door", false, null));
        objects.add(new OverworldObject(CONST.TILE_SIZE * 35, CONST.TILE_SIZE * 87, "resources/bush_door.png", "door", false, null));
        objects.add(new OverworldObject(CONST.TILE_SIZE * 34, CONST.TILE_SIZE * 89, "resources/key.png", "key", true, CONST.ITEM_KEY));
        objects.add(new OverworldObject(CONST.TILE_SIZE * 26, CONST.TILE_SIZE * 89, new String[]{"resources/button_unpressed.png", "resources/button_pressed.png"}, "button", false, null));
        objects.get(3).setLinkedTarget(objects.get(0));
        objects.add(new OverworldObject(CONST.TILE_SIZE * 43, CONST.TILE_SIZE * 87, "resources/bush_door.png", "door", false, null));
        objects.add(new OverworldObject(CONST.TILE_SIZE * 42, CONST.TILE_SIZE * 89, new String[]{"resources/button_unpressed.png", "resources/button_pressed.png"}, "button", false, null));
        objects.get(5).setLinkedTarget(objects.get(4));
        objects.add(new OverworldObject(CONST.TILE_SIZE * 59, CONST.TILE_SIZE * 81, "resources/key.png", "key", true, CONST.ITEM_KEY));
        objects.add(new OverworldObject(CONST.TILE_SIZE * 60, CONST.TILE_SIZE * 89, "resources/bush_door.png", "door", false, null));

    }

    private void loadEnemies(){
        enemies = new LinkedList<>();
        enemies.add(new OverworldEnemy(CONST.TILE_SIZE * 56, CONST.TILE_SIZE * 82, CONST.ENEMY_SPIDER_IMAGE_DIRECTORIES, CONST.ENEMY_SPIDER));
        enemies.add(new OverworldEnemy(CONST.TILE_SIZE * 56, CONST.TILE_SIZE * 88, CONST.ENEMY_SPIDER_IMAGE_DIRECTORIES, CONST.ENEMY_SPIDER));
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(curBattle != null){curBattle.keyPressed(e); return;}
        if(!inMenu) {
            switch (code) {
                case KeyEvent.VK_UP: {
                    player.move(OverworldDirection.UP);
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    player.move(OverworldDirection.LEFT);
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    player.move(OverworldDirection.DOWN);
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    player.move(OverworldDirection.RIGHT);
                    break;
                }
                case KeyEvent.VK_Z: {
                    if(started){player.interact();}
                    else{started = true;}
                    break;
                }
                case KeyEvent.VK_C: {
                    if((interactingNPC == null || !interactingNPC.inDialogue()) && started){
                        inMenu = true;
                        player.stopMove("x");
                        player.stopMove("y");
                    }
                    break;
                }
            }
        }else{
            switch (code) {
                case KeyEvent.VK_UP: {
                    if(menuChoice != 1 && !menuPopout){menuChoice--;}
                    if(menuChoice == 2 && menuPopout && itemChoice != 1){itemChoice--;}
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    if(menuChoice != 4 && !menuPopout){menuChoice++;}
                    if(menuChoice == 2 && menuPopout && itemChoice != player.getItemInventory().size() && !player.getItemInventory().isEmpty()){itemChoice++;}
                    break;
                }
                case KeyEvent.VK_Z: {
                    if(menuChoice != 2) {menuPopout = !menuPopout;}
                    else if(!menuPopout){menuPopout = true;}
                    else{if(!player.getItemInventory().isEmpty()){player.setEquippedItem(itemChoice-1);}}
                    break;
                }
                case KeyEvent.VK_X:
                case KeyEvent.VK_C: {
                    if(!menuPopout) {
                        inMenu = false;
                        menuChoice = 1;
                        itemChoice = 1;
                    }
                    menuPopout = false;
                    break;
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch(code){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN: {
                player.stopMove("y");
                break;
            }
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT: {
                player.stopMove("x");
                break;
            }
        }
    }

    public void keyTyped(KeyEvent e) {

    }

}
