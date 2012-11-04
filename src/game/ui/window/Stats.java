package game.ui.window;

import static game.entities.superentities.Player.TOTAL;
import static game.features.Stat.ATK;
import static game.features.Stat.DEF;
import static game.features.Stat.MAXHP;
import static game.features.Stat.MAXMP;
import static game.features.Stat.STR;
import game.Main;
import game.entities.superentities.Player;
import game.structure.MapManager;
import game.util.Writer;
import game.util.Writer.Fonts;

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
		super(new Point(100, 450), new Dimension(174, 256));
	}
	
	public void render()
	{
		super.render();
		Player player = MapManager.getMap().getPlayer();
		Writer.useFont(Fonts.Courier_White_Bold_14);
		Writer.write(Integer.toString(player.getLevel()), new Point(getX()+X, getY()+Y_INTERVAL[0]));
		Writer.write(player.getExp() + "/" + player.getExpReq(), new Point(getX()+X, getY()+Y_INTERVAL[1]));
		Writer.write(Integer.toString(player.getStat(TOTAL+MAXHP.ID)), new Point(getX()+X, getY()+Y_INTERVAL[2]));
		Writer.write(Integer.toString(player.getStat(TOTAL+MAXMP.ID)), new Point(getX()+X, getY()+Y_INTERVAL[3]));
		Writer.write(Integer.toString(player.getStat(TOTAL+ATK.ID)), new Point(getX()+X, getY()+Y_INTERVAL[4]));
		Writer.write(Integer.toString(player.getStat(TOTAL+DEF.ID)), new Point(getX()+X, getY()+Y_INTERVAL[5]));
		Writer.write(Integer.toString(player.getStat(TOTAL+STR.ID)), new Point(getX()+X, getY()+Y_INTERVAL[6]));
		Writer.write(Integer.toString((int)(player.getAverageDamage()+.5)), new Point(getX()+X, getY()+Y_INTERVAL[7]));
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
						|| (dX < 0 && getX() + SIZE.getWidth() < Main.DIM.getWidth() * .02)
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
