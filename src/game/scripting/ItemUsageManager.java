package game.scripting;

import game.entities.item.EquipItem;
import game.entities.item.EquipItem.EquipType;
import game.features.Stat;
import game.structure.MapManager;

/**
 * The object passed to the usable items scripts. This object's public methods
 * can be used in the script.
 */
public class ItemUsageManager extends AbstractScriptManager
{
	public static final int TOPWEAR = 0, BOTTOMWEAR = 1, HELMET = 2, SHOES = 3, WEAPON = 4, SHIELD = 5;
	
	public boolean upgradeEquip(int equipType, Stat stat, int amount)
	{
		EquipItem equip = MapManager.getMap().getPlayer().getEquips().get(EquipType.values()[equipType]);
		if(equip == null)
			return false;
				
		return equip.upgrade(stat, amount);	
	}
	
}
