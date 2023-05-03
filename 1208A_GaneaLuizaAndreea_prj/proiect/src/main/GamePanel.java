package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable { //gamepanel=ecranul jocului
	//Setari ale ecranului
	final int originalTileSize=16; //16x16 dala=default size pt caracter si dale
	final int scale=3; //scale
	public final int tileSize=originalTileSize*scale; //16x3=48  48x48 dala
	public final int maxScreenCol=16;
	public final int maxScreenRow=12;
	public final int screenWidth=tileSize*maxScreenCol; //768 pixeli
	public final int screenHeight=tileSize*maxScreenRow; // 576 pixeli
	public int actionLockCounter=0;
	
	//WORLD SETTINGS
	public final int maxWorldCol=50;
	public final int maxWorldRow=50;
	//public final int worldWidth=tileSize*maxWorldCol; //aceste 2 var le folosim ca sa oprim worldcamera at the edge 
	//public final int worldHeight=tileSize*maxWorldRow; //DAR NU AM REUSIT
	
	//FPS 60
	int FPS=60;
	TileManager tileM=new TileManager(this);//instantiem tile manager
	public KeyHandler keyH=new KeyHandler(this);
	public CollisionChecker cChecker=new CollisionChecker(this);
	public AssetSetter aSetter=new AssetSetter(this);
	Thread gameThread;                //noi il pornim si il oprim
	public text text_ecran=new text(this);
	
	//sound
	Sound se= new Sound(); //sound effects
	Sound music=new Sound();
	
	
	//entity and object
	public Player player=Player.getInstance(this,keyH);
	public Entity obj[] = new Entity[10];
	public Entity npc[]=new Entity[10];
	public Entity monster[] = new Entity[10];
	public ArrayList<Entity> projectileList=new ArrayList<>();
	ArrayList<Entity> entityList=new ArrayList<>();
	
	
	//game state (avem jocul in functiune 1, si pe pauza 2, etc
	public int gameState;
	public final int titleState=0;
	public final int playState=1;
	public final int pauseState=2;
	public final int characterState=4;
	public final int gameOverState=5;
//o metoda
	//Variabile pentru baza de date

	static Connection c = null;
	public static Statement stmt = null;
	public static int score=0;
	public String sql1="";
	public static boolean firstTime=false;


	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); //desenele din aceasta componenta vor fi intr un painting buffer
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		aSetter.setObject();
		aSetter.setMonster();
		gameState=titleState;
	}
	public void retry() {
		player.setDefaultPositions();
		player.restoreLifeAndMana();
		aSetter.setMonster();
		aSetter.setMonster();
	}
	public void restart() {
		player.setDefaultValues();
		player.setDefaultPositions();
		player.restoreLifeAndMana();
	}
	
	public void startGameThread() {
		gameThread=new Thread(this);
		gameThread.start();
	}
	
	public void run() { //metoda apelata cand pornim thread ul
		double drawInterval=1000000000/FPS; //ecranul se updateaza la fiecare 0,0166666 secunde
		double nextDrawTime=System.nanoTime()+drawInterval;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:company.db");
			stmt = c.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS ZORO "+" (Score INT)";
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		while(gameThread!=null) //se repeta procesul
		{
			//facem loop ul jocului
			//UPDATE pozitia eroului
			update();
			//DRAW desenarea ecranului cu informatiile updatate
			repaint(); //asa apelam paintComponent
			
			try {
				double remainingTime=nextDrawTime-System.nanoTime(); //aflam timpul ramas pana la urmatorul uodate
				remainingTime=remainingTime/1000000; //transf in milisec pt ca sleep accepta doar mili
				if(remainingTime<0) {
					remainingTime=0;
				}
				Thread.sleep((long)remainingTime);
				nextDrawTime+=drawInterval;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void update() { //miscari sus jos stanga dreapta cand apas tastele
		if(gameState==playState) { //updatam info ale playerului doar cand suntem in modul joc
			player.update();
			
			  for(int i=0;i<monster.length;i++) {
			 
				if(monster[i]!=null) {
					if(monster[i].alive==true&&monster[i].dying==false){
					monster[i].update();
						}
					
					if(monster[i].alive==false){
					monster[i]=null;
					score+=100;
					}
			    }
			}
			  
			  for(int i=0;i<projectileList.size();i++) {
					 
					if(projectileList.get(i)!=null) {
						if(projectileList.get(i).alive==true){
							projectileList.get(i).update();
							}
						
						if(projectileList.get(i).alive==false){
							projectileList.remove(i);
						}
				    }
				}
		}
		
		if(gameState==pauseState) {
		//
		}
	}
	
	public void paintComponent(Graphics g) { //metoda standard de a desena pe panel,Graphics clasa cu functii de desenare
		
		super.paintComponent(g); 
		Graphics2D g2=(Graphics2D)g;  //schimbam clasa Graphics g cu Graphics2D , sunt 2 clase similare dar 2d e mai buna
		
		//title screen
		if(gameState==titleState) {
			try {
				text_ecran.draw(g2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			//tile
			tileM.draw(g2); //inainte de player.draw, ca sa fie background
			//add entities to the list
			entityList.add(player);
			
			for(int i=0;i<obj.length;i++) {
				if(obj[i]!=null) {
					entityList.add(obj[i]);
				}
			}
			
			for(int i=0;i<monster.length;i++) {
				if(monster[i]!=null) {
					entityList.add(monster[i]);
				}
			}
			for(int i=0;i<projectileList.size();i++) {
				if(projectileList.get(i)!=null) {
					entityList.add(projectileList.get(i));
				}
			}
			
			//SORT ENTITIES
			Collections.sort(entityList,new Comparator<Entity>() {
				//Hai ca ma uit eu.
			
			public int compare(Entity e1, Entity e2) {
				int result=Integer.compare(e1.worldY, e2.worldY);
				return result;
			}
			});
			
			//DRAW ENTITIES
			for(int i=0;i<entityList.size();i++) {
				entityList.get(i).draw(g2);;
			}
			
			//EMPTY ENTITY LIST
			entityList.clear();
		try {
			text_ecran.draw(g2);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
		g2.dispose(); //dispose the GRaphics2D when we're done
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) { //sound effects
		se.setFile(i);;
		se.play();
	}
}
