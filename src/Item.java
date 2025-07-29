import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Item {
    private String name;
    private String description;
    private BufferedImage image;

    public Item(String name, String description, String imgDir){
        this.name = name;
        this.description = description;
        try{
            image = ImageIO.read(new File(imgDir));
        }catch (IOException e) {System.out.println("Item " + name + " image not found!");}
    }

    public String getName(){return name;}
    public String getDescription(){return description;}
    public BufferedImage getImage(){return image;}

}
