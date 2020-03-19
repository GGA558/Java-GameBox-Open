package Game.PacMan.entities.Statics;

import java.util.Random;

import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Main.Handler;
import Resources.Images;

//I misspelled Spawner, not going to change it, too many places to fix for something that won't break my code.
public class GhostSpawnner extends BaseDynamic {

	private int spawnTime = 0, color = 0;// 0. Red 1. Pink 2. Blue 3. Orange
	private Random rand = new Random();
	private boolean[] colorAvailability;// true if occupied. List of available colors.

	public GhostSpawnner(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler, Images.pacmanDots[2]);
	}

	public void tick() {
		int pos;
		colorAvailability = new boolean[4];
		setAvailability();
		if (handler.getMap().getEnemiesOnMap().size() < 4 * (handler.getPacManState().level + 1) + 2) {
			if (handler.getPacManState().level > 0) {
				pos = rand.nextInt(4);
				if (checkAvailablePos(pos)) {
					this.color = pos;
				} else {
					this.color = getAvailablePos();
				}
			} else {
				pos = this.getAvailablePos();
				if (pos != -1) {
					this.color = pos;
				}
				if (spawnTime == 0) {
					spawnTime = (rand.nextInt(9) + 1) * 60;
				} else {
					spawnTime--;
				}
			}
		} else {
			spawnTime = (rand.nextInt(9) + 1) * 60;
		}

	}

	// sets up all taken colors
	private void setAvailability() {
		int red = 0, blue = 0, pink = 0, orange = 0;
		for (BaseDynamic ghost : handler.getMap().getEnemiesOnMap()) {
			if (ghost instanceof Ghost) {
				if (handler.getPacManState().level == 0) {
					colorAvailability[((Ghost) ghost).color] = true;
				} else if (handler.getPacManState().level == 1) {
					switch (((Ghost) ghost).color) {
					case 0:
						red++;
						break;
					case 1:
						pink++;
						break;
					case 2:
						blue++;
						break;
					case 3:
						orange++;
						break;
					}
					if (red == 2)
						colorAvailability[0] = true;
					if (pink == 2)
						colorAvailability[1] = true;
					if (blue == 2)
						colorAvailability[2] = true;
					if (orange == 2)
						colorAvailability[3] = true;
				} else if (handler.getPacManState().level == 2) {
					switch (((Ghost) ghost).color) {
					case 0:
						red++;
						break;
					case 1:
						pink++;
						break;
					case 2:
						blue++;
						break;
					case 3:
						orange++;
						break;
					}
					if (red == 3)
						colorAvailability[0] = true;
					if (pink == 3)
						colorAvailability[1] = true;
					if (blue == 3)
						colorAvailability[2] = true;
					if (orange == 3)
						colorAvailability[3] = true;
				}
			}
		}
	}

	// checks if there is a unused color, if there is, returns the corresponding
	// number, else returns -1.
	public int getAvailablePos() {
		for (int i = 0; i < colorAvailability.length; i++) {
			if (!colorAvailability[i]) {
				return i;
			}
		}
		return -1;
	}

	// checks if the parameter is a unused color, if it is, returns true, else
	// returns false.
	public boolean checkAvailablePos(int pos) {
		if (!colorAvailability[pos]) {
			return true;
		}
		return false;
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
