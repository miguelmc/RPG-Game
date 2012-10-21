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
import game.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Equipment extends Window {

	private static final Map<EquipType, Point> positions = new HashMap<EquipType, Point>();
	private static final int itemSize = 44;
	static {
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

		for (Map.Entry<EquipType, EquipItem> equip : MapManager.getMap()
				.getPlayer().getEquips().entrySet()) {
			Point position = positions.get(equip.getKey());
			Util.render(equip.getValue().getTexture(), position.getX() + getX()
					- 5, position.getY() + getY() - 10, itemSize, itemSize, 32,
					32);

		}

		Item item = getItemInPosition(Mouse.getX(), Main.DIM.getHeight()
				- Mouse.getY() + 1);

		
		if (item != null) {
			String lines[];
			EquipItem equip = ((EquipItem) item);
			ArrayList<String> linesList = new ArrayList<String>();
			for (int i = 0; i < Stat.values().length; i++) {
				if (equip.getStat(Stat.values()[i]) != 0)
					linesList
							.add(Stat.values()[i].NAME
									+ ": "
									+ Integer.toString(equip.getStat(Stat
											.values()[i])));
			}
			String[] description = Util.tokenizeText(item.getDescription(),
					190, 4);

			lines = new String[linesList.size() + description.length];
			int i;
			for (i = 0; i < linesList.size(); i++)
				lines[i] = linesList.get(i);
			for (int j = 0; j < description.length; j++)
				lines[i + j] = description[j];

		Texture tex = Util.getTexture("UI/window/itemDesc.png");

		glColor4f(1, 1, 1, .5f);

		Util.render(tex, Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1,
				200, Util.getFontHeight() * lines.length + 55,
				tex.getTextureWidth(), tex.getTextureHeight());

		// render a white square with half transparency
		glLoadIdentity();
		glTranslatef(Mouse.getX(), Main.DIM.getHeight() - Mouse.getY() + 1, 0);
		glBegin(GL_QUADS);
		glVertex2f(10, 10);
		glVertex2f(42, 10);
		glVertex2f(42, 42);
		glVertex2f(10, 42);
		glEnd();
		glLoadIdentity();

		glColor4f(1, 1, 1, 1); // return to full opacity

		item.render(Mouse.getX() + 10, Main.DIM.getHeight() - Mouse.getY() + 1
				+ 10);

		Util.write(item.getName(), Mouse.getX() + 42 + 10, Main.DIM.getHeight()
				- Mouse.getY() + 1 + 10);

		for (i = 0; i < lines.length; i++) {
			Util.write(lines[i], Mouse.getX() + 10, Main.DIM.getHeight()
					- Mouse.getY() + 1 + 50 + i * Util.getFontHeight());
		}
	}
}
	private Item getItemInPosition(int x, int y) {

		for (Map.Entry<EquipType, EquipItem> equip : MapManager.getMap()
				.getPlayer().getEquips().entrySet()) {
			if (x >= positions.get(equip.getKey()).getX() + getX()
					&& x <= positions.get(equip.getKey()).getX() + getX()
							+ itemSize
					&& y >= positions.get(equip.getKey()).getY() + getY()
					&& y <= positions.get(equip.getKey()).getY() + getY()
							+ itemSize)
				return equip.getValue();
		}

		return null;
	}

	@Override
	protected void mouse() {
		// TODO Auto-generated method stub

	}

	int getKey() {
		return Keyboard.KEY_E;
	}

}
