package game.features;

public enum Stat
{

	MAXHP(0x00, "maxHP"), MAXMP(0x01, "maxMP"), ATK(0x02, "atk"), STR(0x03, "str"), DEF(0x04, "def");

	public final int ID;
	public final String NAME;

	private Stat(int id, String name)
	{
		ID = id;
		NAME = name;
	}

	public Stat getStat(int id)
	{
		for (Stat s : Stat.values())
			if (s.ID == id)
				return s;
		return null;
	}

}