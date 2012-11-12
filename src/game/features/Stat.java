package game.features;

public enum Stat
{

	MAXHP(1), MAXMP(2), ATK(3), STR(4), DEF(5);

	public final int ID;

	private Stat(int id)
	{
		ID = id;
	}

	public Stat getStat(int id)
	{
		for (Stat s : Stat.values())
			if (s.ID == id)
				return s;
		return null;
	}
	
	public String toString()
	{
		String name = super.toString().toLowerCase().substring(1);
		return super.toString().charAt(0) + name;
	}

}