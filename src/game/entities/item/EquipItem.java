package game.entities.item;

import game.entities.Entity;
import game.features.Stat;
import game.util.XMLParser;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.Point;

/**
 * An item that can be equipped by the player to raise stats.
 */
public class EquipItem extends Item
{

	private java.util.Map<Stat, Integer> stats;
	private EquipType type;

	public EquipItem(int id, Point pos)
	{
		super(id, pos);
		
		XMLParser parser = new XMLParser(path() + "data.xml");
		
		stats = new HashMap<Stat, Integer>();
		type = EquipType.getType(parser.getAttribute("EquipItem", "type"));
		
		Map<String, String> statAttributes = parser.getAttributes("EquipItem/stats");

		for(Stat stat: Stat.values())
		{
			String value = statAttributes.get(stat.NAME);
			
			if(value != null)
				stats.put(stat, Integer.parseInt(value));
		}
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

	public enum EquipType
	{
		
		TOPWEAR(), BOTTOMWEAR(), HELMET(), SHOES(), WEAPON(), SHIELD();

		private EquipType() {}

		public static EquipType getType(String name)
		{
			for (EquipType type : EquipType.values())
				if (type.toString().equals(name))
					return type;

			return null;
		}

	}

}
