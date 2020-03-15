package Game.PacMan.entities.Statics;

import java.util.Random;

import Main.Handler;
import Resources.Images;

public class Dot extends BaseStatic{
	
	Random rand = new Random();
	public int fruitChance, fruitSprite;
	
    public Dot(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.pacmanDots[1]);
        this.fruitChance = rand.nextInt(30);
        if(fruitChance == 15) {
        	this.fruitSprite = rand.nextInt(5);
        }else {
        	this.fruitSprite = -1;
        }
    }
}
