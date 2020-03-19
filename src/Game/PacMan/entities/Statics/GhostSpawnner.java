package Game.PacMan.entities.Statics;

import java.util.Random;

import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Main.Handler;
import Resources.Images;

//I misspelled Spawner, not going to change it, too many places to fix for something that won't break my code.
public class GhostSpawnner extends BaseDynamic{
	
	private int spawnTime = 0, color = 0;// 0. Red   1. Pink  2. Blue  3. Orange
	private Random rand = new Random();
	private boolean[] colorAvailability;//true if occupied. List of available colors.
	
	public GhostSpawnner(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler, Images.pacmanDots[2]);
	}
	
	public void tick() {
		colorAvailability = new boolean[4];
		setAvailability();
		if (handler.getMap().getEnemiesOnMap().size() < 6) {
			int pos = this.checkAvailablePos();
			if (pos != -1) {
				this.color = pos;
			}
			if(spawnTime == 0) {
				spawnTime = (rand.nextInt(9) + 1) * 60;
			}else {
				spawnTime--;
			}
		}else {
			spawnTime = (rand.nextInt(9) + 1) * 60;
		}

	}
	
	//sets up all taken colors
	private void setAvailability() {
		for (BaseDynamic ghost: handler.getMap().getEnemiesOnMap()) {
			if (ghost instanceof Ghost) {
				colorAvailability[((Ghost) ghost).color] = true;
			}
		}
	}
	
	//checks if there is a unused color, if there is, returns the corresponding number, else returns -1.
	public int checkAvailablePos() {
		for (int i = 0; i < colorAvailability.length; i++) {
			if(!colorAvailability[i])
				{return i;}
		}return -1;
	}
	
	public int getSpawnTime() {
		return spawnTime;
	}
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}

}
