package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed,enterPressed,shotKeyPressed;
	
	public KeyHandler(GamePanel gp) { //constructor
		this.gp=gp;
	}
	//  ACESTA IL VOI FOLOSI PT PAUZA SI CELELALTE STATE-URI
	 public void keyPressed(KeyEvent e) {
		int code=e.getKeyCode();
		if(gp.gameState==gp.titleState) {
			titleState(code);
		}
		//play state
		else if(gp.gameState==gp.playState) {
			playState(code);
				
		}
		//pause state
		else if(gp.gameState==gp.pauseState) {
			pauseState(code);
		}
		
		//character state
		else if(gp.gameState==gp.characterState) {
			characterState(code);
		}
		
		//game over state
		else if(gp.gameState==gp.gameOverState) {
			gameOverState(code);
		}
	}
	 
	 
	 public void gameOverState(int code) {
		 if(code==KeyEvent.VK_W) {
			 gp.text_ecran.commandNum--;
			 if(gp.text_ecran.commandNum<0) {
				 gp.text_ecran.commandNum=1;
			 }
			 gp.playSE(3);
		 }
		 if(code==KeyEvent.VK_S) {
			 gp.text_ecran.commandNum++;
			 if(gp.text_ecran.commandNum>1) {
				 gp.text_ecran.commandNum=0;
			 } 
		 }
		 if(code==KeyEvent.VK_ENTER) {
				if(gp.text_ecran.commandNum==0) { //daca selectez retry
					gp.gameState=gp.playState;
					gp.retry();
					
				}
				else if(gp.text_ecran.commandNum==1) {
					gp.gameState=gp.titleState;
					gp.restart();
				}
			} 
	 }
		
	 public void titleState(int code) {
			//title state
		
				if(code==KeyEvent.VK_W) {
					gp.text_ecran.commandNum--;
					if(gp.text_ecran.commandNum<0) {
						gp.text_ecran.commandNum=2;
					}
				}
				if(code==KeyEvent.VK_S) {
					gp.text_ecran.commandNum++;
					if(gp.text_ecran.commandNum>2) {
						gp.text_ecran.commandNum=0;
					}
				}
				if(code==KeyEvent.VK_ENTER) {
					if(gp.text_ecran.commandNum==0) { //selectam new game
						gp.gameState=gp.playState;
						gp.playMusic(0);
					}
					//if(gp.text_ecran.commandNum==1) { //selectam load game// nu mai fac loadf game
						//urmeaza
					//}
					if(gp.text_ecran.commandNum==2) { //selectam quit
						System.exit(0);
					}
				}
				
			}
		 
	public void playState(int code) {
		 if(code==KeyEvent.VK_W) {
				upPressed=true;
			}
			if(code==KeyEvent.VK_S) {
				downPressed=true;
			}
			if(code==KeyEvent.VK_A) {
				leftPressed=true;
			}
			if(code==KeyEvent.VK_D) {
				rightPressed=true;
			}
			if(code==KeyEvent.VK_P) {
				gp.gameState=gp.pauseState;
			}
			if(code==KeyEvent.VK_C) {
				gp.gameState=gp.characterState;
			}
			if(code==KeyEvent.VK_ENTER) {
				enterPressed=true;
			}
			if(code==KeyEvent.VK_F) {
				shotKeyPressed=true;
			}
	 }
	 
	public void pauseState(int code) {
		 if(code==KeyEvent.VK_P) {
				
				gp.gameState=gp.playState;
				}
	 }
	 
	public void characterState(int code) {
		 if(code==KeyEvent.VK_C) {
				gp.gameState=gp.playState;
			}
		}

	public void keyReleased(KeyEvent e) {
		int code=e.getKeyCode();  //cand tasta nu e apsata
		
		if(code==KeyEvent.VK_W) {
			upPressed=false;
		}
		if(code==KeyEvent.VK_S) {
			downPressed=false;
		}
		if(code==KeyEvent.VK_A) {
			leftPressed=false;
		}
		if(code==KeyEvent.VK_D) {
			rightPressed=false;
		}
		if(code==KeyEvent.VK_F) {
			shotKeyPressed=false;
		}
	}

	public void keyTyped(KeyEvent e) {
		//System.out.println(" ");
	}
}
