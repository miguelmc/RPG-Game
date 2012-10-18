package game.entities;

public enum EntityType
{

	Tile("tile", 0x2100), Portal("portal", 0x2200), EtcItem("item/etc", 0x2300), UseItem("item/use", 0x6300), EquipItem(
			"item/equip", 0xA300), Monster("monster", 0x2400), NPC("npc", 0x2500), Player("player", 0x2600), Object(
			"object", 0x2700);

	private String name;
	private int id;

	EntityType(String name, int id)
	{
		this.name = name;
		this.id = id;
	}

	public String toString()
	{
		return name;
	}

	public int id()
	{
		return id;
	}

	public static EntityType getType(int id)
	{

		int typeID = id & 0xFF00;

		for (EntityType type : EntityType.values())
		{
			if (type.id() == typeID)
				return type;
		}

		throw new IllegalArgumentException("The ID: " + id + " does not match an entity type.");

	}
}
