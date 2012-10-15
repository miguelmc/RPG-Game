package game.structure;

import game.entities.superentities.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.util.Point;


public class MapManager{

	private Set<Map> maps = new HashSet<Map>();
	private Map currentMap;
	
	public MapManager(Map... maps) {
		this.maps.addAll(Arrays.asList(maps));
	}

	public void setMap(int id, Point playerPos){
		
		
		Player player;
		Point spawnPoint = playerPos;
		
		if(getCurrentMap() == null){
			player = new Player(Integer.parseInt("2600",16), spawnPoint);
		}else{
			player = getCurrentMap().getPlayer();
			getCurrentMap().removePlayer();
			getCurrentMap().resetCamera();
		}
				
		for(Map map: maps){
			if(map.id() == id){
				currentMap = map;
			}
		}
		
		currentMap.add(player, spawnPoint);
				
	}
	
	public void input(){
		currentMap.input();
	}
	
	public void update(){
		currentMap.update();
	}
	
	public void render(){
		currentMap.render();
	}
	
	public Map getCurrentMap(){
		return currentMap;
	}
	
}
