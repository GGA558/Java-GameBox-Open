package Game.GameStates;

import Display.UI.UIManager;
import Game.PacMan.World.Map;
import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.Dot;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacManState extends State {

    private UIManager uiManager;
    public String Mode = "Intro";
    public int startCooldown = 60*4;//seven seconds for the music to finish
    public int level = 0;

    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(Images.maps[0], handler));

    }


    @Override
    public void tick() {
        if (Mode.equals("Stage")){
        	//Added code to change level
        	if (handler.getMap().getDots() == 0) {
        		level++;
                startCooldown = 60*4 + 10;
        		if(level == Images.maps.length) {
        			level = 0;
        		}
                handler.setMap(MapBuilder.createMap(Images.maps[level], handler));
        	}
            if (startCooldown<=0) {
                for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                    entity.tick();
                }
                ArrayList<BaseStatic> toREmove = new ArrayList<>();
                for (BaseStatic blocks: handler.getMap().getBlocksOnMap()){
                    if (blocks instanceof Dot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                            handler.getMusicHandler().playEffect("pacman_chomp.wav");                        
                            toREmove.add(blocks);
                            if(((Dot) blocks).fruitSprite == -1){
                            	handler.getScoreManager().addPacmanCurrentScore(10);	
							} else {
								handler.getScoreManager().addPacmanCurrentScore(120);
							}
                        }
                    }else if (blocks instanceof BigDot){
                        if (blocks.getBounds().intersects(handler.getPacman().getBounds())){
                            handler.getMusicHandler().playEffect("pacman_chomp.wav");
                            toREmove.add(blocks);
                            handler.getScoreManager().addPacmanCurrentScore(100);

                        }
                    }if (handler.getScoreManager().getPacmanCurrentScore() > handler.getScoreManager().getPacmanHighScore()) {
                  	     handler.getScoreManager().setPacmanHighScore(handler.getScoreManager().getPacmanCurrentScore());
           	        }
                }
                for (BaseStatic removing: toREmove){
                    handler.getMap().getBlocksOnMap().remove(removing);
                }
            }else{
                startCooldown--;
                if(startCooldown == 60*4 + 1) {
                    handler.getMusicHandler().playEffect("pacman_beginning.wav");
        		}
            }
        }else if (Mode.equals("Menu")){
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Stage";
                handler.getMusicHandler().playEffect("pacman_beginning.wav");
            }
        }else{
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Menu";
            }
        }



    }

    @Override
    public void render(Graphics g) {

        if (Mode.equals("Stage")){
            Graphics2D g2 = (Graphics2D) g.create();
            handler.getMap().drawMap(g2);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),(handler.getWidth()/2) + handler.getWidth()/6, 25);
            g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),(handler.getWidth()/2) + handler.getWidth()/6, 75);
        }else if (Mode.equals("Menu")){
            g.drawImage(Images.start,0,0,handler.getWidth()/2,handler.getHeight(),null);
        }else{
            g.drawImage(Images.intro,0,0,handler.getWidth()/2,handler.getHeight(),null);

        }
    }

    @Override
    public void refresh() {

    }


}
