package game.ui.window;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.Main;
import game.entities.item.EquipItem;
import game.entities.item.EquipItem.EquipType;
import game.entities.item.Item;
import game.features.Stat;
import game.structure.MapManager;
import game.structure.Slot;
import game.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Equipment extends Window {

	private static final Map<EquipType, Point> positions = new HashMap<EquipType, Point>();
	List<Item> items = MapManager.getMap().getPlayer().getItems();

	static {
		positions.put(EquipType.HELMET, new Point(74, 45));
		positions.put(EquipType.TOPWEAR, new Point(74, 96));
		positions.put(EquipType.BOTTOMWEAR, new Point(74, 145));
		positions.put(EquipType.SHOES, new Point(74, 194));
		positions.put(EquipType.WEAPON, new Point(16, 96));
		positions.put(EquipType.SHIELD, new Point(126, 96));

	}

	public Equipment() {
		super(new Point(100, 100), new Dimension(174, 256));
	}

	private int getClickedItem(int xPos, int yPos) {
		for (int i = 0; i < items.size(); i++) {
			if (xPos > getPosition().getX() + 7 + 32 * (i % 5)
					&& xPos < getPosition().getX() + 7 + 32 * (i % 5)
							+ Slot.SIZE
					&& yPos > getPosition().getY() + 30 + 32 * (i / 5)
					&& yPos < getPosition().getY() + 30 + 32 * (i / 5)
							+ Slot.SIZE)
				return i;
		}
		return -1;

	}

	public void render() {
		super.render();

		for (Map.Entry<EquipType, EquipItem> equip : MapManager.getMap()
				.getPlayer().getEquips().entrySet()) {
			Point position = positions.get(equip.getKey());
			equip.getValue().render(position.getX() + getX(),
					position.getY() + getY());
		}
		
	}

	@Override
	protected void mouse() {
		// TODO Auto-generated method stub

	}

	int getKey() {
		return Keyboard.KEY_E;
	}

}
