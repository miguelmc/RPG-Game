package game.entities;

public class Tile extends Entity
{

	private boolean blocked = false;

	public Tile(int id)
	{
		super(id);
	}

	public void setBlocked(boolean blocked)
	{
		this.blocked = blocked;
		setStrong();
	}

	public boolean isBlocked()
	{
		return blocked;
	}

}
