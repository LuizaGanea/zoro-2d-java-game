
package entity;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	GamePanel gp;
	
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, 
	attackLeft2, attackRight1, attackRight2; 
	public int worldX, worldY;
	public int speed;
	public String direction="down";
	public int spriteCounter=0;
	public int spriteNum=1; //sprite number
	public Rectangle solidArea=new Rectangle(0,0,48,48); //the default solid area for all entities (zona de coliziune)
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn=false;
	public int actionLockCounter=0;
	public boolean invincible=false;
	public int invincibleCounter=0; //Stai sa vad mai jos
	public int type; //0=player, 1=monster, 2=..
	boolean attacking=false;
	public Rectangle attackArea=new Rectangle(0,0,0,0); //attack area a playerului
	public boolean alive=true;
	public boolean dying=false;
	int dyingCounter=0;
	
	//preluate din superobject
	public BufferedImage image, image2, image3;
	public String name;
	public boolean collision=false;
	
	//character status: VIATA inimi
	public int maxLife;
	public int life;
	public int maxMana;
	public int mana;
	
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	//public int coin; //cati bani are dar nu immi trb
	public Entity currentWeapon;
	public Entity currentShield;
	public Projectile projectile;
	public int useCost; //costul sa aruncam proiectile
	public int shotAvailableCounter=0; //sa nu aruncam 2 proiectile doedata
	
	//item attributes
	public int attackValue;
	public int defenseValue;
	
	public Entity(GamePanel gp) {
		this.gp=gp;
	}
	
		 public void setAction() { } 
		 
		 public void update(){
		 	setAction();
		 	collisionOn=false;
		 	gp.cChecker.checkTile(this);
		 	gp.cChecker.checkObject(this,false);
		 	gp.cChecker.checkEntity(this,gp.monster);
		 	boolean contactPlayer=gp.cChecker.checkPlayer(this);
		 	
		 	if(this.type==2&&contactPlayer==true) { //daca este monstru si contactul este true
		 		if(gp.player.invincible==false) {
		 			//putem primi damage
		 			int damage=attack-gp.player.defense; //cand monstru contacteaza playerul
					if(damage<0) {
						damage=0;
					}
					gp.player.life-=damage;
		 			gp.player.invincible=true; //apoi devenim iar invincibili
		 			
		 		}
		 	}
			if(collisionOn==false) { 
				switch(direction) {
				case "up": worldY-=speed; break;
				case "down": worldY+=speed; break;
				case "left": worldX-=speed; break;
				case "right": worldX+=speed; break; 		
				}
			}
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
			
			if(invincible==true) {
				invincibleCounter++;
				if(invincibleCounter>1000000000) {
					System.out.println(invincibleCounter);
					invincible=false;
					invincibleCounter=0;  //reset the counter
				}
			}
		 }
	 
		public void draw(Graphics2D g2) {
		BufferedImage image=null;
		int screenX=worldX-gp.player.worldX+gp.player.screenX;
		int screenY=worldY-gp.player.worldY+gp.player.screenY;
		
		if(worldX+gp.tileSize>gp.player.worldX-gp.player.screenX && 
				worldX-gp.tileSize<gp.player.worldX+gp.player.screenX&& 
				worldY+gp.tileSize>gp.player.worldY-gp.player.screenY&& 
				worldY-gp.tileSize<gp.player.worldY+gp.player.screenY) {
			
			switch(direction) {
			case "up":
				if(spriteNum==1) {image=up1;}
				if(spriteNum==2) {image=up2;}
				break;
			case "down":
				if(spriteNum==1) {image=down1;}
				if(spriteNum==2) {image=down2;}
				break;
			case "left":
				if(spriteNum==1) {image=left1;}
				if(spriteNum==2) {image=left2;}
				break;
			case "right":
				if(spriteNum==1) {image=right1;}
				if(spriteNum==2) {image=right2;}
				break;
				}

			//display health bar for monsters
			if(type==2) {
				double oneScale=(double)gp.tileSize/maxLife;
				double hpBarValue=oneScale*life;
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1,  screenY-16, gp.tileSize+2,  12);
				g2.setColor(new Color(255,0,30));
				g2.fillRect(screenX, screenY-15, (int)hpBarValue, 10);
				
			}
			if(invincible==true) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
			}
			if(dying==true) {
				dyingAnimation(g2);
			}
			g2.drawImage(image, screenX, screenY,gp.tileSize, gp.tileSize, null); //luata din tilemanager
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		}
		
	}
		public void dyingAnimation(Graphics2D g2) { //cred ca asta dar nu stiu daca e daor pt monstri
			dyingCounter++;
			int i=5;
			
			if(dyingCounter<=i) {changeAlpha(g2,0f);}
			if(dyingCounter>i&&dyingCounter<=i*2) {changeAlpha(g2,1f);}
			if(dyingCounter>i*2&&dyingCounter<=i*3) {changeAlpha(g2,0f);}
			if(dyingCounter>i*3&&dyingCounter<=i*4) {changeAlpha(g2,1f);}
			if(dyingCounter>i*4&&dyingCounter<=i*5) {changeAlpha(g2,0f);}
			if(dyingCounter>i*5&&dyingCounter<=i*6) {changeAlpha(g2,1f);}
			if(dyingCounter>i*6&&dyingCounter<=i*7) {changeAlpha(g2,0f);}
			if(dyingCounter>i*7&&dyingCounter<=i*8) {changeAlpha(g2,1f);}
			if(dyingCounter>i*8) {
				alive=false;
			}
			
		}
		
		public void changeAlpha(Graphics2D g2,float alphaValue) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaValue));
		}
		
		public BufferedImage setup(String imagePath,int width, int height) {
			UtilityTool uTool=new UtilityTool();
			BufferedImage image=null;
			try {
				image=ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
				image=uTool.scaleImage(image, width, height);
			}catch(IOException e) {
				e.printStackTrace();
			}
			return image;
		}
}

