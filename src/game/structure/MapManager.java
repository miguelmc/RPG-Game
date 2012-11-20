package game.structure;

import game.entities.superentities.Player;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.util.Point;

/**
 * Static class used to access and set the current map. Updates, renders and
 * gives input events to the current map.
 */
public class MapManager
{

	private static Set<Map> maps = new HashSet<Map>();
	private static Map currentMap;
	private static final int startMap = 0;

	static {
		// TODO read and create all maps and set current map based on the game config file
		maps.add(new Map(0));
		maps.add(new Map(1));
		maps.add(new Map(2));
		maps.add(new Map(3));
		setMap(startMap, new Point(4, 6));

	}
	
	public static void setMap(int id, Point playerPos)
	{
		// TODO each map should have a spawn point

		Player player;
		Point spawnPoint = playerPos;

		if (currentMap == null) // if this is the starting map, create player
		{
			player = new Player(spawnPoint);
		} else // else retrieve player from previous map
		{
			player = currentMap.getPlayer();
			player.stopAllActions();
			currentMap.removePlayer();
			currentMap.resetCamera();
		}

		for (Map map : maps)
			if (map.id() == id)
				currentMap = map;

		currentMap.add(player, spawnPoint);
	}

	
	
	public static void input()
	{
		currentMap.input();
	}

	public static void update()
	{
		currentMap.update();
	}

	public static void render()
	{
		currentMap.render();
	}

	public static Map getMap()
	{
		return currentMap;
	}

}
