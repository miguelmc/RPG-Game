package game.entities;

import game.entities.item.EquipItem;
import game.entities.item.EtcItem;
import game.entities.item.UsableItem;
import game.entities.superentities.Monster;
import game.structure.GameObject;
import game.structure.MapManager;
import game.structure.Slot;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Util;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

/**
 * An entity is any game object that can be in a map such as the player, NPCs,
 * monsters, objects, etc.
 */
public abstract class Entity extends GameObject
{
	private Dimension renderSize = new Dimension(1, 1);
	private Point offset = new Point();
	private boolean strong = false;
	private Point position;
	private boolean invisible = false;

	public Entity(int id)
	{
		super(id);
	}

	public static Entity createEntity(int id)
	{
		// static factory
		EntityTypes type = EntityTypes.getType(id);

		switch (type)
		{
		case Tile:
			return new Tile(id);
		case Portal:
			return new Portal(id);
		case EquipItem:
			return new EquipItem(id);
		case UseItem:
			return new UsableItem(id);
		case EtcItem:
			return new EtcItem(id);
		case Monster:
			return new Monster(id);
		case NPC:
			return new NPC(id);
		case Object:
			return new Object(id);
		}

		throw new IllegalArgumentException("The ID does not match an entity type.");

	}

	public void render()
	{
		if (!isInvisible())
			Renderer.render(new Builder(
					getTexture(),
					new Point(Util.pointArithmetic(Slot.SIZE, getOffset(), getPositionInGrid())),
					new Dimension(getRenderSize().getWidth()*Slot.SIZE, getRenderSize().getHeight()*Slot.SIZE)) );
	}

	public void midRender(){}
	
	public void UIRender(){}

	public void update(){} //optionally implemented by subclasses

	public Point position()
	{
		return position;
	}

	public String path()
	{
		return EntityTypes.getType(id()).path() + "/" + hexID() + "/";
	}
	
	public int getX()
	{
		return position().getX();
	}

	public int getY()
	{
		return position().getY();
	}

	public void moveTo(Point pos)
	{
		if (position() != null)
			getMap().get(position()).remove(this);
		getMap().get(pos).add(this);
		
		position = new Point(pos); // save a reference to the position
	}

	public Point getPositionInGrid()
	{
		return new Point(getX() - getMap().getOffSet().getX(), getY() - getMap().getOffSet().getY());
	}

	protected void setStrong()
	{
		strong = true;
	}

	public boolean isStrong()
	{
		return strong;
	}

	public Texture getTexture()
	{
		if(MapManager.getMap().getTextureManager().get(id()) == null && !(this instanceof Object.Block))
			MapManager.getMap().getTextureManager().add(id());
		
		return MapManager.getMap().getTextureManager().get(id());
	}
	
	protected Point getOffset()
	{
		return offset;
	}

	public void setOffset(int x, int y)
	{
		offset.setLocation(x, y);
	}

	public void move(Point move)
	{
		moveTo(new Point(getX() + move.getX(), getY() + move.getY()));
	}

	public void modifyPos(Point pos)
	{
		position = pos;
	}

	public boolean isInvisible()
	{
		return invisible;
	}
	
	protected void setInvisible(boolean invisible)
	{
		this.invisible = invisible;
	}

	protected void setRenderSize(int width, int height) {
		renderSize.setSize(width, height);
	}
	
	public Dimension getRenderSize()
	{
		return new Dimension(renderSize);
	}
	
}
