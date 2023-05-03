package entity;

import main.GamePanel;

public class Projectile extends Entity{
	Entity user;

	public Projectile(GamePanel gp) {
		super(gp);
		
	}
	
	public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
		//ca sa impuscam trb sa ii zicem toti parametrii astia
		this.worldX=worldX;
		this.worldY=worldY;
		this.direction=direction;
		this.alive=alive;
		this.user=user;
		this.life=this.maxLife;
		
	}
		
	public void update() {
		if(user==gp.player) { //cn arunca proiectila
			int monsterIndex=gp.cChecker.checkEntity(this, gp.monster);
			if(monsterIndex!=999) {
				gp.player.damageMonster(monsterIndex,attack);
				alive=false; //daca proiectila atinge monstrul, proiectila dispare
			}
		}
		if(user!=gp.player){
			
		}
		
		switch(direction) {
		case "up": worldY-=speed; break;
		case "down": worldY+=speed; break;
		case "left": worldX-=speed; break;
		case "right": worldX+=speed; break;
		}
		life--;
		if(life<=0) { //dupa ce impuscam, incet incet isi pierde viata, adica dispare(dupa 80 frames)
			alive=false;
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
			
		}
	}


