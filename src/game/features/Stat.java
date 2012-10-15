package game.features;

public enum Stat{
	
	MAXHP(0x00, "MaxHP"), MAXMP(0x01, "Max MP"), ATK(0x02, "Attack"), STR(0x03, "Strength");
	
	public final int ID;
	public final String NAME;
	
	private Stat(int id, String name){
		ID = id;
		NAME = name;
	}
	
	public Stat getStat(int id){
		for(Stat s: Stat.values())
			if(s.ID == id)
				return s;
		return null;
	}

}