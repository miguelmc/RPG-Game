package game.ui;

import game.Main;
import game.entities.Entity;
import game.entities.item.EquipItem;
import game.entities.item.Item;
import game.entities.superentities.Player;
import game.structure.GameObject;
import game.structure.MapManager;
import game.util.Util;
import game.util.XMLParser;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Shop extends GameObject{

	private Texture texture;
	private List<Item> items = new ArrayList<Item>();
	private List<Item> playerItems = new ArrayList<Item> (MapManager.getMap().getPlayer().getItems());
	private Item itemSelected;
	private long lastItemSelection = 0;
	
	public Shop(int id)
	{
		super(id);
		texture = Util.getTexture("shop/" + hexID() +"/texture.png");
		parseShop();
		
	}

	private void parseShop() 
	{
		
		XMLParser parser = new XMLParser("shop/" + hexID() + "/data.xml");
		
		List<Map<String, String>> shopItems = parser.getChildrenAttributes("Shop/items");
		
		for(Map<String, String> item: shopItems)
		{
			items.add((Item) Entity.createEntity(Integer.parseInt(item.get("id"), 16)));
		}
		
	}
	
	public void render()
	{
		int sellX=297, buyX = 130, Y=85, goldX = 120, goldY = 513;
		Util.render(texture, 40, 50, 512, 512, 512, 512);
		Util.useFont("Courier New", Font.BOLD, 14, Color.white);
		for(int i=0; i<playerItems.size(); i++)
		{
			playerItems.get(i).render(sellX + 7 + 32 * (i % 5), Y + 30 + 32 * (i / 5));
			if (!(playerItems.get(i) instanceof EquipItem))
			{
				Util.write(Integer.toString(playerItems.get(i).getQuantity()), sellX + 7 + 32 * (i % 5) + 3,
						Y + 30 + 32 * (i / 5));
			}
		}
		for(int i=0; i<items.size(); i++)
		{
			items.get(i).render(buyX + 7 + 32 * (i % 5), Y + 30 + 32 * (i / 5));
		}
		
		Util.useFont("Monaco", Font.PLAIN, 25, Color.white);
		Util.write(Integer.toString(MapManager.getMap().getPlayer().getGold()), goldX,
				goldY);
		
		if(itemSelected != null)
		{
			Util.render(itemSelected.getTexture(), 80, 330, 100, 100, 32, 32);
			Util.write(itemSelected.toString(), 70, 450);
			
			String lines[] = Util.tokenizeText(itemSelected.getDescription(), 280, 3);
			
			for(int i=0; i<lines.length; i++)
			{
				Util.write(lines[i], 200, 320+Util.getFontHeight()*i);
			}
			
			Util.write("$"+itemSelected.getPrice(), 520 - Util.getTextWidth("$"+itemSelected.getPrice()), 460);
		}
		
	}
	
	private Item getItemInPosition(Point position)
	{
				
		int row = (position.getY() - 55)/32 - 2;
		int column = (position.getX() - 137)/32;
		
		int index = row*5 + column;
		
		if(index >= 0 && index < items.size())
			return items.get(row*5+column);
		return null;
		
		
	}
	
	public void mouseInput() {
		
		if(Mouse.getEventButtonState())
		{
			itemSelected = getItemInPosition(new Point(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1));
			if(System.currentTimeMillis() < lastItemSelection+200)
			{
				buy();
			}
			lastItemSelection = System.currentTimeMillis();
		}
		
		
	}
	
	public Item getItemByID(int id)
	{
		for(Item item: items)
		{
			if(item.id() == id)
				return item;
		}
		return null;	
	}
	
	public boolean buy()
	{
						
		if(MapManager.getMap().getPlayer().getGold() < itemSelected.getPrice())
			return false;
				
		MsgBoxManager.sendText("Are you sure you want to buy " + itemSelected + " for $" + itemSelected.getPrice() + "?", true, "onSelection", this);
		if(MsgBoxManager.getSelection())
		{
			
		}
		
		return false;
		
	}
	
	public void onSelection(Boolean selection)
	{
		Player player = MapManager.getMap().getPlayer();
		if(selection == false)
			return;
		
		if(player.addItem(itemSelected))
			player.gainGold(-itemSelected.getPrice());
		else
			MsgBoxManager.sendText("Make sure you have enough space in your inventory.", false);
	}

	

}
