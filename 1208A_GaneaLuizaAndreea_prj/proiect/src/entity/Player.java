package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Projectile;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;


public class Player extends Entity{
	
	KeyHandler keyH;
	public final int screenX;
	public final int screenY;
	int standCounter=0; //folosim pt revenirea eroului in pozitie standard cand se opreste din mers
	static Player instance=null;
	
	static public Player getInstance(GamePanel gp, KeyHandler keyH) {
		if(instance==null)
			instance=new Player(gp,keyH);
		return instance;
	}

	private Player(GamePanel gp, KeyHandler keyH) {
		super(gp); //apelam constructorul superclasei (entity)
		
		this.keyH=keyH;
		
		screenX=gp.screenWidth/2-(gp.tileSize/2);
		screenY=gp.screenHeight/2-(gp.tileSize/2);
		
		
		solidArea=new Rectangle(8,16,32,32); //x,y,width,height doar o mica parte din caracter va fi solida, ca sa nu treaca prin ob solide dar sa para ca se apropie
		solidArea.x=8;
		solidArea.y=16;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		solidArea.width=32;
		solidArea.height=32;
		
		//set player's attack area
		attackArea.width=20;
		attackArea.height=20;
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
	}
	
	public void setDefaultPositions() { //restoring the game after game over
		
		worldX=gp.tileSize*22;; //nu este pozitia ecranului ci pozitia playerului in worldmap
		worldY=gp.tileSize*20;
		direction="down";
	}
	public void restoreLifeAndMana() {
		life=maxLife;
		mana=maxMana;
		invincible=false;
		
	}
	
	public void setDefaultValues() { //pozitie default a eroului
		worldX=gp.tileSize*22;; //nu este pozitia ecranului ci pozitia playerului in worldmap
		worldY=gp.tileSize*20;
		speed=4;
		direction="down";
		
	
		
		//player status life
		level=1;
		maxLife=6;   //2vieti=1 inima
		life=maxLife; //by default
		strength=1;
		dexterity=1;
		exp=0;
		nextLevelExp=4; //cata experienta itittrb ca sa level up
		//coin=0;
		currentWeapon=new OBJ_Sword_Normal(gp);
		currentShield=new OBJ_Shield_Wood(gp);
		projectile=new OBJ_Projectile(gp);
		attack=getAttack();
		defense=getDefense();
		
	}
	public int getAttack() {
		return attack=strength*currentWeapon.attackValue;
	}
	public int getDefense() {
		return defense=dexterity*currentShield.defenseValue;
	}
	public void getPlayerImage() {
	
		try {
			up1=ImageIO.read(getClass().getResourceAsStream("/res/player/fata1.png"));
			up2=ImageIO.read(getClass().getResourceAsStream("/res/player/fata2.png"));
			down1=ImageIO.read(getClass().getResourceAsStream("/res/player/fata1.png"));
			down2=ImageIO.read(getClass().getResourceAsStream("/res/player/fata2.png"));
			left1=ImageIO.read(getClass().getResourceAsStream("/res/player/stanga1.png"));
			left2=ImageIO.read(getClass().getResourceAsStream("/res/player/stanga2.png"));
			right1=ImageIO.read(getClass().getResourceAsStream("/res/player/dreapta1.png"));
			right2=ImageIO.read(getClass().getResourceAsStream("/res/player/dreapta2.png"));
		
			
	} catch(IOException e) {
		e.printStackTrace();
	}
}

