package game.ui.window;

import game.Main;
import game.entities.item.EquipItem;
import game.entities.item.Item;
import game.entities.item.UsableItem;
import game.structure.MapManager;
import game.util.MouseManager;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Util;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Inventory extends Window
{

	private List<Item> items = MapManager.getMap().getPlayer().getItems();
	private Item grabbedItem = null;
	private long grabTime;
	
	private static final int DOUBLE_CLICK_DELAY = 250;
	private static final Point GOLD_POS = new Point(37, 231), SQUARES_ORIGIN = new Point(7, 30);
	private static final int SQUARE_SIZE = 32;
	private static final Dimension INV_SIZE = new Dimension(5, 6);
	
	public Inventory()
	{
		super(new Point(350, 100), new Dimension(174, 256));
	}

	public void mouse()
	{
		if(MouseManager.mousePressed())
		{	
			Item clickedItem = getItemAt(MouseManager.getPosition());
			if(clickedItem == null)
				super.mouse();
			else
			{
				if(grabTime + DOUBLE_CLICK_DELAY > System.currentTimeMillis())
				{
					grabbedItem = null;
					grabTime = 0;
					if(clickedItem instanceof EquipItem)
						MapManager.getMap().getPlayer().addEquip((EquipItem) clickedItem);
					else if(clickedItem instanceof UsableItem)
						MapManager.getMap().getPlayer().useItem((UsableItem) clickedItem);
				}else
				{
					grabTime = System.currentTimeMillis();
					grabbedItem = clickedItem;
				}
			}
		}else
		{
			super.mouse();
		}
		
	}

	private Item getItemAt(Point position)
	{
		if(!Util.inRange(position, 
						new Point(getX() + SQUARES_ORIGIN.getX(),
								  getY() + SQUARES_ORIGIN.getY()), 
						new Dimension(SQUARE_SIZE*INV_SIZE.getWidth(), 
								      SQUARE_SIZE*INV_SIZE.getHeight())))
			return null;
				
		Point invPos = new Point((position.getX() - SQUARES_ORIGIN.getX() - getX())/SQUARE_SIZE, 
								 (position.getY() - SQUARES_ORIGIN.getY() - getY())/SQUARE_SIZE);
		
		
		if(invPos.getY()*INV_SIZE.getWidth() + invPos.getX() < items.size())
			return items.get(invPos.getY()*INV_SIZE.getWidth() + invPos.getX());
		
		return null;

	}

	public void render() 
	{
		super.render();

		Writer.useFont(Fonts.Courier_White_Bold_14);
		for (int i = 0; i < items.size(); i++)
		{
			Renderer.render(new Builder(
					items.get(i).getTexture(),
					new Point(getPosition().getX() + 7 + SQUARE_SIZE * (i % 5), getPosition().getY() + 30 + SQUARE_SIZE * (i / 5)),
					new Dimension(SQUARE_SIZE, SQUARE_SIZE)));
			
			if (!(items.get(i) instanceof EquipItem))
				Writer.write(Integer.toString(items.get(i).getQuantity()), 
							 new Point(getX() + 10 + SQUARE_SIZE * (i%5), getY() + 30 + SQUARE_SIZE * (i/5)));
		}

		Writer.write(Integer.toString(MapManager.getMap().getPlayer().getGold()),
					 new Point(getX() + GOLD_POS.getX(), getY() + GOLD_POS.getY()));
		
		if (grabbedItem != null)
		{
			Dimension renderSize = new Dimension(SQUARE_SIZE, SQUARE_SIZE);
			Point renderPos = new Point(MouseManager.getX() - renderSize.getWidth()/2, 
										MouseManager.getY() - renderSize.getHeight()/2);
			Renderer.render(new Builder(
							grabbedItem.getTexture(),
							renderPos,
							renderSize));
		}
		
		Item item = getItemAt(MouseManager.getPosition());
		
		if (item != null)
			HoverBox.render(item, new Point(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1));
	}
	
	public void onClose()
	{
		grabbedItem = null;
	}
	
}
