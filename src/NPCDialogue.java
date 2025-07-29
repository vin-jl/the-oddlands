import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NPCDialogue {
    private final ArrayList<String> dialogue;
    private int curIndex;
    private boolean inDialogue;

    public NPCDialogue(String dialogueDir){
        this.dialogue = new ArrayList<>();
        try{
            Scanner fr = new Scanner(new File(dialogueDir));
            while(fr.hasNextLine()){
                dialogue.add(fr.nextLine());
            }
        } catch (FileNotFoundException e) {System.out.println("File not found!");}
        this.inDialogue = false;
    }

    public void startDialogue(){this.inDialogue = true;}
    public boolean isInDialogue(){return this.inDialogue;}
    public void nextLine(){this.curIndex++;}


    public void drawDialogue(Graphics g){
        int textX = CONST.DIALOGUE_BOX_X + 16;
        int textY = CONST.DIALOGUE_BOX_Y + 42;
        //if the current index is within bounds
        if(curIndex <= dialogue.size()-1){
            drawBox(g);
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 32F));
            g.setColor(Color.WHITE);
            //split lines using regex "_nl_" (calculating number of characters in real time is cumbersome, will format ahead of time)
            for(String line : dialogue.get(curIndex).split("_nl_")){
                g.drawString(line,textX, textY);
                textY += 40;
            }
        }else{
            //stop dialogue
            curIndex = 0;
            inDialogue = false;
        }
    }

    private void drawBox(Graphics g){
        //drawing ring box
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(5));
        //white border
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(CONST.DIALOGUE_BOX_X, CONST.DIALOGUE_BOX_Y, CONST.DIALOGUE_BOX_WIDTH, CONST.DIALOGUE_BOX_HEIGHT, 20, 20);
        //translucent black background
        g2.setColor(new Color(0, 0, 0, 128));
        //slightly smaller than the box to fill
        g2.fillRoundRect(CONST.DIALOGUE_BOX_X +2, CONST.DIALOGUE_BOX_Y +2, CONST.DIALOGUE_BOX_WIDTH -4, CONST.DIALOGUE_BOX_HEIGHT -4, 20, 20);
        g2.setStroke(oldStroke);
    }

}
