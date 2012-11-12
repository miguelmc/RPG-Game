package game.entities;

public enum EntityTypes
{

	Tile("tile", 0x2100), Portal("portal", 0x2200), EtcItem("item/etc", 0x2300), UseItem("item/use", 0x6300), EquipItem(
			"item/equip", 0xA300), Monster("monster", 0x2400), NPC("npc", 0x2500), Player("player", 0x2600), Object(
			"object", 0x2700);

	private String path;
	private int id;

	EntityTypes(String name, int id)
	{
		this.path = name;
		this.id = id;
	}

	public String path()
	{
		return path;
	}

	public int id()
	{
		return id;
	}

	public static EntityTypes getType(int id)
	{

		int typeID = id & 0xFF00;

		for (EntityTypes type : EntityTypes.values())
			if (type.id() == typeID)
				return type;

		throw new IllegalArgumentException("The ID: " + id + " does not match an entity type.");

	}
}
