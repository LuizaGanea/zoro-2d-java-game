package monster;

import entity.Entity;
import main.GamePanel;

public class DogFactory {
//Deci adaug metoda de constructie
	//Si cred ca e suficient, acum trebuie sa-mi arati unde creeai inamicii
	 //sa dam save

	public Entity buildDog(GamePanel gp) {
		return new MON_Caine(gp);
	}
}
//cred ca 