package game.entities.item;

import game.entities.Entity;
import game.entities.EntityType;
import game.structure.Slot;
import game.util.XMLParser;

import java.util.List;
import java.util.ListIterator;

import org.lwjgl.util.Point;

public abstract class Item extends Entity{
	
	private String NAME, DESCRIPTION;
	private int quantity;
	
	public Item(int id, int amount){
		super(id);
		quantity = amount;
		
		parseItem(new XMLParser(EntityType.getType(id()) + "/" + hexID() + "/data.xml"));
		
		setTexture(getMap().getTextureManager().get(id()));
	}
	
	protected void parseItem(XMLParser parser){
		NAME = parser.getAttribute(getClass().getSimpleName(), "name");
		DESCRIPTION = parser.getAttribute(getClass().getSimpleName(), "desc");
	}
	
	public Item(int id, Point pos){
		this(id, 1);
	}
	
	public Item(int id){
		this(id, 1);
	}
	
	public String getName() {
		return NAME;
	}

	public String getDescription() {
		return DESCRIPTION;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setQuantity(int amount){
		quantity = amount;
	}
	
	public void add(int num){
		quantity += num;
	}
	
	public void setPosition(Point pos){
		if(position() != null){
			
			Bundle bundle = (Bundle) getMap().get(position()).get(Slot.ITEMS);
			List<Item> items = bundle.getItems();
			
			for(ListIterator<Item> l = items.listIterator(); l.hasNext();){
				if(l.next().equals(this))
					l.remove();
			}
		}
		
		((Bundle) getMap().get(pos).get(Slot.ITEMS)).add(this);
		modifyPos(new Point(pos));
	}
	
	public String toString(){
		return getName();
	}
	
	public boolean equals(Object obj){
		return super.equals(obj) ? ((Item)obj).getQuantity() == getQuantity() : false; 
	}
	
	public int hashCode(){
		return super.hashCode() + 99999 + getQuantity();
	}
			
}
