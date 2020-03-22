package Game.GameStates;

import Display.UI.UIManager;
import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.GhostSpawnner;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.Dot;
import Game.PacMan.entities.Statics.Teleporter;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class PacManState extends State {

	private UIManager uiManager;
	public String Mode = "Intro";
	private Random rand = new Random();
	public int startCooldown = 60 * 4;// seven seconds for the music to finish
	public int level = 0;

	public PacManState(Handler handler) {
		super(handler);
		handler.setMap(MapBuilder.createMap(Images.maps[0], handler));

	}

	@Override
	public void tick() {
		
		if(handler.getPacman().getHealth() == 0) {
			Mode = "End";	
	    }
		
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
			if (startCooldown <= 0) {
				ArrayList<BaseDynamic> toREMove = new ArrayList<>();
				ArrayList<BaseDynamic> toAdd = new ArrayList<>();
				for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
					entity.tick();
					if (entity instanceof GhostSpawnner) {
						if (((GhostSpawnner) entity).getSpawnTime() == 0) {
							toAdd.add(new Ghost(entity.x, entity.y, MapBuilder.pixelMultiplier,
									MapBuilder.pixelMultiplier, handler, ((GhostSpawnner) entity).getColor()));
						}else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)){
							toAdd.add(new Ghost(entity.x, entity.y, MapBuilder.pixelMultiplier, MapBuilder.pixelMultiplier, handler, rand.nextInt(4)));
					}
					}	
					if (entity.getBounds().intersects(handler.getPacman().getBounds()) && entity instanceof Ghost) {
//                        handler.getMusicHandler().playEffect("pacman_chomp.wav");                        
//                        toREMove.add(entity);
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
		} else if (Mode.equals("End")) {
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)) {
				Mode = "Intro";
				handler.getPacman().health = 3;
				handler.getScoreManager().setPacmanCurrentScore(0);
				startCooldown = 60 * 4 + 10;
			}
		} 
		else {
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
		} else if (Mode.equals("End")) {
			//Graphics2D g2 = (Graphics2D) g.create();
			//handler.getMap().drawMap(g2);
			g.setColor(Color.RED);	
			g.setFont(new Font("TimesRoman", Font.ITALIC, 46));
			g.drawString("GAME OVER!",handler.getWidth()/3,handler.getHeight()/8);
			g.drawString("YOUR SCORE: " + String.valueOf(handler.getScoreManager().getPacmanCurrentScore()),handler.getWidth()/3,handler.getHeight()/5);
			g.drawString("HIGH SCORE: " + String.valueOf(handler.getScoreManager().getPacmanHighScore()),handler.getWidth()/3,handler.getHeight()/3);
			g.drawString("PRESS 'ENTER' TO TRY AGAIN!",handler.getWidth()/3,handler.getHeight()/2);	
		}  
		else {
			g.drawImage(Images.intro, 0, 0, handler.getWidth() / 2, handler.getHeight(), null);

		}
	}

	@Override
	public void refresh() {

	}

}
