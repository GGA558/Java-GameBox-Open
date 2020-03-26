package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;
import Resources.ScoreManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


public class EnemyBee extends BaseEntity {
	int row,col;//row 3-4, col 0-7
    boolean justSpawned=true,attacking=false, positioned=false,hit=false,centered = false, noAddScore = false, hasNotSpun = true;
    Animation idle,turn90Left;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX,formationY,speed,centerCoolDown=60;
    int timeAlive=0, attackPattern2 = 10;
    int BeeScore=100;
	int TimePos=60*random.nextInt(12), spinCounter = 12;
	int[] attackPattern = new int[2];
  
    
    public EnemyBee(int x, int y, int width, int height, Handler handler,int row, int col, EntityManager enemies) {
        super(x, y, width, height, Images.galagaEnemyBee[0], handler);
        
//        row=random.nextInt(2)+2;
//        col=random.nextInt(8);
        this.row = row;
        this.col = col;
        attackPattern[0] = random.nextInt(2);
        spinCounter = 2;
        
        
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaEnemyBee[0];
        idleAnimList[1] = Images.galagaEnemyBee[1];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaEnemyBee);
        spawn();
        speed = 4;
        formationX=(handler.getWidth()/4)+(col*((handler.getWidth()/2)/8))+8;
        formationY=(row*(handler.getHeight()/10))+8;
    }

    public void spawn() {
        spawnPos = random.nextInt(3);
        switch (spawnPos){
            case 0://left
                x = (handler.getWidth()/4)-width;
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;
            case 1://top
                x = random.nextInt((handler.getWidth()-handler.getWidth()/2))+handler.getWidth()/4;
                y = -height;
                break;
            case 2://right
                x = (handler.getWidth()/2)+ width + (handler.getWidth()/4);
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;
//            case 3://down
//                x = random.nextInt((handler.getWidth()/2))+handler.getWidth()/4;
//                y = handler.getHeight()+height;
//                break;
        }
        bounds.x=x;
        bounds.y=y;
    }

    @Override
    public void tick() {
        super.tick();
        idle.tick();
        if (hit){
            if (enemyDeath.end){
            	if(!noAddScore) {
            	    handler.getScoreManager().addGalagaCurrentScore(BeeScore);
            	    if(handler.getScoreManager().getGalagaCurrentScore()>handler.getScoreManager().getGalagaHighScore()) {
            		    handler.getScoreManager().setGalagaHighScore(handler.getScoreManager().getGalagaCurrentScore());
            	    }  
            	}
            	remove = true;
                return;
                
            }

            enemyDeath.tick();
        }
        
            
        
        if (justSpawned){
            timeAlive++;
            if (!centered && Point.distance(x,y,handler.getWidth()/2,handler.getHeight()/2)>speed){//reach center of screen
                switch (spawnPos){
                    case 0://left
                        x+=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
                    case 1://top
                        y+=speed;
                        if (Point.distance(x,y,handler.getWidth()/2,y)>speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        break;
                    case 2://right
                        x-=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
//                    case 3://down
//                        y-=speed;
//                        if (Point.distance(x,y,handler.getWidth()/2,y)>speed) {
//                            if (x > handler.getWidth() / 2) {
//                                x -= speed;
//                            } else {
//                                x += speed;
//                            }
//                        }
//                        break;
                }
                if (timeAlive>=60*60*2){
                    //more than 2 minutes in this state then die
                    //60 ticks in a second, times 60 is a minute, times 2 is a minute
                    damage(new PlayerLaser(0,0,0,0,Images.galagaPlayerLaser,handler,handler.getGalagaState().entityManager));
                }

            }else {//move to formation
                if (!centered){
                    centered = true;
                    timeAlive = 0;
                }
                if (centerCoolDown<=0){
                    if (Point.distance(x, y, formationX, formationY) > speed) {//reach center of screen
                        if (Math.abs(y-formationY)>6) {
                            y -= speed;
                        }
                        if (Point.distance(x,y,formationX,y)>speed/2) {
                            if (x >formationX) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        
                    }else{
                        positioned =true;
                        justSpawned = false;
                    }
                }else{
                    centerCoolDown--;
                }
                if(!justSpawned && !positioned && !attacking) {
                	damage(new PlayerLaser(0,0,0,0,Images.galagaPlayerLaser,handler,handler.getGalagaState().entityManager));
                
                }
                if (timeAlive>=60*60*2){
                    //more than 2 minutes in this state then die
                    //60 ticks in a second, times 60 is a minute, times 2 is a minute
                    damage(new PlayerLaser(0,0,0,0,Images.galagaPlayerLaser,handler,handler.getGalagaState().entityManager));
                }
            }
        }else if (positioned){
        	if(TimePos<=0) {
        		attacking=true;
        		positioned=false;
        	} else {
        		TimePos--;
        	}
        	
        	
		} else if (attacking) {

			if ((this.x >= handler.getGalagaState().entityManager.playerShip.x - 10
					&& this.x <= handler.getGalagaState().entityManager.playerShip.x + 10) && hasNotSpun) {// Initializes the spin sequence.
				attackPattern2 = 0;
				hasNotSpun = false;
			} else if (x <= handler.getWidth() / 4 + 2 || attackPattern[1] == 2) {// move diagonally right
				attackPattern[1] = 2;
				y += speed;
				x += speed / 6 * 5;
				if (x == (handler.getWidth() - handler.getWidth() / 4 - 63)) {// Avoid getting out of the screen (sides)
					attackPattern[1] = 1;
				}
			} else if (x >= (handler.getWidth() - handler.getWidth() / 4 - 63) || attackPattern[1] == 1) {// move diagonally left
				attackPattern[1] = 1;
				y += speed;
				x -= speed / 6 * 5;
				if (x == handler.getWidth() / 4) {// Avoid getting out of the screen (sides)
					attackPattern[1] = 2;
				}
			} else {// Spin Sequence
				switch (attackPattern2) {
				case 0:
					x += speed;
//        			attackPattern2 = 1;
					spinCounter--;
					if (spinCounter <= 0) {
						attackPattern2 = 1;
						spinCounter = 12;
					}
					break;
				case 1:
					y -= speed;
//        			attackPattern2 = 2;
					spinCounter--;
					if (spinCounter <= 0) {
						attackPattern2 = 2;
						spinCounter = 12;
					}
					break;
				case 2:
					x -= speed;
//        			attackPattern2 = 3;
					spinCounter--;
					if (spinCounter <= 0) {
						attackPattern2 = 3;
						spinCounter = 12;
					}
					break;
				case 3:
					y += speed;
					break;
				}
				if (attackPattern2 == 10) {
					switch (attackPattern[0]) {
					case 0:
						y += speed;
						x += speed;
						break;
					case 1:
						y += speed;
						x -= speed;
						break;
					}
				}
			}
        	
       	
        	// kills enemy bee if out of screen.
        }if (centered || !centered) {
        	if(this.y > handler.getHeight()|| this.x < handler.getWidth()/5 || this.x > handler.getWidth()*4/5 ) {
        		noAddScore = true;
        		damage(new PlayerLaser(0,0,0,0,Images.galagaPlayerLaser,handler,handler.getGalagaState().entityManager));
        	}
        }
        
        bounds.x=x;
        bounds.y=y;  
        
            
    }

    @Override
    public void render(Graphics g) {
        ((Graphics2D)g).draw(new Rectangle(formationX,formationY,32,32));
        if (arena.contains(bounds)) {
            if (hit){
                g.drawImage(enemyDeath.getCurrentFrame(), x, y, width, height, null);
            }else{
                g.drawImage(idle.getCurrentFrame(), x, y, width, height, null);

            }
        }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        super.damage(damageSource);
        if (damageSource instanceof PlayerLaser){
            hit=true;
            if(!noAddScore) {
                handler.getMusicHandler().playEffect("explosion.wav");
            }
            damageSource.remove = true;
        }
    }
}