	public void getPlayerAttackImage()
	{
		try {
		attackUp1=ImageIO.read(getClass().getResourceAsStream("/res/player/atacsus1.png"));
		attackUp2=ImageIO.read(getClass().getResourceAsStream("/res/player/atacsus2.png"));
		attackDown1=ImageIO.read(getClass().getResourceAsStream("/res/player/atacjos1.png"));
		attackDown2=ImageIO.read(getClass().getResourceAsStream("/res/player/atacjos2.png"));
		attackLeft1=ImageIO.read(getClass().getResourceAsStream("/res/player/atacstanga1.png"));
		attackLeft2=ImageIO.read(getClass().getResourceAsStream("/res/player/atacstanga2.png"));
		attackRight1=ImageIO.read(getClass().getResourceAsStream("/res/player/atacdreapta1.png"));
		attackRight2=ImageIO.read(getClass().getResourceAsStream("/res/player/atacdreapta2.png"));
		
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() { //miscari sus jos stanga dreapta cand apas tastele
		
		
		if(attacking==true) {
			attacking();
		}
		else if(keyH.upPressed==true || keyH.downPressed==true || keyH.leftPressed==true || keyH.rightPressed==true) {
		if(keyH.upPressed==true||keyH.enterPressed==true) {
			direction="up";
		}
		else if(keyH.downPressed==true) {
			direction="down";
		}
		else if(keyH.leftPressed==true) {
			direction="left";
		}
		else if(keyH.rightPressed==true) {
			direction="right";
		}
		
		//vf coliziunea cu tiles urile
		collisionOn=false;
		gp.cChecker.checkTile(this);
		
		//vf coliziunea cu obiecteke
		//int objIndex =gp.cChecker.checkObject(this,true);
		
		//check monster collision
		int monsterIndex=gp.cChecker.checkEntity(this, gp.monster);
		contactMonster(monsterIndex);
		
		//if collision false, nu se misca eroul
		if(collisionOn==false&&gp.keyH.enterPressed==false) {
			switch(direction) {
			case "up": worldY-=speed; break;
			case "down": worldY+=speed; break;
			case "left": worldX-=speed; break;
			case "right": worldX+=speed; break;
				
			}
		}
		gp.keyH.enterPressed=false;
		
		spriteCounter++;
		if(spriteCounter>12) {
			if(spriteNum==1) {
				spriteNum=2;
			}
			else if(spriteNum==2) {
				spriteNum=1;
			}
			spriteCounter=0;
		}
		}
		else {
			standCounter++;
			if(standCounter==15) {
				spriteNum=1; //cand ne oprim din mers, eroul revine in pozitia standard
				standCounter=0;	
			}	
		}
		
		if(gp.keyH.shotKeyPressed==true&&projectile.alive==false&&shotAvailableCounter==30) {//nu putem arunca proiectila decat daca cea precedenta e inca vie(valabila)
			projectile.set(worldX,worldY,direction,true,this);
			
			//adaugam priiectila in array lsit
			gp.projectileList.add(projectile);
			shotAvailableCounter=0;
			gp.playSE(2);
			
			
		}
		
		
		if(invincible==true) {
			invincibleCounter++;
			if(invincibleCounter>60) {
				invincible=false;
				invincibleCounter=0;  //reset the counter
			}
		}
		if(shotAvailableCounter<30) {
			shotAvailableCounter++;
		}
		
		if(life<=0) {
			gp.gameState=gp.gameOverState;
			gp.playSE(3);
		}
		
	}
	
	public void attacking() {
		spriteCounter++;
		if(spriteCounter<=5) {
			spriteNum=1;
			
		}
		if(spriteCounter>5&&spriteCounter<=25) {
			spriteNum=2;
			
			//save the current wirldx, worldy, solidarea
			int currentWorldX=worldX;
			int currentWorldY=worldY;
			int solidAreaWidth=solidArea.width;
			int solidAreaHeight=solidArea.height;
			
			//adjusting for the attackARea
			switch(direction) {
			case "up": worldY-=attackArea.height; break;
			case "down": worldY+=attackArea.height; break;
			case "left": worldX-=attackArea.width; break;
			case "right": worldX+=attackArea.width; break;
			}
			
			//attack area devine solid area
			solidArea.width=attackArea.width;
			solidArea.height=attackArea.height;
			
			//check monster collisiion
			int monsterIndex=gp.cChecker.checkEntity(this,  gp.monster);
			damageMonster(monsterIndex,attack);
			
			//dupa ce am verificat coliziunile, restore the original data
			worldX=currentWorldX;
			worldY=currentWorldY;
			solidArea.width=solidAreaWidth;
			solidArea.height=solidAreaHeight;
			
			
		}
		if(spriteCounter>25) {
			spriteNum=1;
			spriteCounter=0;
			attacking=false;
		}
	}
	 
	public void contactMonster(int i) {
		if(i!=999) {
			if(invincible==false&&gp.monster[i].dying==false) {
				int damage=gp.monster[i].attack-defense;
				if(damage<0) {
					damage=0;
				}
				life-=damage; //daca ating un monstru imi scade viata, primesc damage
				invincible=true;
			}
			
		}
	}
	
	public void damageMonster(int i,int attack) {
		if(i!=999) {
			
			if(gp.monster[i].invincible==false) { 
				int damage=attack-gp.monster[i].defense;
				if(damage<0) {
					damage=0; 
				}
				
				gp.monster[i].life-=damage;
				gp.text_ecran.addMessage(damage+" damage!");
				
				gp.monster[i].invincible=true;
				if(gp.monster[i].life<=0) {
					gp.monster[i].dying=true;
					gp.text_ecran.addMessage("Ai scapat de un "+gp.monster[i].name+"!");
					gp.text_ecran.addMessage("Exp+ "+gp.monster[i].exp);
					exp+=gp.monster[i].exp;
					checkLevelUp();
					
				}
			}
		
		}
	}
	
	public void checkLevelUp() {
		if(exp>=nextLevelExp) {
			level++;
			nextLevelExp=nextLevelExp*2;
			maxLife+=2; 
			strength++;
			dexterity++;
			attack=getAttack();
			defense=getDefense();
			
			gp.playSE(1);
			gp.text_ecran.addMessage("You are level "+level+" now");
			
			 
			}
	}
	
	public void draw(Graphics2D g2) {
	//	g2.setColor(Color.white);
		//g2.fillRect(x,y,gp.tileSize ,gp.tileSize );
		BufferedImage image=null;
		int tempScreenX=screenX;
		int tempScreenY=screenY;
		
		switch(direction) {
		case "up":
			if(attacking==false) { //daca nu is in modul atac, arat sprite urile obisnuite
				if(spriteNum==1) {image=up1;}
				if(spriteNum==2) {image=up2;}
			}
			if(attacking==true) {
				tempScreenY=screenY-gp.tileSize;
				if(spriteNum==1) {image=attackUp1;}
				if(spriteNum==2) {image=attackUp2;}
			}
			
			break;
		case "down":
			if(attacking==false) {
				if(spriteNum==1) {image=down1;}
				if(spriteNum==2) {image=down2;}
			}
			if(attacking==true) {
				if(spriteNum==1) {image=attackDown1;}
				if(spriteNum==2) {image=attackDown2;}
			}
			
			break;
		case "left":
			if(attacking==false) {
				if(spriteNum==1) {image=left1;}
				if(spriteNum==2) {image=left2;}
			}
			if(attacking==true) {
				tempScreenX=screenX-gp.tileSize;
				if(spriteNum==1) {image=attackLeft1;}
				if(spriteNum==2) {image=attackLeft2;}
			}
			
			break;
		case "right":
			if(attacking==false) {
				if(spriteNum==1) {image=right1;}
				if(spriteNum==2) {image=right2;}
			}
			if(attacking==true) {
				if(spriteNum==1) {image=attackRight1;}
				if(spriteNum==2) {image=attackRight2;}
			}
			break;
			
			}
		
	/*	//stoping the camera at the edge
		int x=screenX;
		int y=screenY;
		if(screenX>worldX){
			x=worldX;
		}
		if(screenY>worldY) {
			y=worldY;
		}
		int rightOffset=gp.screenWidth-screenX;
		if(rightOffset>gp.worldWidth-worldX) {
			x=gp.screenWidth-gp.worldWidth-worldX;
		}
		int bottomOffset=gp.screenHeight-screenY;
		if(bottomOffset>gp.worldHeight-worldY) {
			y=gp.screenHeight-gp.worldHeight-worldY;
		}
		
		*/
		
		//cream niste conditii(pt partea cu coliziunile cu monstrii)
		if(invincible==true) {
			//cand caracterul este invincibil il facem pe jumatate transparent
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f)); //umbla la opacitate
		}
		g2.drawImage(image, tempScreenX, tempScreenY  ,gp.tileSize, gp.tileSize,null);
		
		//reset opacitate
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f)); 
		
		
	}
}
