package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Ghost extends BaseDynamic{

    protected double velX,velY,speed = 1;
    public String facing = "Up";
    public boolean moving = true,turnFlag = false,justSpawned = true;
    public Animation leftAnimG,rightAnimG,upAnimG,downAnimG;
    public Random rand = new Random();
    int turnCooldown = 30, direction;
// justSpawned makes sure it doesn't crawl back into the hole it came from.
// direction is used to change the direction of the ghost when needed.

    public Ghost(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.ghost);
        leftAnimG = new Animation(128,Images.blinkyLeft);
        rightAnimG = new Animation(128,Images.blinkyRight);
        upAnimG = new Animation(128,Images.blinkyUp);
        downAnimG = new Animation(128,Images.blinkyDown);
    }

    @Override
    public void tick(){

        switch (facing){
            case "Right":
                x+=velX;
                rightAnimG.tick();
                break;
            case "Left":
                x-=velX;
                leftAnimG.tick();
                break;
            case "Up":
                y-=velY;
                upAnimG.tick();
                break;
            case "Down":
                y+=velY;
                downAnimG.tick();
                break;
        }
        if (turnCooldown<=0){
            turnFlag= false;
            turnCooldown = 30;
        }
        if (turnFlag){
            turnCooldown--;
        }
        

//        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)  ||handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) && !turnFlag&& checkPreVerticalCollisions("Up")){
//            facing = "Up";
//            turnFlag = true;
//        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)) && !turnFlag&& checkPreHorizontalCollision("Left")){
//            facing = "Left";
//            turnFlag = true;
//        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)) && !turnFlag && checkPreHorizontalCollision("Right")){
//            facing = "Right";
//            turnFlag = true;
//        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) && !turnFlag&& checkPreVerticalCollisions("Down")){
//            facing = "Down";
//            turnFlag = true;
//        }

        if (facing.equals("Right") || facing.equals("Left")){
            checkHorizontalCollision();
        }else{
            checkVerticalCollisions();
        }

    }

    // I added code so that when colliding, change direction.
    public void checkVerticalCollisions() {
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();

        boolean ghostDies = false;
        boolean toUp = moving && facing.equals("Up");

        Rectangle ghostBounds = toUp ? ghost.getTopBounds() : ghost.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    velY = 0;
                    if (toUp) {
                        ghost.setY(brick.getY() + ghost.getDimension().height);
                        if(justSpawned) {
                        	direction = rand.nextInt(2);
                        	justSpawned = false;
                        }
                        else {direction = rand.nextInt(3);}
                    	switch(direction) {
                    	case 0:
                    		facing = "Right";
                    		break;
                    	case 1:
                    		facing = "Left";
                    		break;
                    	case 2:
                    		facing = "Down";
                    		break;
                    	}
                    }else {
						ghost.setY(brick.getY() - brick.getDimension().height);
					    direction = rand.nextInt(3);
					    switch (direction) {
					    case 0:
						    facing = "Right";
						    break;
					    case 1:
						    facing = "Left";
						    break;
					    case 2:
						    facing = "Up";
						    break;
					    }
                	}
                }
            }
        }

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = !toUp ? enemy.getTopBounds() : enemy.getBottomBounds();
            if (ghostBounds.intersects(enemyBounds)) {
                ghostDies = true;
                break;
            }
        }

        if(ghostDies) {
            handler.getMap().reset();
        }
    }


    public boolean checkPreVerticalCollisions(String facing) {
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();

        boolean ghostDies = false;
        boolean toUp = moving && facing.equals("Up");

        Rectangle ghostBounds = toUp ? ghost.getTopBounds() : ghost.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;

    }


    // I added code so that when colliding, change direction.
    public void checkHorizontalCollision(){
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        velX = speed;
        boolean ghostDies = false;
        boolean toRight = moving && facing.equals("Right");

        Rectangle ghostBounds = toRight ? ghost.getRightBounds() : ghost.getLeftBounds();

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (ghostBounds.intersects(enemyBounds)) {
                ghostDies = true;
                break;
            }
        }

        if(ghostDies) {
            handler.getMap().reset();
        }else {

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (ghostBounds.intersects(brickBounds)) {
                        velX = 0;
                        if (toRight) {
                            ghost.setX(brick.getX() - ghost.getDimension().width);
    					    direction = rand.nextInt(3);
    					    switch (direction) {
    					    case 0:
    						    facing = "Down";
    						    break;
    					    case 1:
    						    facing = "Left";
    						    break;
    					    case 2:
    						    facing = "Up";
    						    break;
    					    }
                        }
                        else {
                            ghost.setX(brick.getX() + brick.getDimension().width);
    					    direction = rand.nextInt(3);
    					    switch (direction) {
    					    case 0:
    						    facing = "Up";
    						    break;
    					    case 1:
    						    facing = "Down";
    						    break;
    					    case 2:
    						    facing = "Right";
    						    break;
    					    }
                        }
                    }
                }
            }
        }
    }


    public boolean checkPreHorizontalCollision(String facing){
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        velX = speed;
        boolean toRight = moving && facing.equals("Right");

        Rectangle ghostBounds = toRight ? ghost.getRightBounds() : ghost.getLeftBounds();

        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                if (ghostBounds.intersects(brickBounds)) {
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

}
