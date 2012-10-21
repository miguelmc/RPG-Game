package game.ui;

import game.entities.Entity;
import game.entities.item.Item;
import game.entities.superentities.Player;
import game.structure.GameObject;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shop extends GameObject{

	List<Item> items = new ArrayList<Item>();
	
	public Shop(int id)
	{
		super(id);
		
		parseShop();
		
	}

	private void parseShop() 
	{
		
		XMLParser parser = new XMLParser("shop/" + hexID() + "/data.xml");
		
		List<Map<String, String>> shopItems = parser.getChildrenAttributes("Shop/items");
		
		for(Map<String, String> item: shopItems)
		{
			items.add((Item) Entity.createEntity(Integer.parseInt(item.get("id"))));
		}
		
	}
	
	public void render()
	{
		//TODO
	}
	
	public boolean buy(int id, Player player)
	{
		
		Item item = items.get(id);
		int price = 100;
		
		if(player.getGold() < price)
			return false;
		
		MsgBoxManager.sendText("Are you sure you want to buy " + item + " for $" + price + "?", true);
		if(MsgBoxManager.getSelection())
		{
			if(player.addItem(item))
			{
				player.gainGold(-price);
				return true;
			}else
			{
				MsgBoxManager.sendText("Make sure you have enough space in your inventory.", false);
			}
		}
		
		return false;
		
	}
	
	public void sell(int id, Player player)
	{
		
	}
	
}
