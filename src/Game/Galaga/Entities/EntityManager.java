package Game.Galaga.Entities;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by AlexVR on 1/25/2020
 */
public class EntityManager {

    public PlayerShip playerShip;
//    public EnemyShip enemyShip;
    public ArrayList<BaseEntity> enemyentities;
    public ArrayList<BaseEntity> entities;
    
    public EntityManager(PlayerShip playerShip) {
        entities = new ArrayList<>();
    	enemyentities= new ArrayList<>();
        this.playerShip = playerShip;
        
    }
          
    public void tick(){
        playerShip.tick();
        
    	ArrayList<BaseEntity> toRemove = new ArrayList<>();
//    	ArrayList<BaseEntity> toAdd= new ArrayList<>();
        
//    	for(BaseEntity entity: enemyentities) {
//    		toAdd.add(entity);
//    		continue;
//    		}
    	entities.addAll(enemyentities);
    	enemyentities.clear();	
    	
    	for (BaseEntity entity: entities){
            if (entity.remove){
            	toRemove.add(entity);
                continue;
            }
            entity.tick();
            if (entity.bounds.intersects(playerShip.bounds)){
                playerShip.damage(entity);
         
            }
        }
        for (BaseEntity toErase:toRemove){
            entities.remove(toErase);
        }
               
    }

    public void render(Graphics g){
        for (BaseEntity entity: entities){
            entity.render(g);
        }
        playerShip.render(g);

    }
    public ArrayList<BaseEntity> getEntities(){
    	return entities;
    }

}