package game.ui.window;

import game.Main;
import game.entities.item.EquipItem;
import game.entities.item.EquipItem.EquipType;
import game.structure.MapManager;
import game.util.MouseManager;
import game.util.Renderer;
import game.util.Renderer.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Equipment extends Window {

	private static final Map<EquipType, Point> positions = new HashMap<EquipType, Point>();
	private static final int itemSize = 44;
	private long grabTime;
	
	static { //TODO read from file
		positions.put(EquipType.HELMET, new Point(74, 45));
		positions.put(EquipType.TOPWEAR, new Point(74, 96));
		positions.put(EquipType.BOTTOMWEAR, new Point(74, 145));
		positions.put(EquipType.SHOES, new Point(74, 198));
		positions.put(EquipType.WEAPON, new Point(16, 96));
		positions.put(EquipType.SHIELD, new Point(126, 96));
	}

	public Equipment() {
		super(new Point(100, 100), new Dimension(174, 256));
	}

	public void render() {
		super.render();

		//renders every equip
		Set<Entry<EquipType, EquipItem>> equips = MapManager.getMap().getPlayer().getEquips().entrySet();
		for (Map.Entry<EquipType, EquipItem> equip : equips) {
			if(equip.getValue() != null)
				Renderer.render(new Builder(
						equip.getValue().getTexture(),
						new Point(positions.get(equip.getKey()).getX() + getX() - 5,
								  positions.get(equip.getKey()).getY() + getY() - 10),
						new Dimension(itemSize, itemSize))
						.imageSize(32, 32));
		}

		EquipItem equip = getEquipAt(MouseManager.getPosition());

		if (equip != null) //if the mouse is over an equip
			HoverBox.render(equip, new Point(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1));
	}
	
	private EquipItem getEquipAt(Point pos) {

		for (Map.Entry<EquipType, EquipItem> equip : MapManager.getMap()
				.getPlayer().getEquips().entrySet()) {
			if (pos.getX() >= positions.get(equip.getKey()).getX() + getX()
				&& pos.getX() <= positions.get(equip.getKey()).getX() + getX() + itemSize
				&& pos.getY() >= positions.get(equip.getKey()).getY() + getY()
				&& pos.getY() <= positions.get(equip.getKey()).getY() + getY() + itemSize)
				return equip.getValue();
		}

		return null;
	}
	
	public void mouse()
	{
		if(MouseManager.mousePressed())
		{	
			EquipItem clickedEquip = getEquipAt(MouseManager.getPosition());
			if(clickedEquip == null)
				super.mouse();
			else
			{
				if(grabTime + DOUBLE_CLICK_DELAY > System.currentTimeMillis())
				{
					grabTime = 0;
					MapManager.getMap().getPlayer().removeEquip(clickedEquip.getType());
				}else grabTime = System.currentTimeMillis();
			}
		}else super.mouse();
	}

	int getKey() {
		return Keyboard.KEY_E;
	}

}
