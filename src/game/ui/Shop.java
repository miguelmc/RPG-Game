package game.ui;

import game.entities.Entity;
import game.entities.item.Item;
import game.entities.superentities.Player;
import game.structure.GameObject;
import game.structure.MapManager;
import game.util.Util;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;

public class Shop extends GameObject{

	private Texture texture;
	private List<Item> items = new ArrayList<Item>();
	private Item itemSelected;
	
	
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
		int X=297, Y=85;
		Util.render(texture, 40, 50, 512, 512, 512, 512);
		for(int i=0; i<items.size(); i++)
		{
			items.get(i).render(X + 7 + 32 * (i % 5), Y + 30 + 32 * (i / 5));
		}
		
	}
	
	public void mouseInput() {
		// TODO Auto-generated method stub
		
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
	
	public boolean buy(int id, Player player)
	{
		
		itemSelected = getItemByID(id);
				
		if(player.getGold() < itemSelected.getPrice())
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
