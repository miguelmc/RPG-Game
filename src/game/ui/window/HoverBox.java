package game.ui.window;

import game.entities.item.EquipItem;
import game.entities.item.Item;
import game.features.Stat;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Util;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class HoverBox {

	private static final int HOVER_BOX_WIDTH = 200;
	private static final Dimension ITEM_IMG_SIZE = new Dimension(32, 32);
	private static final int PADDING = 10;
	private static final Point namePosition = new Point(ITEM_IMG_SIZE.getWidth() + PADDING*2, PADDING);
	
	private HoverBox(){}
	
	public static void render(Item item, Point position)
	{
		Writer.useFont(Fonts.Courier_White_Bold_14);
		
		List<String> paragraph = new ArrayList<String>(); //everything written in the hover box						
		
		//TODO render item name, equipitem stats are not rendering
		
		if(item instanceof EquipItem)
		{
			EquipItem equip = (EquipItem) item;
			
			//adds the equip stats to the paragraph
			for (int i = 0; i < Stat.values().length; i++) {
				if (equip.getStat(Stat.values()[i]) != 0)
					paragraph.add(Stat.values()[i].toString() + ": "
								  + Integer.toString(equip.getStat(Stat.values()[i])));
			}
			
			paragraph.add("");
		}
		
		//adds the  item description to the paragraph
		paragraph.addAll(Writer.toParagraph(item.getDescription(), 185));
		
		if(item instanceof EquipItem)
		{
			paragraph.add("");
			paragraph.add("Upgrades Available: " + ((EquipItem)item).getUpgradesAvailable());
		}
		
		Renderer.render(new Builder(
				Util.getTexture("UI/window/hoverBox.png"),
				position,
				new Dimension(HOVER_BOX_WIDTH, Writer.fontHeight() * paragraph.size() + ITEM_IMG_SIZE.getHeight() + PADDING*3)));

		//renders the item's image's background
		Renderer.renderQuad(new Point(position.getX() + PADDING, position.getY() + PADDING), ITEM_IMG_SIZE);

		//renders the item's image
		Renderer.render(new Builder(
				item.getTexture(),
				new Point(position.getX() + PADDING, position.getY() + PADDING),
				ITEM_IMG_SIZE));

		//writes the item name
		Writer.write(Writer.toParagraph(item.getName(), 
										HOVER_BOX_WIDTH - ITEM_IMG_SIZE.getWidth() - PADDING*3), //max length
										new Point(position.getX() + namePosition.getX(), //position
											      position.getY() + namePosition.getY()),
										2); //max number of lines
		
		//writes the paragraph
		Writer.write(paragraph, new Point(position.getX() + PADDING, position.getY() + PADDING*2 + ITEM_IMG_SIZE.getHeight()), -1);
	}
	
}
