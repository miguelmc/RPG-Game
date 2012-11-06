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
	
	private HoverBox(){}
	
	public static void render(Item item, Point position)
	{
		Writer.useFont(Fonts.Courier_White_Bold_14);
		List<String> paragraph = new ArrayList<String>(); //everything written in the hover box						
		
		if(item instanceof EquipItem)
		{
			EquipItem equip = (EquipItem) item;
			
			//adds the equip stats to the paragraph
			for (int i = 0; i < Stat.values().length; i++) {
				if (equip.getStat(Stat.values()[i]) != 0)
					paragraph.add(Stat.values()[i].NAME + ": "
								  + Integer.toString(equip.getStat(Stat.values()[i])));
			}
		}
		
		//adds the  item description to the paragraph
		paragraph.addAll(Writer.toParagraph(item.getDescription(), 185));
		
		Renderer.render(new Builder(
				Util.getTexture("UI/window/itemDesc.png"),
				position,
				new Dimension(HOVER_BOX_WIDTH, Writer.fontHeight() * paragraph.size() + 55)));

		//renders the item's image's background
		Renderer.renderQuad(new Point(position.getX() + 10, position.getY() + 10), ITEM_IMG_SIZE);

		//renders the item's image
		Renderer.render(new Builder(
				item.getTexture(),
				new Point(position.getX() + 10, position.getY() + 10),
				ITEM_IMG_SIZE));

		//writes the paragraph
		Writer.write(paragraph, new Point(position.getX() + 10, position.getY() + 50), -1);
	}
	
}
