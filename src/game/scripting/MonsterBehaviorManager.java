package game.scripting;

import game.entities.superentities.Monster;
import game.entities.superentities.SuperEntity;
import game.structure.MapManager;
import game.util.Util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Point;

public class MonsterBehaviorManager {

	Monster monster;
	
	public MonsterBehaviorManager(Monster monster)
	{
		this.monster = monster;
	}
	
	public boolean isPlayerInFront(int maxRange)
	{
		Point front = Util.addRelPoints(monster.position(), new Point(0, 1), monster.getFacingDir());
		return MapManager.getMap().getPlayer().position().equals(front);
	}
	
	public boolean isAngry()
	{
		return monster.isAngry();
	}
	
	public void moveRandom()
	{
		List<Integer> nums = new ArrayList<Integer>();
		nums.add(SuperEntity.UP);
		nums.add(SuperEntity.RIGHT);
		nums.add(SuperEntity.DOWN);
		nums.add(SuperEntity.LEFT);
		monster.moveRandom(nums);
	}
	
	public void moveToPlayer()
	{
		
	}
	
	public void attack(int skillID)
	{
		monster.attack(skillID);
	}
	
}
