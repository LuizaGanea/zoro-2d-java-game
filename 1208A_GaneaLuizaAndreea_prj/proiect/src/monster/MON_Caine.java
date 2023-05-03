package monster;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;


public class MON_Caine extends Entity {
	

	//aici?
	//Nu, facem o clasa separata care va apela acest constructor
	//bine
	//Deci o poti face
	//in ce pachet?
	//Mai bine in acelasi cu MON_Caine
	public MON_Caine(GamePanel gp) {
		super(gp);
		type=2;
		
		name="Caine";
		speed=1;
		maxLife=4;
		life=maxLife;
		attack=2;
		defense=0;
		exp=2; //cat iau cand il omor
		
		
		solidArea.x=8;
		solidArea.y=16;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		solidArea.width=32;
		solidArea.height=32;
		getImage();
	}
	
	public void getImage() {
		try {
			up1=ImageIO.read(getClass().getResourceAsStream("/res/monster/fatacaine1.png"));
			up2=ImageIO.read(getClass().getResourceAsStream("/res/monster/fatacaine2.png"));
			down1=ImageIO.read(getClass().getResourceAsStream("/res/monster/fatacaine1.png"));
			down2=ImageIO.read(getClass().getResourceAsStream("/res/monster/fatacaine2.png"));
			left1=ImageIO.read(getClass().getResourceAsStream("/res/monster/stangacaine1.png"));
			left2=ImageIO.read(getClass().getResourceAsStream("/res/monster/stangacaine2.png"));
			right1=ImageIO.read(getClass().getResourceAsStream("/res/monster/dreaptacaine1.png"));
			right2=ImageIO.read(getClass().getResourceAsStream("/res/monster/dreaptacaine2.png"));
		
			
	} catch(IOException e) {
		e.printStackTrace();
	}

		
		/*
	
		up1=setup("/monster/fatacaine1");
		up2=setup("/monster/fatacaine2");
		down1=setup("/monster/fatacaine1");
		down2=setup("/monster/fatacaine2");
		left1=setup("/monster/stangacaine1");
		left2=setup("/monster/stangacaine2");
		right1=setup("/monster/dreaptacaine1");
		right2=setup("/monster/dreaptacaine2");
		
		*/
		
	}
	
	public void setAction() {
		actionLockCounter++;
		if(actionLockCounter==120) {
			Random random=new Random();
			int i=random.nextInt(100)+1;
			if(i<=25) {
				direction="up";
			}
			if(i>25&&i<=50) {
				direction="down";
			}
			if(i>50&&i<=75) {
				direction="left";
			}
			if(i>75&&i<=100) {
				direction="right";
			}
			actionLockCounter=0;
		}
	}

	

}
