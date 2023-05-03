package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool { //aici punem functii pe care o sa le apelam
		
		public BufferedImage scaleImage(BufferedImage original, int width, int height) {
		BufferedImage scaledImage=new BufferedImage(width, height, original.getType()); //aici era original.image.getType() dar am gresit
		Graphics2D g2=scaledImage.createGraphics();
		g2.drawImage(original,0,0,width,height,null);
		g2.dispose();
		
		return scaledImage;
		
	}

}
