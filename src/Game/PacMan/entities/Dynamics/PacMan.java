package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Game.PacMan.entities.Statics.Teleporter;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacMan extends BaseDynamic{

	protected double velX,velY,speed = 1;
    public String facing = "Left";
    public boolean moving = true,turnFlag = false, justDied = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim,deathAnim;
    int turnCooldown = 20, deathCooldown = 0;
    private int health = 3;
    // OX and OY for Original x and y.
    public int myOX;
    public int myOY;
    

    public PacMan(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.pacmanRight[0]);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
        deathAnim = new Animation(128, Images.pacmanDeath);
        this.health = health;
    }

    @Override
    public void tick(){

        switch (facing){
            case "Right":
                x+=velX;
                rightAnim.tick();
                break;
            case "Left":
                x-=velX;
                leftAnim.tick();
                break;
            case "Up":
                y-=velY;
                upAnim.tick();
                break;
            case "Down":
                y+=velY;
                downAnim.tick();
                break;
            case "Dead":
            	deathAnim.tick();
            	deathCooldown--;
            	if(deathAnim.end) {
            		health--;
                	this.handler.getPacman().setX(myOX);
                	this.handler.getPacman().setY(myOY);
                	facing = "Left";
            	}else if (justDied && deathCooldown <= 0) {
                	handler.getMusicHandler().playEffect("pacman_death.wav");
                	justDied = false;
                	deathCooldown = 60;
            	}else if (justDied) {
                	justDied = false;
            	}
            	break;
        }
        if (turnCooldown<=0){
            turnFlag= false;
        }
        if (turnFlag){
            turnCooldown--;
        }

        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)) && !turnFlag && checkPreHorizontalCollision("Right") && facing != "Dead"){
            facing = "Right";
            turnFlag = true;
            turnCooldown = 20;
        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)) && !turnFlag&& checkPreHorizontalCollision("left") && facing != "Dead"){
            facing = "Left";
            turnFlag = true;
            turnCooldown = 20;
        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) && !turnFlag&& checkPreVerticalCollisions("Up") && facing != "Dead"){
            facing = "Up";
            turnFlag = true;
            turnCooldown = 20;
        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) && !turnFlag&& checkPreVerticalCollisions("Down") && facing != "Dead"){
            facing = "Down";
            turnFlag = true;
            turnCooldown = 20;
        }
        //Health cap = 4
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) && health < 4) {
    	      health++;
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)) {
        	handler.getMap().reset();
        	facing = "Dead";
        	handler.getMusicHandler().playEffect("pacman_death.wav");
        }

//        if (facing.equals("Right") || facing.equals("Left")){
//            checkHorizontalCollision();
//        }else{
//            checkVerticalCollisions();
//        }
        /* There is an odd bug that makes pacman immortal if he hits a wall and stays still.
         * By changing this code, that event happens much less often.
         */
        checkVerticalCollisions();
        checkHorizontalCollision();

    }

    public void checkVerticalCollisions() {
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();

        boolean pacmanDies = false;
        boolean toUp = moving && facing.equals("Up");

        Rectangle pacmanBounds = toUp ? pacman.getTopBounds() : pacman.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (pacmanBounds.intersects(brickBounds)) {
                    velY = 0;
                    if (toUp)
                        pacman.setY(brick.getY() + pacman.getDimension().height);
                    else
                        pacman.setY(brick.getY() - brick.getDimension().height);
                }
            }
        }

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = !toUp ? enemy.getTopBounds() : enemy.getBottomBounds();
            if (pacmanBounds.intersects(enemyBounds)) {
//            	this.handler.getPacman().setX(myOX);
//            	this.handler.getPacman().setY(myOY);
//            	health --;
                pacmanDies = true;
                break;
            }
        }

        if(pacmanDies) {
        	facing = "Dead";
        	justDied = true;
            handler.getMap().reset();
        }

    }


    public boolean checkPreVerticalCollisions(String facing) {
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();

        boolean pacmanDies = false;
        boolean toUp = moving && facing.equals("Up");

        Rectangle pacmanBounds = toUp ? pacman.getTopBounds() : pacman.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (pacmanBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;

    }



    public void checkHorizontalCollision(){
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        velX = speed;
        boolean pacmanDies = false;
        boolean toRight = moving && facing.equals("Right");

        Rectangle pacmanBounds = toRight ? pacman.getRightBounds() : pacman.getLeftBounds();

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (pacmanBounds.intersects(enemyBounds)) {
//            	this.handler.getPacman().setX(myOX);
//            	this.handler.getPacman().setY(myOY);
//            	health --;
                pacmanDies = true;
                break;
            }
        }

        if(pacmanDies) {
          	facing = "Dead";
          	justDied = true;
            handler.getMap().reset();
        }else {

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (pacmanBounds.intersects(brickBounds)) {
                        velX = 0;
                        if (toRight)
                            pacman.setX(brick.getX() - pacman.getDimension().width);
                        else
                            pacman.setX(brick.getX() + brick.getDimension().width);
                    }
                }
            }
        }
    }


    public boolean checkPreHorizontalCollision(String facing){
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        velX = speed;
        boolean toRight = moving && facing.equals("Right");

        Rectangle pacmanBounds = toRight ? pacman.getRightBounds() : pacman.getLeftBounds();

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (pacmanBounds.intersects(brickBounds)) {
                        return false;
                    }
                }
            }
        return true;
    }


    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }
    public int getHealth() {
    	return health;
    }
    public void setHealth(int health) {
    	this.health = health;
    }


}
