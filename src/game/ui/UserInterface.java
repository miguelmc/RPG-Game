package game.ui;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.Main;
import game.entities.superentities.Player;
import game.features.Stat;
import game.structure.MapManager;
import game.ui.window.WindowManager;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.util.Point;

// TODO change the list to a queue
// TODO? use notifications timing in a separate thread?
/**
 * Manages everything related to the user interface. Renders the windows and
 * serves as an interface to send simple notifications. Also loads the fonts.
 */
public class UserInterface
{

	private static final UserInterface ui = new UserInterface();
	private static final Point NOTIFICATIONS_POS = new Point(10, 70);
	
	class Notification{
		private String notification;
		private Long creationTime;
		private final long lifeTime = 2000L;
		
		private boolean died()
		{
			return System.currentTimeMillis() > creationTime + lifeTime;
		}
		
		public Notification(String message)
		{
			notification = message;
			creationTime = System.currentTimeMillis();
		}
	}
	
	private static Queue<Notification> notifications = new LinkedList<Notification>();
	
	public static void render()
	{
		
		// renders all notifications
		Writer.useFont(Fonts.Arial_Black_Bold_14);
		
		Iterator<Notification> it = notifications.iterator();
		
		while(it.hasNext() && it.next().died())
			it.remove();
		
		it = notifications.iterator();
		
		for(int i=notifications.size()-1; it.hasNext(); i--)
			Writer.write(it.next().notification, new Point(NOTIFICATIONS_POS.getX(), NOTIFICATIONS_POS.getY() + i*Writer.fontHeight()));
		
		
		WindowManager.render();
		Writer.useFont(Fonts.Arial_Black_Bold_14);
		
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
		
		glColor3f(0f, 0f, 0f);
		glLoadIdentity();
		glLineWidth(2);
		glBegin(GL_LINES);
		glVertex2f((float) (10), (float) (10));
		glVertex2f((float) (10+width), (float) (10));
		glVertex2f((float) (10+width), (float) (10));
		glVertex2f((float) (10+width), (float) (32));
		glVertex2f((float) (10+width), (float) (32));
		glVertex2f((float) (10), (float) (32));		
		glVertex2f((float) (10), (float) (32));
		glVertex2f((float) (10), (float) (10));
		glEnd();
		glLoadIdentity();

		//Writes hp/maxhp on HP bar
		
		Writer.write(MapManager.getMap().getPlayer().getHP() + "/" + MapManager.getMap().getPlayer()
				.getStat(Player.TOTAL + Stat.MAXHP.ID), new Point(10+(width/2) - 20 , 13));
		
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

		glColor3f(0f, 0f, 0f);
		glLoadIdentity();
		glLineWidth(2);
		glBegin(GL_LINES);
		glVertex2f((float) (200), (float) (10));
		glVertex2f((float) (200+width), (float) (10));
		glVertex2f((float) (200+width), (float) (10));
		glVertex2f((float) (200+width), (float) (32));
		glVertex2f((float) (200+width), (float) (32));
		glVertex2f((float) (200), (float) (32));		
		glVertex2f((float) (200), (float) (32));
		glVertex2f((float) (200), (float) (10));
		glEnd();
		glLoadIdentity();
		
		//Writes mp/maxmp on MP bar
		Writer.useFont(Fonts.Courier_White_Bold_14);
		Writer.write(MapManager.getMap().getPlayer().getMP() + "/" + MapManager.getMap().getPlayer()
						.getStat(Player.TOTAL + Stat.MAXMP.ID), new Point(200+(width/2) - 20 , 13));	
		
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
		
		glColor3f(0f, 0f, 0f);
		glLoadIdentity();
		glLineWidth(2);
		glBegin(GL_LINES);
		glVertex2f((float) (390), (float) (10));
		glVertex2f((float) (390+width), (float) (10));
		glVertex2f((float) (390+width), (float) (10));
		glVertex2f((float) (390+width), (float) (32));
		glVertex2f((float) (390+width), (float) (32));
		glVertex2f((float) (390), (float) (32));		
		glVertex2f((float) (390), (float) (32));
		glVertex2f((float) (390), (float) (10));
		glEnd();
		glLoadIdentity();
		
		//Writes xp/maxxp on XP bar
		Writer.useFont(Fonts.Arial_Black_Bold_14);
		Writer.write(MapManager.getMap().getPlayer().getExp() + "/" + MapManager.getMap().getPlayer().getExpReq(),
				new Point(390+(width/2) - 20 , 13));
		
		glColor4f(0f, 0f, 0f, 1f);

		Writer.useFont(Fonts.Courier_White_Bold_28);
		Writer.write(MapManager.getMap().getName(), new Point(10, 40));
		Writer.write(Integer.toString(MapManager.getMap().getPlayer().getLevel()), 
				new Point(Main.DIM.getWidth()-10, 5), Writer.RIGHT);

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
	 * Send a notification to the screen which disappears after several seconds.
	 * <br><br>
	 */
	public static void sendNotification(String message)
	{
		notifications.add(ui.new Notification(message));
	}

}
