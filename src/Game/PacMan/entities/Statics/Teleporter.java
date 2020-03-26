package Game.PacMan.entities.Statics;

import java.awt.image.BufferedImage;

import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Main.Handler;
import Resources.Images;

public class Teleporter extends BaseStatic {

	public Teleporter(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler, Images.pacmanDots[2]);
	}

	public void teleportationSequence() {
		for (BaseStatic isTeleporter : handler.getMap().getBlocksOnMap()) {
			if (isTeleporter instanceof Teleporter) {
				if (this.x != isTeleporter.x || this.y != isTeleporter.y) {
					if (this.x > isTeleporter.x) {
						handler.getPacman().setX(isTeleporter.x + handler.getPacman().getWidth());
						handler.getPacman().setY(isTeleporter.y);
					} else if (this.y > isTeleporter.y) {
						handler.getPacman().setX(isTeleporter.x);
						handler.getPacman().setY(isTeleporter.y + handler.getPacman().getHeight());
					} else if (this.x < isTeleporter.x) {
						handler.getPacman().setX(isTeleporter.x - handler.getPacman().getWidth());
						handler.getPacman().setY(isTeleporter.y);
					} else if (this.y < isTeleporter.y) {
						handler.getPacman().setX(isTeleporter.x);
						handler.getPacman().setY(isTeleporter.y - handler.getPacman().getHeight());
					}
				}
			}
		}
	}
	
	public void teleportationSequence(Ghost ghost) {
		for (BaseStatic isTeleporter : handler.getMap().getBlocksOnMap()) {
			if (isTeleporter instanceof Teleporter) {
				if (this.x != isTeleporter.x || this.y != isTeleporter.y) {
					if (this.x > isTeleporter.x) {
						ghost.setX(isTeleporter.x + handler.getPacman().getWidth());
						ghost.setY(isTeleporter.y);
					} else if (this.y > isTeleporter.y) {
						ghost.setX(isTeleporter.x);
						ghost.setY(isTeleporter.y + handler.getPacman().getHeight());
					} else if (this.x < isTeleporter.x) {
						ghost.setX(isTeleporter.x - handler.getPacman().getWidth());
						ghost.setY(isTeleporter.y);
					} else if (this.y < isTeleporter.y) {
						ghost.setX(isTeleporter.x);
						ghost.setY(isTeleporter.y - handler.getPacman().getHeight());
					}
				}
			}
		}
	}
	
	public BaseDynamic boundsIntersectGhost() {
		for (BaseDynamic ghost: handler.getMap().getEnemiesOnMap()) {
			if (ghost instanceof Ghost) {
				if(ghost.getBounds().intersects(this.getBounds())) {
					return ghost;
				}
			}
		}return null;
	}
}
