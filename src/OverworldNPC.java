import java.awt.*;

public class OverworldNPC extends OverworldSprite{
    private final NPCDialogue dialogue;

    public OverworldNPC(int worldX, int worldY, String imgDir, String dialogueDir) {
        super(worldX, worldY, imgDir);
        this.dialogue = new NPCDialogue(dialogueDir);
    }

    public void startDialogue(){
        dialogue.startDialogue();
    }

    public void nextLine(){
        dialogue.nextLine();
    }

    public boolean inDialogue(){return dialogue.isInDialogue();}

    public void drawDialogue(Graphics g){
        dialogue.drawDialogue(g);
    }

}
