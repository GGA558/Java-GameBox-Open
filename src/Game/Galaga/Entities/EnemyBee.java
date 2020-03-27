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
    boolean justSpawned=true,attacking=false, positioned=false,hit=false,centered = false, noAddScore = false, 
    		hasNotSpun = true, initializedAttackPattern = false;
    Animation idle,turn90Left;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX,formationY,speed,centerCoolDown=60;
    private int timeAlive=0, spinPattern = -1, attackPattern1 =0;
    int BeeScore=100;
	int TimePos=60*random.nextInt(12), spinCounter = 12;
  
    
    public EnemyBee(int x, int y, int width, int height, Handler handler,int row, int col, EntityManager enemies) {
        super(x, y, width, height, Images.galagaEnemyBee[0], handler);
        
//        row=random.nextInt(2)+2;
//        col=random.nextInt(8);
        this.row = row;
        this.col = col;
        attackPattern1 = random.nextInt(2);
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
			
			if (hasNotSpun) {
				y+=speed;
				if ( (x > handler.getWidth()/2 || attackPattern1 == 0) && attackPattern1 != 1) {
					x-=speed;
					attackPattern1 = 0;
					if (x < handler.getWidth()/4 + 10) {
						attackPattern1 = 1;
					}
				}
				else {
					x+=speed;
					if (x > handler.getWidth() * 3 / 4 - 63) {
						attackPattern1 = 0;
					}
				}

				initializedAttackPattern = true;
			}else {// Spin Sequence
				switch (spinPattern) {
				case 0:
					x += speed;
					spinCounter--;
					if (spinCounter <= 0) {
						spinPattern = 1;
						spinCounter = 6;
					}
					break;
				case 1:
					x += speed/2;
					y -= speed/2;
					spinCounter--;
					if (spinCounter <= 0) {
						spinPattern = 2;
						spinCounter = 6;
					}
					break;
				case 2://
					y -= speed;
					spinCounter--;
					if (spinCounter <= 0) {
						spinPattern = 3;
						spinCounter = 6;
					}
					break;
				case 3:
					y -= speed/2;
					x -= speed/2;
					spinCounter--;
					if (spinCounter <= 0) {
						spinPattern = 4;
						spinCounter = 6;
					}
					break;
				case 4:
					x -= speed;
					spinCounter--;
					if (spinCounter <= 0) {
						spinPattern = 5;
						spinCounter = 6;
					}
					break;
				case 5:
					x -= speed/2;
					y += speed/2;
					spinCounter--;
					if (spinCounter <= 0) {
						spinPattern = 6;
						spinCounter = 6;
					}
					break;
				case 6:
					y += speed;
					break;
				}
			}
			if ((this.x >= handler.getGalagaState().entityManager.playerShip.x - 10
					&& this.x <= handler.getGalagaState().entityManager.playerShip.x + 10) && hasNotSpun) {// Initializes the spin sequence.
				spinPattern = 0;
				hasNotSpun = false;
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