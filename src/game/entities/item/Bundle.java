package game.entities.item;

import game.entities.Entity;
import game.structure.Slot;

import java.util.ArrayList;
import java.util.List;

public class Bundle extends Entity {

	private List<Item> items = new ArrayList<Item>();
	
	public Bundle(List<Item> list) {
		super(Integer.parseInt("6300",16));
		this.items = list;
	}
	
	public List<Item> getItems(){
		return items;
	}
	
	public void add(Item i){
		items.add(i);
		setRenderPositions();
	}

	public void remove(Item i) {
		items.remove(i);
		setRenderPositions();
	}
	
	public void render() {
		for(Item item: getItems()){
			item.render();
		}
	}
	
	public void setRenderPositions(){
		
		int size = Slot.SIZE;
		
		switch(items.size()){
		
		case 1:
			items.get(0).getRenderOffSet().setLocation(0, 0);
			break;
		case 2:
			items.get(0).getRenderOffSet().setLocation(-size/4, size/4);
			items.get(1).getRenderOffSet().setLocation(size/4, -size/4);
			break;
		case 3:
			items.get(0).getRenderOffSet().setLocation(-size/3, size/3);
			items.get(1).getRenderOffSet().setLocation(0, -size/3);
			items.get(2).getRenderOffSet().setLocation(size/3, size/3);
			break;
		default:
			items.get(0).getRenderOffSet().setLocation(-size/3, size/3);
			items.get(1).getRenderOffSet().setLocation(-size/3, -size/3);
			items.get(2).getRenderOffSet().setLocation(size/3, -size/3);
			items.get(3).getRenderOffSet().setLocation(size/3, size/3);
			break;
		}
	}
	
}
