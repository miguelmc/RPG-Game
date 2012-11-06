package game.ui.window;

import game.Main;
import game.entities.item.EquipItem;
import game.entities.item.EquipItem.EquipType;
import game.structure.MapManager;
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
			Renderer.render(new Builder(
					equip.getValue().getTexture(),
					new Point(positions.get(equip.getKey()).getX() + getX() - 5,
							  positions.get(equip.getKey()).getY() + getY() - 10),
					new Dimension(itemSize, itemSize))
					.imageSize(32, 32));
		}

		EquipItem equip = getEquipAt(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1);

		if (equip != null) //if the mouse is over an equip
			HoverBox.render(equip, new Point(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1));
	}
	
	private EquipItem getEquipAt(int x, int y) {

		for (Map.Entry<EquipType, EquipItem> equip : MapManager.getMap()
				.getPlayer().getEquips().entrySet()) {
			if (x >= positions.get(equip.getKey()).getX() + getX()
				&& x <= positions.get(equip.getKey()).getX() + getX() + itemSize
				&& y >= positions.get(equip.getKey()).getY() + getY()
				&& y <= positions.get(equip.getKey()).getY() + getY() + itemSize)
				return equip.getValue();
		}

		return null;
	}

	int getKey() {
		return Keyboard.KEY_E;
	}

}
