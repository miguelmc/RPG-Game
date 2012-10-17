package game.entities.superentities;

import game.structure.MapManager;

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
		if(MapManager.getMap().get(position).getStrongEntity()==null){
			Monster monster = new Monster(id);
			monster.setPosition(position);
			currentMonster = monster;
			spawn = false;
		}else{
			deadTime += 1000; //retry in 1 second
		}
	}
	
}