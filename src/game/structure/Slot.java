package game.structure;

import game.entities.Entity;
import game.entities.item.Bundle;
import game.entities.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Slot {

	public static int SIZE = 32;
	public static final int TILE = 0, PORTAL = 1, ITEMS = 2, MONSTER = 3, NPC = 4, PLAYER = 5, OBJECT = 6;
	
	private Entity entities[] = new Entity[7];
	
	public Slot() {
		entities[ITEMS] = new Bundle(new ArrayList<Item>());
	}
	
	public Entity get(int type) {
		return entities[type];
	}
	
	public List<Entity> getAll() {
		List<Entity> all = new ArrayList<Entity>(Arrays.asList(entities));
		all.removeAll(Collections.singleton(null));
		return all;
	}
	
	public void remove(Entity e) {
		entities[getType(e)] = null;
	}

	public void remove(int type) {
		entities[type] = null;
	}
	
	public void set(Entity entity) {
		//TODO validate type
		entities[getType(entity)] = entity;
	}

	public void update(){
		for(Entity e: entities){
			if(e != null)
				e.update();
		}
	}
		
	private int getType(Entity e) {
		return ((e.id() & 0x0F00) >> 8) - 1;
	}
	
}
