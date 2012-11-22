package game.scripting;

import game.entities.Portal;
import game.structure.MapManager;

import org.lwjgl.util.Point;

/**
 * The object passed to the portal scripts. This object's public methods can be
 * used in the script.
 */
public class PortalActionManager extends AbstractScriptManager
{
	public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	private Portal portal;

	public int playerFacingDir()
	{
		return getPlayer().getFacingDir();
	}

	public void setPosition(int x, int y)
	{
		getPlayer().moveTo(new Point(x, y));
	}

	public void setPosition(Point pos)
	{
		setPosition(pos.getX(), pos.getY());
	}

	public Portal getPortal()
	{
		return portal;
	}

	public int getX()
	{
		return getPortal().getX();
	}

	public int getY()
	{
		return getPortal().getX();
	}

	public void setMap(int id)
	{
		super.setMap(id, MapManager.getMap(id).getPortalByID(getID()).position());
	}
	
	public void setMap(int id, Point position)
	{
		super.setMap(id, position);
	}

	public void setPortal(Portal portal)
	{
		this.portal = portal;
	}

	public int getID()
	{
		return getPortal().id();
	}

}
