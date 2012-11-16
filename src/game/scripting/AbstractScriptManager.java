package game.scripting;

import game.entities.Entity;
import game.entities.EntityTypes;
import game.entities.item.Item;
import game.entities.superentities.Player;
import game.features.Stat;
import game.structure.MapManager;

import org.lwjgl.util.Point;

/**
 * The abstract type of the object passed to the scripts. Contains basic
 * functions that are usually used in the script.
 */
public abstract class AbstractScriptManager
{

	public void setHP(int hp)
	{
		getPlayer().setHP(hp);
	}

	public int getHP()
	{
		return getPlayer().getHP();
	}

	public int getMaxHP()
	{
		return getPlayer().getStat(Player.TOTAL + Stat.MAXHP.ID);
	}

	public void setMP(int mp)
	{
		getPlayer().setMP(mp);
	}

	public int getMP()
	{
		return getPlayer().getMP();
	}

	public int getMaxMP()
	{
		return getPlayer().getStat(Player.TOTAL + Stat.MAXMP.ID);
	}

	public void setMap(int id, Point p)
	{
		MapManager.setMap(id, p);
	}

	public int getMapID()
	{
		return MapManager.getMap().id();
	}

	public String getMapName()
	{
		return MapManager.getMap().getName();
	}

	public void gainGold(int amount)
	{
		getPlayer().gainGold(amount);
	}
	
	public boolean gainItem(int id, int amount)
	{
		id = Integer.parseInt(Integer.toString(id), 16);
		
		assert EntityTypes.getType(id) == EntityTypes.EtcItem ||
			   EntityTypes.getType(id) == EntityTypes.UseItem ||
			   EntityTypes.getType(id) == EntityTypes.EquipItem;
		
		Item item = (Item) Entity.createEntity(id);
		
		item.setQuantity(amount);
		
		return getPlayer().gainItem(item);
	}
	
	protected Player getPlayer()
	{
		return MapManager.getMap().getPlayer();
	}

}
