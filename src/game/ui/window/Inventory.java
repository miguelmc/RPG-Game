package game.ui.window;

import game.Main;
import game.entities.item.EquipItem;
import game.entities.item.Item;
import game.entities.item.UsableItem;
import game.structure.MapManager;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Inventory extends Window
{

	List<Item> items = MapManager.getMap().getPlayer().getItems();
	private Item click = null;
	private long timeOfClick = 0L;
	private boolean itemGrabbed = false;

	private static final Point GOLD_POS = new Point(37, 231);
	private static final int ITEM_IMG_SIZE = 32;
	
	public Inventory()
	{
		super(new Point(350, 100), new Dimension(174, 256));
	}

	public void mouse()
	{

		Item clickedItem = getItemAt(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1);

		if (Mouse.getEventButtonState())
		{
			setPressed(true);

			if (clickedItem != null)
			{
				
			}
		} else if (!Mouse.getEventButtonState())
		{
			int dX = Mouse.getDX();
			int dY = Mouse.getDY();
			if (!itemGrabbed)
			{
				if (Mouse.isButtonDown(0) && isPressed())
				{
					if (clickedItem == null)
					{
						if (!((dX > 0 && getX() > Main.DIM.getWidth() * .98)
								|| (dX < 0 && getX() + SIZE.getWidth() < Main.DIM.getWidth() * .02)
								|| (dY > 0 && getY() < Main.DIM.getHeight() * .02) || (dY < 0 && getY() > Main.DIM
								.getHeight() * .98)))
						{
							setPosition(getX() + dX, getY() - dY);
						}
					}
				} else if (dX == 0 && dY == 0)
				{
					setPressed(false);
				}
			} else if (dX == 0 && dY == 0)
			{
				itemGrabbed = false;
			}
		}

		if (Mouse.getEventButtonState())
		{
			if (timeOfClick + 200L < System.currentTimeMillis())
			{
				click = clickedItem;
				timeOfClick = System.currentTimeMillis();
			} else if (clickedItem == click && click != null)
			{
				if (clickedItem instanceof UsableItem)
				{
					MapManager.getMap().getPlayer().useItem(clickedItem);
				} else if (clickedItem instanceof EquipItem)
				{
					EquipItem equip = (EquipItem) clickedItem;
					MapManager.getMap().getPlayer().removeEquip(equip.getType());
					MapManager.getMap().getPlayer().addEquip(equip);
					MapManager.getMap().getPlayer().removeItem(clickedItem);
				}
				click = null;
			}
		}
	}

	private Item getItemAt(int xPos, int yPos)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (xPos > getPosition().getX() + 7 + ITEM_IMG_SIZE * (i % 5)
					&& xPos < getPosition().getX() + 7 + ITEM_IMG_SIZE * (i % 5) + ITEM_IMG_SIZE
					&& yPos > getPosition().getY() + 30 + ITEM_IMG_SIZE * (i / 5)
					&& yPos < getPosition().getY() + 30 + ITEM_IMG_SIZE * (i / 5) + ITEM_IMG_SIZE)
				return items.get(i);
		}
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
					new Point(getPosition().getX() + 7 + ITEM_IMG_SIZE * (i % 5), getPosition().getY()+ 30 + ITEM_IMG_SIZE * (i / 5)),
					new Dimension(32, 32)));
			
			if (!(items.get(i) instanceof EquipItem))
			{
				Writer.write(Integer.toString(items.get(i).getQuantity()), 
							 new Point(getX() + 10 + ITEM_IMG_SIZE * (i%5), getY() + 30 + ITEM_IMG_SIZE * (i/5)));
			}
		}

		Writer.write(Integer.toString(MapManager.getMap().getPlayer().getGold()),
					 new Point(getX() + GOLD_POS.getX(), getY() + GOLD_POS.getY()));

		Item item = getItemAt(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1);

		if (item != null)
			HoverBox.render(item, new Point(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1));
	}

	@Override
	int getKey()
	{
		return Keyboard.KEY_I;
	}

}
