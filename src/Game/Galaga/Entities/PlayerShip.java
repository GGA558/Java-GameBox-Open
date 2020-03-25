package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerShip extends BaseEntity{

    private int health = 1,attackCooldown = 30,speed =6,destroyedCoolDown = 60*7;
    private boolean attacking = false, destroyed = false, justSpawned = true;
    private Animation deathAnimation;
    boolean[][] ocupancy = new boolean[5][8];
    // e for enemy
    int eRow, eCol, randCounter = random.nextInt(5)*60;
    
     public PlayerShip(int x, int y, int width, int height, BufferedImage sprite, Handler handler) {
        super(x, y, width, height, sprite, handler);

        deathAnimation = new Animation(256,Images.galagaPlayerDeath);
      
    }

    @Override
    public void tick() {
        super.tick();
        if(justSpawned) {
        	justSpawned = false;
        	initialSpawn();
        }else {
        	checkAvaliablePos();
        	if (enemyHasDied()) {
        		if (randCounter <=0) {
        		    randCounter = random.nextInt(5)*60;
        	        eRow = random.nextInt(4);
        	        eCol = random.nextInt(8);
        	        subsequentSpawn(eRow, eCol);
        		}else {
        			randCounter--;
        		}
        	}
        }
        
        if (destroyed){
            if (destroyedCoolDown<=0){
                destroyedCoolDown=60*7;
                destroyed=false;
                deathAnimation.reset();
                bounds.x=x;
                health--;
            }else{
                deathAnimation.tick();
                destroyedCoolDown--;
            }
        }else {
            if (attacking) {
                if (attackCooldown <= 0) {
                    attacking = false;
                } else {
                    attackCooldown--;
                }
            }
            
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !attacking) {
                handler.getMusicHandler().playEffect("laser.wav");
                
                //rapidfire powerup after every 1000 points
                
                int rapidfire=handler.getScoreManager().getGalagaCurrentScore();
                if(rapidfire>=3000) {
                	attackCooldown=15;
                }else if (rapidfire>=2000) {
                	attackCooldown=20; 
                }else if (rapidfire>=1000) {
                	attackCooldown=25; 
                }else {
                	attackCooldown=30;
                }
                attacking = true;
                handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
                 }

            if (handler.getKeyManager().left) {
                x -= (speed);
                if (x<=handler.getWidth()/4) {
                	x+=(speed);        		
                }
            }
            if (handler.getKeyManager().right) {
                x += (speed);
                if (x>=handler.getWidth()-handler.getWidth()/4-64) {
               	x-=(speed);
                }
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) && !destroyed ){
            	handler.getMusicHandler().playEffect("explosion.wav");
            	destroyed=true;
            	
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_H)){
            	health++;
            }

            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)) {
            	spawnBee(random.nextInt(3)+2, random.nextInt(8));

            }
            
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_O)) {
            	spawnEShip(random.nextInt(2), random.nextInt(8));

            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_T)){
            	handler.getGalagaState().setState(handler.getPauseState());
            }
                        
            bounds.x = x;
        }

    }

    @Override
    public void render(Graphics g) {
         if (destroyed){
        	 //If health==0 games restarts
        	 if(health==0) {
        		 	g.drawString("Game Over", handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);	
     	 			if(deathAnimation.end) {
     	 				handler.getGameProperties().reStart(true); 
        		 }
        	 }
             if (deathAnimation.end){
            	 	if(health!=0) {
            	 		g.drawString("READY",handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);
            	 	}
            	 	
             }else {
                 g.drawImage(deathAnimation.getCurrentFrame(), x, y, width, height, null);
             	}
         }else {
             super.render(g);
         }
      }	
    

    @Override
    public void damage(BaseEntity damageSource) {
        if (damageSource instanceof PlayerLaser){
            return;
        }
        destroyed = true;
        handler.getMusicHandler().playEffect("explosion.wav");

        bounds.x = -10;
       
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    
    // new code
    public void spawnBee(int r, int c) {
    	if (!ocupancy[r][c]) {
    		ocupancy[r][c] = true;
    		handler.getGalagaState().entityManager.entities.add(new EnemyBee(bounds.x, bounds.y,width/2,height/2,
    				handler,r, c, handler.getGalagaState().entityManager));
    		return;
    		
    	}else if (ocupancy[r][c]){
    		for(int i = 4; i > 1; i--) {
    			for (int j = 0; j< 8; j++) {
    				if (!ocupancy[i][j]) {
    					ocupancy[i][j] = true;
    					handler.getGalagaState().entityManager.entities.add(new EnemyBee(bounds.x, bounds.y,width/2,height/2,
    		    				handler,i, j, handler.getGalagaState().entityManager));
    					return;
    				}
    			}
    		}
    	}
    }
    
    	public void spawnEShip(int r, int c) {
        	if (!ocupancy[r][c]) {
        		ocupancy[r][c] = true;
        		handler.getGalagaState().entityManager.entities.add(new EnemyShip(bounds.x, bounds.y,width/2 + 20,height/2 + 25,
        				handler,r, c, handler.getGalagaState().entityManager));
        		return;
        		
        	}else if (ocupancy[r][c]){
        		for(int i = 0; i <= 1; i++) {
        			for (int j = 0; j< 8; j++) {
        				if (!ocupancy[i][j]) {
        					ocupancy[i][j] = true;
        					handler.getGalagaState().entityManager.entities.add(new EnemyShip(bounds.x, bounds.y,width/2 + 20,height/2 + 25,
        		    				handler,i, j, handler.getGalagaState().entityManager));
        					return;
        				}
        			}
        		}
        	}
    }
    public void initialSpawn() {
    	for (int i = 0; i < 5; i++) {
    		for(int j = 0; j< 8; j++) {
    			// Spawn Bee
    			ocupancy[i][j] = true;
    			if(i >= 2) {
    				handler.getGalagaState().entityManager.entities.add(new EnemyBee(bounds.x, bounds.y,width/2,height/2,
		    				handler,i, j, handler.getGalagaState().entityManager));
    			}// Spawn EnemyShip
    			else {
    				handler.getGalagaState().entityManager.entities.add(new EnemyShip(bounds.x, bounds.y,width/2 + 20,height/2 + 25,
		    				handler,i, j, handler.getGalagaState().entityManager));
    			}
    		}
    	}
    }
    public void subsequentSpawn(int ro, int co) {
    	if(ro >= 2) {
    		spawnBee( ro, co);
    	}else {
    		spawnEShip(ro, co);
    	}
    }
    

    /*Resets the boolean list to false, then grabs the entities arraylist from entity manager
     * and finally gets the positions of all the alive entities and marks them true.
     * If all spaces are taken, the whole list will be true.
     * I asked for some help to make this method; I adapted it for my purpose
     * ** Originally written by Ricardo Garcia
     */ 
    public void checkAvaliablePos() {
    	ocupancy = new boolean[5][8];
		ArrayList<BaseEntity> myEntities = handler.getGalagaState().entityManager.getEntities();
		
		int myRow = 0;
		int myCol = 0;
		BaseEntity myBaseEntity;
		EnemyBee myEnemyBee;
		EnemyShip myEnemyShip;
		
		for (int i=0; i < myEntities.size(); i++) {
			myBaseEntity = myEntities.get(i);
			if (myBaseEntity instanceof EnemyBee) {
				myEnemyBee = (EnemyBee)myBaseEntity;
				myRow = myEnemyBee.row;
				myCol = myEnemyBee.col;
				ocupancy[myRow][myCol] = true;
			}else if(myBaseEntity instanceof EnemyShip) {
				myEnemyShip = (EnemyShip)myBaseEntity;
				myRow = myEnemyShip.row;
				myCol = myEnemyShip.col;
				ocupancy[myRow][myCol] = true;
		    }
	    }
    }
    
    // Runs a loop, if it finds a false position, returns true, otherwise, returns false.
    public boolean enemyHasDied() {
    	for (int i = 4; i >= 0 ; i--) {
    		for(int j = 0; j < 8; j++) {
    			if(!ocupancy[i][j]) {
    				return true;
    			}
    		}
    	}return false;
    }
}