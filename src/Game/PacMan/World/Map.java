package Game.PacMan.World;

import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.Dot;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {

    ArrayList<BaseStatic> blocksOnMap;
    ArrayList<BaseDynamic> enemiesOnMap;
    Handler handler;
    private double bottomBorder;
    private Random rand;
    private int mapBackground, toggle = 0;
    private boolean isOn = true;
    // toggle and isOn are used to make the dots blink.

    public Map(Handler handler) {
        this.handler=handler;
        this.rand = new Random();
        this.blocksOnMap = new ArrayList<>();
        this.enemiesOnMap = new ArrayList<>();
        bottomBorder=handler.getHeight();
        this.mapBackground = this.rand.nextInt(6);
    }

    public void addBlock(BaseStatic block){
        blocksOnMap.add(block);
    }

    public void addEnemy(BaseDynamic entity){

        enemiesOnMap.add(entity);

    }

    public void drawMap(Graphics2D g2) {
    	/*Every 12 ticks, flip the switch(isOn)
    	 *I need to reset toggle eventually because ints have a finite ammount of numbers it can use.
    	 *toggle resets every 60 ticks.
    	 */
    	toggle++;
		if(toggle % 12 == 0) {
			isOn = !isOn;
		}if(toggle >= 60) {
			toggle = 0;
		}
		for (int i = 0; i< handler.getPacman().getHealth();i++) {
            g2.drawImage(Images.pacmanRight[0], (handler.getWidth() - handler.getWidth() / 4 - handler.getWidth() / 15) + ((handler.getPacman().width*5)*i), handler.getHeight()-handler.getHeight()/4, handler.getWidth() / 18, handler.getHeight() / 12, null);
        }
        for (BaseStatic block:blocksOnMap) {
        	if(block instanceof BigDot) {
        		if(isOn) {//Big Dot appears
        			block.sprite = Resources.Images.pacmanDots[0];
				}else {//Big Dot disappears
					block.sprite = Resources.Images.pacmanDots[2];	
				}
			}if(block instanceof Dot){
				if(((Dot) block).fruitSprite != -1) {
					block.sprite = Resources.Images.pacmanFruits[((Dot) block).fruitSprite];
				}
			}
            g2.drawImage(block.sprite, block.x, block.y, block.width, block.height, null);
        }
        
        for (BaseDynamic entity:enemiesOnMap) {
            if (entity instanceof PacMan) {
                switch (((PacMan) entity).facing){
                    case "Right":
                        g2.drawImage(((PacMan) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Left":
                        g2.drawImage(((PacMan) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Up":
                        g2.drawImage(((PacMan) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Down":
                        g2.drawImage(((PacMan) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                }
            }else if (entity instanceof Ghost) {
                 switch (((Ghost) entity).facing){
                    case "Right":
                        g2.drawImage(((Ghost) entity).rightAnimG.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Left":
                        g2.drawImage(((Ghost) entity).leftAnimG.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Up":
                        g2.drawImage(((Ghost) entity).upAnimG.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Down":
                        g2.drawImage(((Ghost) entity).downAnimG.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                }
            }
            else {
                g2.drawImage(entity.sprite, entity.x, entity.y, entity.width, entity.height, null);
            }
        }

    }

    public ArrayList<BaseStatic> getBlocksOnMap() {
        return blocksOnMap;
    }

    public ArrayList<BaseDynamic> getEnemiesOnMap() {
        return enemiesOnMap;
    }

    public double getBottomBorder() {
        return bottomBorder;
    }

    public void reset() {
    }
}
