package game.entities.item;

import game.entities.Entity;
import game.features.Stat;
import game.ui.UserInterface;
import game.util.XMLParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.lwjgl.util.Point;

/**
 * An item that can be equipped by the player to raise stats.
 */
public class EquipItem extends Item
{

	private java.util.Map<Stat, Integer> stats;
	private EquipType type;
	private int upgradesAvailable;
	
	private static java.util.Map<EquipType, Integer> maxUpgrades;
	
	static
	{
		maxUpgrades = new HashMap<EquipType, Integer>();
		maxUpgrades.put(EquipType.TOPWEAR, 3);
		maxUpgrades.put(EquipType.BOTTOMWEAR, 3);
		maxUpgrades.put(EquipType.HELMET, 3);
		maxUpgrades.put(EquipType.SHOES, 2);
		maxUpgrades.put(EquipType.WEAPON, 5);
		maxUpgrades.put(EquipType.SHIELD, 2);
	}
 
	public EquipItem(int id, Point pos)
	{
		super(id, pos);
		
		XMLParser parser = new XMLParser(path() + "data.xml");
		
		stats = new HashMap<Stat, Integer>();
		type = EquipType.getType(parser.getAttribute("EquipItem", "type"));
		
		Map<String, String> statAttributes = parser.getAttributes("EquipItem/stats");

		for(Stat stat: Stat.values())
		{
			String value = statAttributes.get(stat.name().toLowerCase());
			
			if(value != null) 
				stats.put(stat, Integer.parseInt(value));
		}
		
		upgradesAvailable = maxUpgrades.get(EquipType.TOPWEAR);
	}

	public EquipItem(int id)
	{
		this(id, null);
	}

	public EquipType getType()
	{
		return type;
	}
	
	public int getStat(Stat stat)
	{
		return stats.get(stat) != null ? stats.get(stat) : 0;
	}
	
	protected Map<Stat, Integer> getStats(){
		return stats;
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof EquipItem))
			return false;
		return stats.equals(((EquipItem)obj).getStats()) && id() == ((Entity)obj).id();
	}

	public int hashCode()
	{
		return id() + 99999 + stats.hashCode();
	}
	
	public boolean upgrade(Stat stat, int amount)
	{
		if(getUpgradesAvailable() == 0)
		{
			UserInterface.sendNotification("Your " + getType() + " do not have any upgrades available.");
			return false;
		}
		
		double stdDev = amount*.2;
		double randomizedAmount = (new Random().nextGaussian()*stdDev + amount);
		
		if(randomizedAmount < 0) randomizedAmount = 0;
		
		amount = (int)Math.round(randomizedAmount);
		
		upgradesAvailable--;
		stats.put(stat, stats.get(stat) + amount);
		
		UserInterface.sendNotification("Successfully upgraded " + getType() + ": +" + amount);
		
		return true;
	}
	
	public int getUpgradesAvailable()
	{
		return upgradesAvailable;
	}

	public enum EquipType
	{	
		TOPWEAR(), BOTTOMWEAR(), HELMET(), SHOES(), WEAPON(), SHIELD();

		private EquipType() {}

		public static EquipType getType(String name)
		{
			for (EquipType type : EquipType.values())
				if (type.toString().equalsIgnoreCase(name))
					return type;

			return null;
		}
	}
	
	public void setQuantity(int quanitity) {}
	
}
