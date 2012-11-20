package game.features;


public enum Stat
{

	MAXHP(1, "MaxHP"), MAXMP(2, "MaxMP"), ATK(3, "Atk"), STR(4, "Str"), DEF(5, "Def");

	public final int ID;
	public String name;

	private Stat(int id, String name)
	{
		ID = id;
		this.name = name;
	}

	public static Stat getStat(int id)
	{
		for (Stat s : Stat.values())
			if (s.ID == id)
				return s;
		return null;
	}
	
	public String toString()
	{
		return name;
	}

}