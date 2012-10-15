package game.entities.superentities;

import game.Main;

import org.lwjgl.util.Point;

public class Spawner{
	
	private final Point position;
	private int id;
	private int respawnTime;
	private long deadTime;
	private boolean spawn = false;
	private Monster currentMonster;
	
	public Spawner(Monster monster, int respawnTime){
		this.position = monster.position();
		id = monster.id();
		currentMonster = monster;
		this.respawnTime = respawnTime;
	}
	
	public void update(){
		if(currentMonster.isDead() && !spawn){
			deadTime = System.currentTimeMillis();
			spawn = true;
		}
		
		if(System.currentTimeMillis() > deadTime + respawnTime && spawn){
			spawn();
		}
	}
	
	private void spawn(){
		if(Main.getMapManager().getCurrentMap().getStrongEntitiesAt(position).isEmpty()){
			Monster monster = new Monster(id);
			monster.setPosition(position);
			currentMonster = monster;
			spawn = false;
		}else{
			deadTime += 1000; //retry in 1 second
		}
	}
	
}