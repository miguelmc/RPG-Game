package game.ui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.Main;
import game.entities.superentities.Player;
import game.features.Stat;
import game.structure.MapManager;
import game.ui.window.Window;
import game.util.Util;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

// TODO change the list to a queue
// TODO? use notifications timing in a separate thread?
/**
 * Manages everything related to the user interface. Renders the windows and
 * serves as an interface to send simple notifications. Also loads the fonts.
 */
public class UserInterface
{

	private static ArrayList<String> notifications = new ArrayList<String>();
	private static ArrayList<Long> times = new ArrayList<Long>(); // TODO change to a queue

	static
	{
		// load fonts
		System.out.println("Loading 0%");
		Util.useFont("Courier New", Font.BOLD, 14, Color.white);
		System.out.println("Loading 20%");
		Util.useFont("Monaco", Font.PLAIN, 25, Color.white);
		System.out.println("Loading 40%");
		Util.useFont("Arial", Font.BOLD, 10, Color.white);
		System.out.println("Loading 60%");
		Util.useFont("Arial", Font.BOLD, 14, Color.black);
		System.out.println("Loading 80%");
		Util.useFont("Courier New", Font.BOLD, 28, Color.white);
		System.out.println("Loading 100%");

	}
	
	public static void render()
	{
		// renders all notifications
		Util.useFont("Arial", Font.BOLD, 14, Color.black);
		for (int i = notifications.size() - 1; i >= 0; i--)
		{
			if (times.get(i) > System.currentTimeMillis())
			{
				Util.write(notifications.get(i), 10, 80 + i * Util.getFontHeight());
			} else
			{
				notifications.remove(i);
				times.remove(i);
				i--;
			}
		}

		Window.renderAll();

		// HP BAR
		int width = 128;
		int limit = (int) (width * MapManager.getMap().getPlayer().getHP() / MapManager.getMap().getPlayer()
				.getStat(Player.TOTAL + Stat.MAXHP.ID));
		glColor4f(1f, 0f, 0f, .6f);
		glBegin(GL_QUADS);
		glVertex2f(10, 10);
		glVertex2f(10 + limit, 10);
		glVertex2f(10 + limit, 32);
		glVertex2f(10, 32);
		glEnd();
		glColor4f(.7f, .7f, .7f, .6f);
		glBegin(GL_QUADS);
		glVertex2f(10 + limit, 10);
		glVertex2f(10 + width, 10);
		glVertex2f(10 + width, 32);
		glVertex2f(10 + limit, 32);
		glEnd();
		glColor4f(0f, 0f, 0f, 1f);

		// MP BAR
		limit = (int) (width * MapManager.getMap().getPlayer().getMP() / MapManager.getMap().getPlayer()
				.getStat(Player.TOTAL + Stat.MAXMP.ID));
		glColor4f(0f, 0f, 1f, .6f);
		glBegin(GL_QUADS);
		glVertex2f(200, 10);
		glVertex2f(200 + limit, 10);
		glVertex2f(200 + limit, 32);
		glVertex2f(200, 32);
		glEnd();
		glColor4f(.7f, .7f, .7f, .6f);
		glBegin(GL_QUADS);
		glVertex2f(200 + limit, 10);
		glVertex2f(200 + width, 10);
		glVertex2f(200 + width, 32);
		glVertex2f(200 + limit, 32);
		glEnd();
		glColor4f(0f, 0f, 0f, 1f);

		// EXP BAR
		limit = (int) (width * MapManager.getMap().getPlayer().getExp() / MapManager.getMap().getPlayer().getExpReq());
		glColor4f(1f, .5f, 0f, .6f);
		glBegin(GL_QUADS);
		glVertex2f(390, 10);
		glVertex2f(390 + limit, 10);
		glVertex2f(390 + limit, 32);
		glVertex2f(390, 32);
		glEnd();
		glColor4f(.7f, .7f, .7f, .6f);
		glBegin(GL_QUADS);
		glVertex2f(390 + limit, 10);
		glVertex2f(390 + width, 10);
		glVertex2f(390 + width, 32);
		glVertex2f(390 + limit, 32);
		glEnd();
		glColor4f(0f, 0f, 0f, 1f);

		Util.useFont("Courier New", Font.BOLD, 28, Color.white);
		Util.write(MapManager.getMap().getName(), 10, 40);
		Util.write(Integer.toString(MapManager.getMap().getPlayer().getLevel()), (float) (Main.DIM.getWidth() - 30), 5f);

		if (MsgBoxManager.isActive())
			MsgBoxManager.render();

	}

	/**
	 * 
	 * <br>
	 * <b>sendNotification</b>
	 * <br>
	 * <p>
	 * <tt>public static void sendNotification(String s)</tt>
	 * </p>
	 * Send a notification to the screen which dissappears after several seconds.
	 * <br><br>
	 */
	public static void sendNotification(String s)
	{
		notifications.add(s);
		times.add(System.currentTimeMillis() + 2000L);
	}

	

}
