package game.entities;

import game.util.XMLParser;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Point;

public class Object extends Entity
{

	private List<Block> blocks = new ArrayList<Block>();

	public Object(int id)
	{
		super(id);

		if (isBlock())
			return;

		XMLParser parser = new XMLParser("object/" + hexID() + "/data.xml");

		setRenderSize(Integer.parseInt(parser.getAttribute("Object", "width")),
				Integer.parseInt(parser.getAttribute("Object", "height")));
		setRenderOffset(Integer.parseInt(parser.getAttribute("Object", "offsetX")),
				Integer.parseInt(parser.getAttribute("Object", "offsetY")));

		List<java.util.Map<String, String>> blockList = parser.getChildrenAttributes("Object");
		for (java.util.Map<String, String> data : blockList)
		{
			Block block = new Block(this);
			block.modifyPos(new Point(Integer.parseInt(data.get("x")), Integer.parseInt(data.get("y"))));
			blocks.add(block);
		}

	}

	public Object()
	{
		super(-1);
	}

	public List<Block> getBlocks()
	{
		return blocks;
	}

	public boolean isBlock()
	{
		return id() == Integer.parseInt("6700", 16);
	}

	public void modifyPos(Point pos)
	{
		super.modifyPos(pos);

		if (blocks != null)
		{
			for (Block block : blocks)
			{
				block.modifyPos(new Point(pos.getX() + block.getX(), pos.getY() + block.getY()));
			}
		}
	}

	public void render()
	{
		super.render();
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
			this.parent = parent;
		}

		public Object getParent()
		{
			return parent;
		}

	}

}