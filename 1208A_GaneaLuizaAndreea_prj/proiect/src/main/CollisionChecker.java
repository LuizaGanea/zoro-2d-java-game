package main;

import entity.Entity;

public class CollisionChecker {
	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) { //constructot
		this.gp=gp;
	}
	
	public void checkTile(Entity entity) { //verificam coliziunile eroului, inamicilor
		int entityLeftWorldX=entity.worldX+entity.solidArea.x;
		int entityRightWorldX=entity.worldX+entity.solidArea.x+entity.solidArea.width;
		int entityTopWorldY=entity.worldY+entity.solidArea.y;
		int entityBottomWorldY=entity.worldY+entity.solidArea.y+entity.solidArea.height;
		
		int entityLeftCol=entityLeftWorldX/gp.tileSize;
		int entityRightCol=entityRightWorldX/gp.tileSize;
		int entityTopRow=entityTopWorldY/gp.tileSize;
		int entityBottomRow=entityBottomWorldY/gp.tileSize;
		int tileNum1, tileNum2; //stanga si dreapta eroului
		
		switch(entity.direction) {
		case "up":
			entityTopRow=(entityTopWorldY-entity.speed)/gp.tileSize; 
			tileNum1=gp.tileM.mapTileNum[entityLeftCol%50][entityTopRow%50];		
			tileNum2=gp.tileM.mapTileNum[entityRightCol%50][entityTopRow%50];
			if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true) {
				entity.collisionOn=true;
				
			}
			break;
		case "down":
			entityBottomRow=(entityBottomWorldY+entity.speed)/gp.tileSize; 
			tileNum1=gp.tileM.mapTileNum[entityLeftCol%50][entityBottomRow%50];		
			tileNum2=gp.tileM.mapTileNum[entityRightCol%50][entityBottomRow%50];
			if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true) {
				entity.collisionOn=true;
				
			}
			
			break;
		case "left":
			entityLeftCol=(entityLeftWorldX-entity.speed)/gp.tileSize; 
			tileNum1=gp.tileM.mapTileNum[entityLeftCol%50][entityTopRow%50];		
			tileNum2=gp.tileM.mapTileNum[entityLeftCol%50][entityBottomRow%50];
			if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true) {
				entity.collisionOn=true;
				
			} 
			break;
		case "right":
			entityRightCol=(entityRightWorldX+entity.speed)/gp.tileSize; 
			tileNum1=gp.tileM.mapTileNum[entityRightCol%50][entityTopRow%50];		
			tileNum2=gp.tileM.mapTileNum[entityRightCol%50][entityBottomRow%50];
			if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true) {
				entity.collisionOn=true;
				
			}
			break;
		
		}	
	}
	
	public int checkObject(Entity entity, boolean player) {
		//intai vf daca aceasta entitate este player sau nu
		
		int index=999;
		//Hai sa rezolvam cu singleton mai intai, da la clasa Main
		for(int i=0;i<gp.obj.length;i++) //aici am un array la obiecte poate merge
		{
			if(gp.obj[i]!=null) {
				//get entity's solid area position
				entity.solidArea.x=entity.worldX+entity.solidArea.x;
				entity.solidArea.y=entity.worldY+entity.solidArea.y;
				
				//get the object's soid area position
				gp.obj[i].solidArea.x=gp.obj[i].worldX+gp.obj[i].solidArea.x;
				gp.obj[i].solidArea.y=gp.obj[i].worldY+gp.obj[i].solidArea.y;
				
				switch(entity.direction) {
				case "up": entity.solidArea.y-=entity.speed;break;
					
				case "down":entity.solidArea.y+=entity.speed;break;
					
				case "left":entity.solidArea.x-=entity.speed;break;
					
				case "right":
					entity.solidArea.x-=entity.speed;System.out.println("right collision!");break;
				
				}
				if(entity.solidArea.intersects(gp.obj[i].solidArea)) {
					System.out.println("right collision!");
				
				if(gp.obj[i].collision==true) {
					entity.collisionOn=true;
				}
				if(player==true) {//daca este player entitatea
					index=i;
				}
			}

			}
			//reseting the values
			entity.solidArea.x=entity.solidAreaDefaultX;
			entity.solidArea.y=entity.solidAreaDefaultY;
			if(gp.obj[i]!=null) {
				gp.obj[i].solidArea.x=gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y=gp.obj[i].solidAreaDefaultY;
			}
			else {
				gp.obj[i]=new Entity(gp);
				gp.obj[i].solidArea.x=gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y=gp.obj[i].solidAreaDefaultY;
			}
			
		}
		
		return index;
	}
		
	public int checkEntity(Entity entity, Entity[] target) {
		
		int index=999;
		for(int i=0;i<target.length;i++)
		{
			if(target[i]!=null) {
				//get entity's solid area position
				entity.solidArea.x=entity.worldX+entity.solidArea.x;
				entity.solidArea.y=entity.worldY+entity.solidArea.y;
				
				//get the object's soid area position
				target[i].solidArea.x=target[i].worldX+target[i].solidArea.x;
				target[i].solidArea.y=target[i].worldY+target[i].solidArea.y;
				
				switch(entity.direction) {
				case "up":	entity.solidArea.y-=entity.speed; break;
				case "down": entity.solidArea.y+=entity.speed; break;
				case "left": entity.solidArea.x-=entity.speed;	break;
				case "right": entity.solidArea.x-=entity.speed; break;
				
				}
				if(entity.solidArea.intersects(target[i].solidArea)) {
					if(target[i]!=entity) {
						entity.collisionOn=true;
						index=i;
					}
					
			}
			//reseting the values
			entity.solidArea.x=entity.solidAreaDefaultX;
			entity.solidArea.y=entity.solidAreaDefaultY;
			target[i].solidArea.x=target[i].solidAreaDefaultX;
			target[i].solidArea.y=target[i].solidAreaDefaultY;
			
		}}
		return index;
	}

	public boolean checkPlayer(Entity entity) {
		boolean contactPlayer=false;
		//get entity's solid area position
		entity.solidArea.x=entity.worldX+entity.solidArea.x;
		entity.solidArea.y=entity.worldY+entity.solidArea.y;
		
		//get the object's soid area position
		gp.player.solidArea.x=gp.player.worldX+gp.player.solidArea.x;
		gp.player.solidArea.y=gp.player.worldY+gp.player.solidArea.y;
		
		switch(entity.direction) {
		case "up":entity.solidArea.y-=entity.speed;break;
		case "down":entity.solidArea.y+=entity.speed;break;
		case "left":entity.solidArea.x-=entity.speed;break;
		case "right":entity.solidArea.x-=entity.speed;break;
		}
		if(entity.solidArea.intersects(gp.player.solidArea)) {
			System.out.println("up collision!");
			entity.collisionOn=true;
			contactPlayer=true; //cand entitatea intersecteaza playerul
		}
		//reseting the values
		entity.solidArea.x=entity.solidAreaDefaultX;
		entity.solidArea.y=entity.solidAreaDefaultY;
		gp.player.solidArea.x=gp.player.solidAreaDefaultX;
		gp.player.solidArea.y=gp.player.solidAreaDefaultY;
		
		return contactPlayer;
	}
	
}



