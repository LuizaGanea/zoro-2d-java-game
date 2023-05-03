package main;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Entity;
import object.OBJ_Heart;

public class text {
	GamePanel gp;
	Graphics2D g2;
	BufferedImage heart_full, heart_half, heart_blank;
	Font arial_45, arial_80B; //am modificat dimensiunea, nu mai e 45
	public boolean messageOn=false;
	//public String message=" ";
	ArrayList<String> message=new ArrayList<>();
	ArrayList<Integer> messageCounter=new ArrayList<>();
	//int messageCounter=0;
	public int commandNum=0; //sa apasam pe opt din meniu
	
	
	public text(GamePanel gp) {
		this.gp=gp;
		arial_45=new Font("Arial",Font.ITALIC,30);
		arial_80B=new Font("Arial",Font.BOLD,80);
		
		//inima
		Entity heart= new OBJ_Heart(gp);
		heart_full=heart.image;
		heart_half=heart.image2;
		heart_blank=heart.image3;
		
		
	} 
	
	public void addMessage(String text) { //pt mesajele de exp, damage etc
		message.add(text);
		messageCounter.add(0); //default value of the message counter, incepe de la 0
	}

	 public void drawTitleScreen() {
		g2.setColor(new Color(70,120,80));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
		String txt="Zoro";
		int x=getXforCenteredText(txt);
		int y=gp.tileSize*3;
		
		g2.setColor(Color.black);
		g2.drawString(txt, x+5, y+5); //umbre
		
		g2.setColor(Color.white);
		g2.drawString(txt,x,y);
		
		//meniu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
		txt="NEW GAME";
		x=getXforCenteredText(txt);
		y+=gp.tileSize*3;
		g2.drawString(txt, x, y);
		if(commandNum==0) {
			g2.drawString(">", x-gp.tileSize ,y);
		}
		
		
		/*
		txt="LOAD GAME";
		x=getXforCenteredText(txt);
		y+=gp.tileSize;
		g2.drawString(txt, x, y);
		if(commandNum==1) {
			g2.drawString(">", x-gp.tileSize ,y);
		}
		*/
		
		
		txt="QUIT";
		x=getXforCenteredText(txt);
		y+=gp.tileSize;
		g2.drawString(txt, x, y);
		if(commandNum==2) {
			g2.drawString(">", x-gp.tileSize ,y);
		}
		
	 }

