package game.entities.item;

/**
 * An item which cannot be used or equipped. It may be required for a quest or sold in a shop.
 */
public class EtcItem extends Item
{

	public EtcItem(int id)
	{
		super(id);
	}

	public EtcItem(int id, int amount)
	{
		super(id, amount);
	}

}
