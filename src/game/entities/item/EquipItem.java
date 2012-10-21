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
	}

	public EquipItem(int id)
	{
		this(id, null);
	}

	@Override
	protected void parseItem(XMLParser parser)
	{
		super.parseItem(parser);

		stats = new HashMap<Stat, Integer>();

		type = EquipType.getType(parser.getAttribute("EquipItem", "type"));

		Map<String, String> statAttributes = parser.getAttributes("EquipItem/stats");

		stats.put(Stat.MAXHP, Integer.parseInt(statAttributes.get("maxHP")));
		stats.put(Stat.MAXMP, Integer.parseInt(statAttributes.get("maxMP")));
		stats.put(Stat.ATK, Integer.parseInt(statAttributes.get("atk")));
		stats.put(Stat.STR, Integer.parseInt(statAttributes.get("str")));
		stats.put(Stat.DEF, Integer.parseInt(statAttributes.get("def")));

	}

	/**
	 * 
	 * <br>
	 * <b>getType</b>
	 * <br>
	 * <p>
	 * <tt>public EquipType getType()</tt>
	 * </p>
	 * There are several type of equip items, such as weapons, topwear, bottomwear, helmet, shoes, etc.
	 * <br><br>
	 */
	public EquipType getType()
	{
		return type;
	}

	/**
	 * 
	 * <br>
	 * <b>getStat</b>
	 * <br>
	 * <p>
	 * <tt>public int getStat(Stat stat)</tt>
	 * </p>
	 * Returns the value of certain stat of the equip item.
	 * <br><br>
	 * @see com.game.features.Stat
	 */
	public int getStat(Stat stat)
	{
		return stats.get(stat);
	}
	
	/**
	 * 
	 * <br>
	 * <b>getStats</b>
	 * <br>
	 * <p>
	 * <tt>protected Map<Stat, Integer> getStats()</tt>
	 * </p>
	 * Returns a map of all stats and their values.
	 * <br><br>
	 */
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

	/**
	 * The type of equip item. The player can wear one of each type of equip.
	 */
	public enum EquipType
	{
		
		TOPWEAR("topwear"), BOTTOMWEAR("bottomwear"), HELMET("helmet"), SHOES("shoes"), WEAPON("weapon"), SHIELD("shield");

		private String name;

		private EquipType(String name)
		{
			this.name = name;
		}

		public static EquipType getType(String name)
		{

			for (EquipType type : EquipType.values())
			{
				if (type.toString().equals(name))
					return type;
			}

			return null;

		}

		public String toString()
		{
			return name;
		}

	}

}
