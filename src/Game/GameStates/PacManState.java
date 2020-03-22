package Game.GameStates;

import Display.UI.UIManager;
import Game.PacMan.World.Map;
import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.GhostSpawnner;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.Dot;
import Game.PacMan.entities.Statics.Teleporter;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacManState extends State {

	private UIManager uiManager;
	public String Mode = "Intro";
	public int startCooldown = 60 * 4;// seven seconds for the music to finish
	public int level = 0, pacmanEatGhostTime = 0;

	public PacManState(Handler handler) {
		super(handler);
		handler.setMap(MapBuilder.createMap(Images.maps[0], handler));

	}

	@Override
	public void tick() {
		pacmanEatGhostTime--;
		if (Mode.equals("Stage")) {
			// Added code to change level
			if (handler.getMap().getDots() == 0) {
				level++;
				startCooldown = 60 * 4 + 10;
				if (level == Images.maps.length) {
					level = 0;
				}
				// tempHealth is used to make sure the health doesn't reset every level.
				int tempHealth = handler.getPacman().getHealth();
				handler.setMap(MapBuilder.createMap(Images.maps[level], handler));
				handler.getPacman().setHealth(tempHealth);
			}
			if (handler.getPacman().deathAnim.end) {
				handler.getPacman().deathAnim.reset();
				startCooldown = 60 * 5;
			}
			if (startCooldown <= 0) {
				ArrayList<BaseDynamic> toREMove = new ArrayList<>();
				ArrayList<BaseDynamic> toAdd = new ArrayList<>();
				for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
					entity.tick();
					if (entity instanceof GhostSpawnner) {
						if (((GhostSpawnner) entity).getSpawnTime() == 0) {
							toAdd.add(new Ghost(entity.x, entity.y, MapBuilder.pixelMultiplier,
									MapBuilder.pixelMultiplier, handler, ((GhostSpawnner) entity).getColor()));
						}
					}
					if (entity.getBounds().intersects(handler.getPacman().getBounds()) && entity instanceof Ghost) {
						if (pacmanEatGhostTime > 0) {
							handler.getMusicHandler().playEffect("pacman_eatghost.wav");
							handler.getScoreManager().addPacmanCurrentScore(500);
							toREMove.add(entity);
						}
					}
				}
				ArrayList<BaseStatic> toREmove = new ArrayList<>();
				for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
					if (blocks instanceof Dot) {
						if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
							toREmove.add(blocks);
							if (((Dot) blocks).fruitSprite == -1) {
								handler.getMusicHandler().playEffect("pacman_chomp.wav");
								handler.getScoreManager().addPacmanCurrentScore(10);
							} else {
								handler.getMusicHandler().playEffect("pacman_eatfruit.wav");
								handler.getScoreManager().addPacmanCurrentScore(120);
							}
						}
					} else if (blocks instanceof BigDot) {
						if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
							handler.getMusicHandler().playEffect("pacman_chomp.wav");
							pacmanEatGhostTime = 600;// Ten seconds for pacman to eat ghosts.
							toREmove.add(blocks);
							handler.getScoreManager().addPacmanCurrentScore(100);

						}
					} else if (blocks instanceof Teleporter) {
						if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
							((Teleporter) blocks).teleportationSequence();
						}
						if (((Teleporter) blocks).boundsIntersectGhost() != null) {
							((Teleporter) blocks)
									.teleportationSequence((Ghost) ((Teleporter) blocks).boundsIntersectGhost());
						}
					}
					if (handler.getScoreManager().getPacmanCurrentScore() > handler.getScoreManager()
							.getPacmanHighScore()) {
						handler.getScoreManager().setPacmanHighScore(handler.getScoreManager().getPacmanCurrentScore());
					}
				}
				for (BaseStatic removing : toREmove) {
					handler.getMap().getBlocksOnMap().remove(removing);
				}
				for (BaseDynamic Removing : toREMove) {
					handler.getMap().getEnemiesOnMap().remove(Removing);
				}
				if (toAdd.size() > 0) {
					for (BaseDynamic thingToAdd : toAdd) {
						handler.getMap().getEnemiesOnMap().add(thingToAdd);
					}
				}
			} else {
				startCooldown--;
				if (startCooldown == 60 * 4 + 1) {
					handler.getMusicHandler().playEffect("pacman_beginning.wav");
				}
			}
		} else if (Mode.equals("Menu")) {
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)) {
				Mode = "Stage";
				handler.getMusicHandler().playEffect("pacman_beginning.wav");
			}
		} else {
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)) {
				Mode = "Menu";
			}
		}

	}

	@Override
	public void render(Graphics g) {

		if (Mode.equals("Stage")) {
			Graphics2D g2 = (Graphics2D) g.create();
			handler.getMap().drawMap(g2);
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
			g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),
					(handler.getWidth() / 2) + handler.getWidth() / 6, 25);
			g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),
					(handler.getWidth() / 2) + handler.getWidth() / 6, 75);
		} else if (Mode.equals("Menu")) {
			g.drawImage(Images.start, 0, 0, handler.getWidth() / 2, handler.getHeight(), null);
		} else {
			g.drawImage(Images.intro, 0, 0, handler.getWidth() / 2, handler.getHeight(), null);

		}
	}

	@Override
	public void refresh() {

	}

}
