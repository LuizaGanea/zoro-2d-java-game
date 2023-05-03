package main;
import monster.DogFactory;
public class AssetSetter {
	GamePanel gp;
	public AssetSetter(GamePanel gp) {
		this.gp=gp;
	}
	
	public void setObject() {
	}
	public void setMonster() {
		int i=0;
		DogFactory MON_Fabrica=new DogFactory();
		//Deci fiecare new MON_Caine(gp); se inlocuieste cu MON_Fabrica.buildDog(gp);
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*22;
		gp.monster[i].worldY=gp.tileSize*36;
		i++;
		
		//adica ceva de genul
		//gp.monster[i]=new MON_Fabrica.buildDog(gp);?
		//Fara new, am zis ca tot se inlocuieste
		//fa tu la un ,onstru ca exemplu si fac 
		//Poti continua
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*23;
		gp.monster[i].worldY=gp.tileSize*37;
		i++;
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*49;
		gp.monster[i].worldY=gp.tileSize*42;
		i++;
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*50;
		gp.monster[i].worldY=gp.tileSize*43;
		i++;
		
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*47;
		gp.monster[i].worldY=gp.tileSize*45;
		i++;
		
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*48;
		gp.monster[i].worldY=gp.tileSize*46;
		i++;
		
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*23;
		gp.monster[i].worldY=gp.tileSize*46;
		i++;
		
		gp.monster[i]=MON_Fabrica.buildDog(gp);
		gp.monster[i].worldX=gp.tileSize*24;
		gp.monster[i].worldY=gp.tileSize*46;
		i++;

	}
}