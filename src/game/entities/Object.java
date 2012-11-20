package game.entities;

import game.util.XMLParser;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Point;

/**
 * Entity for objects such as trees or houses.
 * The object itself is not "strong" but it contains several Blocks which are strong. This is because an object can be bigger than a single tile by no Entity can.
 */
public class Object extends Entity
{

	private List<Block> blocks = new ArrayList<Block>();

	public Object(int id)
	{
		super(id);
		
		if(isBlock()) return;
				
		XMLParser parser = new XMLParser(path() + "data.xml");
		
		setRenderSize(Integer.parseInt(parser.getAttribute("Object", "width")),
					  Integer.parseInt(parser.getAttribute("Object", "height")));
		setOffset(Integer.parseInt(parser.getAttribute("Object", "offsetX")),
				  Integer.parseInt(parser.getAttribute("Object", "offsetY")));

		List<java.util.Map<String, String>> blockList = parser.getChildrenAttributes("Object");
		for (java.util.Map<String, String> data : blockList)
		{
			Block block = new Block(this);
			block.modifyPos(new Point(Integer.parseInt(data.get("x")), Integer.parseInt(data.get("y"))));
			blocks.add(block);
		}
	}

	public List<Block> getBlocks()
	{
		return blocks;
	}

	public boolean isBlock()
	{
		return this instanceof Block;
	}

	public void modifyPos(Point pos)
	{
		super.modifyPos(pos);

		if (blocks != null)
			for (Block block : blocks)
				block.modifyPos(new Point(pos.getX() + block.getX(), pos.getY() + block.getY()));
	}

	/**
	 * Instance of an object which is invisible and is the building block of an
	 * object bigger than 1 tile
	 */
	public class Block extends Object
	{

		Object parent;

		public Block(Object parent)
		{
			super(Integer.parseInt("6700", 16));
			setStrong();
			setInvisible(true);
		}

		public Object getParent()
		{
			return parent;
		}
		
		public int id()
		{
			return parent.id();
		}

	}

}