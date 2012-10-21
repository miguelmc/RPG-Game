package game.ui.window;

import static game.entities.superentities.Player.TOTAL;
import static game.features.Stat.ATK;
import static game.features.Stat.DEF;
import static game.features.Stat.MAXHP;
import static game.features.Stat.MAXMP;
import static game.features.Stat.STR;
import game.Main;
import game.entities.item.Item;
import game.entities.superentities.Player;
import game.structure.MapManager;
import game.util.Util;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Stats extends Window{
	
	public static final int X = 105, Y=61, Y_INTERVAL[] = new int[8];
	
	static
	{
		for(int i=0; i< Y_INTERVAL.length; i++)
		{
			Y_INTERVAL[i] = (int) (Y + 24.5*i);
		}
	}
	
	public Stats ()
	{
		super(new Point(100, 100), new Dimension(174, 256));
	}
	
	public void render()
	{
		super.render();
		Player player = MapManager.getMap().getPlayer();
		Util.useFont("Courier New", Font.BOLD, 14, Color.white);
		Util.write(Integer.toString(player.getLevel()), getX()+X, getY()+Y_INTERVAL[0]);
		Util.write(player.getExp() + "/" + player.getExpReq(), getX()+X, getY()+Y_INTERVAL[1]);
		Util.write(Integer.toString(player.getStat(TOTAL+MAXHP.ID)), getX()+X, getY()+Y_INTERVAL[2]);
		Util.write(Integer.toString(player.getStat(TOTAL+MAXMP.ID)), getX()+X, getY()+Y_INTERVAL[3]);
		Util.write(Integer.toString(player.getStat(TOTAL+ATK.ID)), getX()+X, getY()+Y_INTERVAL[4]);
		Util.write(Integer.toString(player.getStat(TOTAL+DEF.ID)), getX()+X, getY()+Y_INTERVAL[5]);
		Util.write(Integer.toString(player.getStat(TOTAL+STR.ID)), getX()+X, getY()+Y_INTERVAL[6]);
		Util.write(Integer.toString((int)(player.getAverageDamage()+.5)), getX()+X, getY()+Y_INTERVAL[7]);
	}
	
	
	@Override
	protected void mouse() {
		if (Mouse.getEventButtonState())
		{
			setPressed(true);
		} else if (!Mouse.getEventButtonState())
		{
			int dX = Mouse.getDX();
			int dY = Mouse.getDY();

			if (Mouse.isButtonDown(0) && isPressed()) {

				if (!((dX > 0 && getX() > Main.DIM.getWidth() * .98)
						|| (dX < 0 && getX() + getWidth() < Main.DIM.getWidth() * .02)
						|| (dY > 0 && getY() < Main.DIM.getHeight() * .02) || (dY < 0 && getY() > Main.DIM
						.getHeight() * .98))) {
					setPosition(getX() + dX, getY() - dY);
					}
			} else if (dX == 0 && dY == 0) {
				setPressed(false);
			}

		}

	}
	
	int getKey()
	{
		return Keyboard.KEY_S;
	}

}
