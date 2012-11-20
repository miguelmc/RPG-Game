package game.structure;

import game.Main;
import game.entities.Entity;
import game.entities.Object;
import game.entities.Object.Block;
import game.entities.Portal;
import game.entities.Tile;
import game.entities.superentities.Player;
import game.entities.superentities.Spawner;
import game.util.TextureManager;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Map extends GameObject
{

	private String NAME;
	private Point offset = new Point(0, 0); // for rendering purposes
	private Dimension size = new Dimension();
	private Player player;
	private Slot[][] matrix;
	private TextureManager textureManager = new TextureManager(); // holds all textures used by the map
	private List<Spawner> spawners = new ArrayList<Spawner>();

	public final static int VIEW_LIMIT = 7; // Tiles away from the side to move the camera

	public Map(int id)
	{
		this(id, null);
	}

	public Map(int id, TextureManager texManager)
	{
		super(id);
		parseMap();
		loadTextures(texManager);
	}

	private void loadTextures(TextureManager prevTexManager)
	{
		
	}

	private void parseMap()
	{
		XMLParser parser = new XMLParser("map/" + hexID() + ".xml");
		
		// Parse map info
		NAME = parser.getAttribute("Map", "name");
		size.setWidth(Integer.parseInt(parser.getAttribute("Map", "width")));
		size.setHeight(Integer.parseInt(parser.getAttribute("Map", "height")));
		
		// initialize matrix
		matrix = new Slot[size.getWidth()][size.getHeight()];
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[0].length; j++)
			{
				matrix[i][j] = new Slot();
			}
		}
		
		// Parse tiles
		Queue<Integer> tileQueue = new LinkedList<Integer>();

		List<java.util.Map<String, String>> tiles = parser.getChildrenAttributes("Map/Tiles");
		for (java.util.Map<String, String> data : tiles)
		{
			int id = Integer.parseInt(data.get("id"), 16);
			int amount = Integer.parseInt(data.get("amount"));
			for (int i = 0; i < amount; i++)
			{
				tileQueue.add(id);
			}
		}
		

		for (int i = 0; i < size.getHeight(); i++)
		{
			for (int j = 0; j < size.getWidth(); j++)
			{
				Tile tile = new Tile(tileQueue.poll());
				add(tile, new Point(j, i));
			}
		}
		
		// Parse all other entities
		String xmlElements[] = { "Portals", "Monsters", "NPCs", "Objects" };

		for (String xmlElement : xmlElements)
		{
			List<java.util.Map<String, String>> entities = parser.getChildrenAttributes("Map/" + xmlElement);
			for (java.util.Map<String, String> data : entities)
			{
				Point position = new Point(Integer.parseInt(data.get("x")), Integer.parseInt(data.get("y")));
				add(Entity.createEntity(Integer.parseInt(data.get("id"), 16)), position);
				if (xmlElement.equals("Monsters"))
					spawners.add(new Spawner(get(position).getMonster(), 10000));
			}
		}
		
	}

	public void input()
	{
		getPlayer().input();
	}

	public void update()
	{
		for (Slot s : getAllSlots())
		{
			s.update();
		}

		for (Spawner spawner : spawners)
		{
			spawner.update();
		}

	}

	public void render()
	{
		// Do not render slot by slot, but by entity type (i.e. first all tiles,
		// then all items, etc)
		for (int j = 0; j < 3; j++)
		{
			for (int i = 0; i < 5; i++)
			{
				for (Slot s : getAllSlots())
					s.render(i, j);
			}
		}
	}

	public void add(Entity entity, Point pos)
	{

		entity.modifyPos(new Point(pos));
		get(pos).add(entity);

		if (entity instanceof Object && !entity.isStrong())
		{
			for (Block block : ((Object) entity).getBlocks())
			{
				add(block, block.position());
			}
		}

		if (entity instanceof Player)
		{
			player = (Player) entity; // save a reference to the player
			centerView(); // center the view to the player
		}

	}

	public void moveView(int horizontal, int vertical)
	{
		if (getSize().getWidth() > Main.GRIDSIZE.getWidth())
			offset.setX(offset.getX() + horizontal);
		if (getSize().getHeight() > Main.GRIDSIZE.getHeight())
			offset.setY(offset.getY() + vertical);
	}

	public boolean isPlayerAt(Point pos)
	{
		return player.position().equals(pos);
	}

	public void removePlayer()
	{
		if (hasPlayer())
		{
			get(getPlayer().position()).remove(getPlayer());
			player = null;
		}
	}

	public boolean isPointInMap(Point pos)
	{
		return pos.getX() < size.getWidth() && pos.getY() < size.getHeight() && pos.getX() >= 0 && pos.getY() >= 0;
	}
	
	public static boolean isPointInGrid(Point p)
	{
		return p.getX() >= 0 && p.getY() >= 0 && p.getX() < Main.GRIDSIZE.getWidth()
				&& p.getY() < Main.GRIDSIZE.getHeight();
	}

	public void resetCamera()
	{
		moveView(-offset.getX(), -offset.getY());
	}

	public void centerView()
	{
		
		if(size.getWidth() < Main.GRIDSIZE.getWidth() && size.getHeight() < Main.GRIDSIZE.getHeight())
			return;
		
		while (getPlayer().getX() >= Main.GRIDSIZE.getWidth() - Map.VIEW_LIMIT + offset.getX()
				&& get(new Point(Main.GRIDSIZE.getWidth() + getOffSet().getX(), 0)).getTile() != null)
		{
			offset.setX(offset.getX() + 1);
		}

		while (getPlayer().getY() >= Main.GRIDSIZE.getHeight() - Map.VIEW_LIMIT + offset.getY()
				&& get(new Point(0, Main.GRIDSIZE.getHeight() + getOffSet().getY())).getTile() != null)
		{
			offset.setY(offset.getY() + 1);
		}

	}

	public List<Entity> getStrongEntities()
	{

		List<Entity> strongEntities = new ArrayList<Entity>();

		for (Slot s : getAllSlots())
		{
			for (Entity e : s.getAll())
			{
				if (e.isStrong())
					strongEntities.add(e);
			}
		}

		return strongEntities;
	}

	public List<Slot> getAllSlots()
	{
		List<Slot> slots = new ArrayList<Slot>();

		for (int i = 0; i < size.getWidth(); i++)
		{
			for (int j = 0; j < size.getHeight(); j++)
			{
				slots.add(matrix[i][j]);
			}
		}

		return slots;
	}

	public Slot get(Point pos)
	{
		if (isPointInMap(pos))
			return matrix[pos.getX()][pos.getY()];
		return null;
	}

	public boolean hasPlayer()
	{
		return player != null;
	}

	public Player getPlayer()
	{
		return player;
	}

	public Dimension getSize()
	{
		return size;
	}

	public Point getOffSet()
	{
		return offset;
	}

	public String getName()
	{
		return NAME;
	}

	public TextureManager getTextureManager()
	{
		return textureManager;
	}

	public void remove(Entity entity)
	{
		get(entity.position()).remove(entity);
	}

	public Portal getPortalByID(int id)
	{
		for (Slot s : getAllSlots())
		{
			Portal portal = s.getPortal();
			if (portal != null)
				if (portal.id() == id)
					return portal;
		}
		return null;
	}

}