	public void draw(Graphics2D g2) throws IOException {
		this.g2=g2;
		//nr de pietre din inventar=ifinit
		g2.setFont(arial_45); //fontul scrisului
		g2.setColor(Color.white);
		g2.drawString("Proiectile: infinit", 25, 100); //coordonatele indica baza scrisului
		
		//verificam gamestateul curent
		if(gp.gameState==gp.titleState) {
			drawTitleScreen();
		}
		if(gp.gameState==gp.playState) { 
			drawPlayerLife();
			drawMessage();
			gp.firstTime=false;

		}
		if(gp.gameState==gp.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		//character state
		if(gp.gameState==gp.characterState) {
			drawCharacterScreen();
		}
		//game over state
		if(gp.gameState==gp.gameOverState) { //cand moare playerul
			drawGameOverScreen();
			if (gp.firstTime == false) {
				try {
					gp.firstTime=true;
					gp.sql1 = "INSERT INTO ZORO(Score)" + "VALUES (" + gp.score + ")";
					gp.stmt.executeUpdate(gp.sql1);
					//Acum incearca sa faci un gameOver
					gp.stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	public void drawGameOverScreen() {
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD));
		
		text="GAME OVER";
		
		//umbra
		g2.setColor(Color.black);
		x=getXforCenteredText(text);
		y=gp.tileSize*4;
		g2.drawString(text, x, y);
		
		//main
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		//retry
		g2.setFont(g2.getFont().deriveFont(50f));
		text="Retry";
		x=getXforCenteredText(text);
		y+=gp.tileSize*4;
		g2.drawString(text, x, y);
		if(commandNum==0) {
			g2.drawString(">", x-40, y);
		}
		
		//back to the title screen
		text="Quit";
		x=getXforCenteredText(text);
		y+=55;
		g2.drawString(text, x, y);
		if(commandNum==1) {
			g2.drawString(">", x-40, y);
		}
		
	}
	
	public void drawCharacterScreen() {
		//create a frame
		final int frameX=gp.tileSize;
		final int frameY=gp.tileSize;
		final int frameWidth=gp.tileSize*5;
		final int frameHeight=gp.tileSize*10;
		drawSubWindow(frameX,frameY,frameWidth, frameHeight);
		
		//text in window
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		int textX=frameX+20;
		int textY=frameY+gp.tileSize;
		final int lineHeight=35;
		
		//names
		g2.drawString("Level", textX, textY);
		textY+=lineHeight;
		g2.drawString("Life", textX, textY);
		textY+=lineHeight;
		g2.drawString("Strength", textX, textY);
		textY+=lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY+=lineHeight;
		g2.drawString("Attack", textX, textY);
		textY+=lineHeight;
		g2.drawString("Defense", textX, textY);
		textY+=lineHeight;
		g2.drawString("Exp", textX, textY);
		textY+=lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY+=lineHeight;
		//g2.drawString("Coin", textX, textY);
		//textY+=lineHeight;
		g2.drawString("Weapon", textX, textY);
		textY+=lineHeight;
		g2.drawString("Shield", textX, textY);
		textY+=lineHeight;
		
		//values
		int tailX=(frameX+frameWidth)-30;
		//reset texty
		textY=frameY+gp.tileSize;
		String value;
		
		value =String.valueOf(gp.player.level);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		textY+=lineHeight;
		
		value =String.valueOf(gp.player.life+"/"+gp.player.maxLife);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		textY+=lineHeight;
		
		value =String.valueOf(gp.player.strength);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		textY+=lineHeight;
		
		value =String.valueOf(gp.player.dexterity);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		textY+=lineHeight;
		
		value =String.valueOf(gp.player.attack);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		textY+=lineHeight;
		
		value =String.valueOf(gp.player.defense);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		textY+=lineHeight;
		
		value =String.valueOf(gp.player.exp);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		textY+=lineHeight;
		
		value =String.valueOf(gp.player.nextLevelExp);
		textX=getXforAlignToRightText(value,tailX);
		g2.drawString(value,textX,textY);
		
		//weapon image
		g2.drawImage(gp.player.currentWeapon.down1, tailX-gp.tileSize,textY,null); 
		textY+=gp.tileSize;
		g2.drawImage(gp.player.currentShield.down1,tailX-gp.tileSize,textY,null);
	}
	
	public void drawSubWindow(int x, int y, int width, int height) {
		Color c =new Color(0,0,0,210);
		g2.setColor(c);;
		g2.fillRoundRect(x,y,width,height,35,35);
		c=new Color(255,255,255);
		g2.setColor(c);;
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
		
	}
	
	public void drawPlayerLife() {
		
		//gp.player.life=6;
		int x=gp.tileSize/2;
		int y=gp.tileSize/2;
		int i=0;
		//draw max life
		while(i<gp.player.maxLife/2) {// /2 pt ca 1viata=2inimi
			g2.drawImage(heart_blank, x,y,null); 
			i++;
			x+=gp.tileSize;
		}
		//reset pt valorile:
		x=gp.tileSize/2;
		y=gp.tileSize/2;
		i=0;
		//draw current life
		while(i<gp.player.life) {
			g2.drawImage(heart_half,x,y,null);
			i++;
			if(i<gp.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x+=gp.tileSize;
		}
	}
	
	public void drawMessage() {
		int messageX=gp.tileSize;
		int messageY=gp.tileSize*4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
		for(int i=0;i<message.size();i++) {
			if(message.get(i)!=null) {
				//mesaj
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				//umbra
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX+2, messageY+2);
				
				int counter=messageCounter.get(i)+1; //messageCounter++ dar e vector deci svriem asa
				messageCounter.set(i, counter); //set the counter to the array
				messageY+=50;
				
				//sa dispara esajele dupa cv timp
				if(messageCounter.get(i)>180) {
					message.remove(i);
					messageCounter.remove(i);
				}
				
			}
		}
	}
		
	public void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
		String txt="PAUSED"; //il afisam la mijlocul ecranului
		int x=getXforCenteredText(txt);
		
		int y=gp.screenHeight/2;
		g2.drawString(txt,x,y);
	}
	
	public int getXforCenteredText(String txt) {
		int length=(int)g2.getFontMetrics().getStringBounds(txt, g2).getWidth();
		int x=gp.screenWidth/2-length/2;
		return x;
	}
	public int getXforAlignToRightText(String txt, int tailX) {
		int length=(int)g2.getFontMetrics().getStringBounds(txt, g2).getWidth();
		int x=tailX-length;
		return x;
	}
	

}


