package game.structure;

public abstract class GameObject
{

	private final int ID;
	private String hexID;

	public GameObject(int id)
	{
		ID = id;

		hexID = Integer.toHexString(id());
		while (hexID.length() != 4)
		{
			hexID = "0" + hexID;
		}

	}

	public String toString()
	{
		return Integer.toString(ID);
	}

	public boolean equals(Object obj)
	{
		return obj instanceof GameObject ? ((GameObject) obj).id() == id() : false;
	}

	public int hashCode()
	{
		return id();
	}

	public int id()
	{
		return ID;
	}

	public String hexID()
	{
		return hexID;
	}

	public static Map getMap()
	{
		return MapManager.getMap();
	}

	public boolean isEntity()
	{
		return (id() & 0x2000) == 0x2000;
	}

}
