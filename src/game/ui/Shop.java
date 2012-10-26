package game.ui;

import game.Main;
import game.entities.Entity;
import game.entities.item.EquipItem;
import game.entities.item.Item;
import game.entities.superentities.Player;
import game.structure.GameObject;
import game.structure.MapManager;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Util;
import game.util.XMLParser;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Shop extends GameObject{

	private Texture texture;
	private List<Item> items = new ArrayList<Item>();
	private List<Item> playerItems = MapManager.getMap().getPlayer().getItems();
	private Item itemSelected;
	private long lastItemSelection = 0;
	private static Texture buyButton, sellButton;
	private boolean buy = true;
		
	static
	{
		buyButton = Util.getTexture("shop/buy.png");
		sellButton = Util.getTexture("shop/sell.png");
	}
	
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
			System.out.println(items.get(items.size()-1).getQuantity());
		}
		
	}
	
	public void render()
	{
		int sellX=297, buyX = 130, Y=85, goldX = 120, goldY = 513;
		Renderer.render(new Builder(
				texture,
				new Point(40, 50),
				new Dimension(512, 512)));
		
		Util.useFont("Courier New", Font.BOLD, 14, Color.white);
		for(int i=0; i<playerItems.size(); i++)
		{
			
			Renderer.render(new Builder(
					playerItems.get(i).getTexture(),
					new Point(sellX + 7 + 32 * (i % 5), Y + 30 + 32 * (i / 5)),
					new Dimension(32, 32)));			
			
			if (!(playerItems.get(i) instanceof EquipItem))
			{
				Util.write(Integer.toString(playerItems.get(i).getQuantity()), sellX + 7 + 32 * (i % 5) + 3,
						Y + 30 + 32 * (i / 5));
			}
		}
		for(int i=0; i<items.size(); i++)
		{
			Renderer.render(new Builder(
					items.get(i).getTexture(),
					new Point(buyX + 7 + 32 * (i % 5),  Y + 30 + 32 * (i / 5)),
					new Dimension(32, 32)));
		}
		
		Util.useFont("Monaco", Font.PLAIN, 25, Color.white);
		Util.write(Integer.toString(MapManager.getMap().getPlayer().getGold()), goldX,
				goldY);
		
		if(itemSelected != null)
		{
			
			Renderer.render(new Builder(
					itemSelected.getTexture(),
					new Point(80, 330),
					new Dimension(100, 100))
					.imageSize(32, 32));
			Util.write(itemSelected.toString(), 70, 450);
			
			String lines[] = Util.tokenizeText(itemSelected.getDescription(), 280, 3);
			
			for(int i=0; i<lines.length; i++)
				Util.write(lines[i], 200, 320+Util.getFontHeight()*i);
			
			if(buy)
			{	
				Util.write("$"+itemSelected.getPrice(), 520 - Util.getTextWidth("$"+itemSelected.getPrice()), 460);
				
				Renderer.render(new Builder(
						buyButton,
						new Point(350, 450),
						new Dimension(100, 46))
						.imageSize(40, 18));
				
			}else
			{
				Util.write("$"+(int)(itemSelected.getPrice()*.6), 520 - Util.getTextWidth("$"+(int)(itemSelected.getPrice()*.6)), 460);
				
				Renderer.render(new Builder(
						sellButton,
						new Point(350, 450),
						new Dimension(100, 46))
						.imageSize(40, 18));
				
			}
		}
	}
	
	private Item getItemInPosition(Point position) // FIXME NOT WORKING PROPERLY
	{
			
		if(position.getX()<137 || position.getY()>300)
			return null;
		
		int row = (position.getY() - 55)/32 - 2;
		
		if(position.getX() > 304)
		{
			
			int column = (position.getX()-304)/32;
			
			if(column>=0 && column < 5 && row >= 0 && row < 6 && row*5+column < playerItems.size()){
				buy = false;
				return playerItems.get(row*5+column);
			}
		}else
		{
			int column = (position.getX() - 137)/32;
			
			if(column>=0 && column < 5 && row >= 0 && row < 6 && row*5 + column < items.size()){
				buy = true;
				return items.get(row*5+column);
			}
		}
		return null;
	}
	
	public void mouseInput() {
		
		if(MsgBoxManager.isActive())
			return;
		
		if(Mouse.getEventButtonState())
		{
			
			int x = Mouse.getX();
			int y = Main.DIM.getHeight() - Mouse.getY() + 1;
			Item selection = getItemInPosition(new Point(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1));
			itemSelected = selection != null ? selection : itemSelected;
			
			if(itemSelected == null)
				return;
			
			if(System.currentTimeMillis() < lastItemSelection+200 || (x > 350 && y > 450 && x < 350 + 100 && y < 450 + 46))
			{
				if(buy)
				{
					buy();
				}else 
				{
					sell();
				}
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
	
	public void buy()
	{					
		if(MapManager.getMap().getPlayer().getGold() < itemSelected.getPrice())
		{
			MsgBoxManager.sendMessage("You cant afford to buy that...", MsgBoxManager.OK);
			return;
		}
				
		MsgBoxManager.sendMessage("Are you sure you want to buy " + itemSelected + " for $" + itemSelected.getPrice() + "?", MsgBoxManager.YES_NO, onBuy);
	}
	
	private Runnable onBuy = new Runnable(){
		public void run()
		{
			Player player = MapManager.getMap().getPlayer();
			if(!MsgBoxManager.getAnswer())
			{
				MsgBoxManager.sendMessage("Make up your mind...", MsgBoxManager.OK);
			}else
			{
				System.out.println(itemSelected.getQuantity());
				if(player.addItem(itemSelected))
					player.gainGold(-itemSelected.getPrice());
				else
					MsgBoxManager.sendMessage("Make sure you have enough space in your inventory.", MsgBoxManager.OK);
			}
		}
	};
	
	public void sell()
	{
		MsgBoxManager.sendMessage("Are you sure you want to sell " + itemSelected + " for $" + (int)(itemSelected.getPrice()*.6) + "?", MsgBoxManager.YES_NO, onSell);
	}
	
	private Runnable onSell = new Runnable(){
		public void run()
		{
			Player player = MapManager.getMap().getPlayer();
			if(!MsgBoxManager.getAnswer())
			{
				MsgBoxManager.sendMessage("Make up your mind...", MsgBoxManager.OK);
			}else
			{
				player.gainGold((int) (itemSelected.getPrice()*.6));
				
				player.loseItem(itemSelected.id(), 1);
				itemSelected = null;
			}
		}
	};

}
