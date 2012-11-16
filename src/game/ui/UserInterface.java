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

public class UserInterface
{

	private static final UserInterface ui = new UserInterface();
	private static final Point NOTIFICATIONS_POS = new Point(10, 70);
	private static Queue<Notification> notifications = new LinkedList<Notification>();
	private static Queue<Notification> importantNotifications = new LinkedList<Notification>();
		
	public static void render()
	{
		Writer.useFont(Fonts.Arial_Black_Bold_14);
		renderNotifications(notifications, NOTIFICATIONS_POS, Writer.LEFT);
		
		Writer.useFont(Fonts.Monaco_White_Plain_25);
		renderNotifications(importantNotifications, new Point(Main.DIM.getWidth()/2, Main.DIM.getHeight()/4), Writer.CENTER);
		
		WindowManager.render();
		
		Writer.useFont(Fonts.Arial_Black_Bold_14);
		//TODO use Renderer
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
		limit = (int) (width * MapManager.getMap().getPlayer().getExp() / MapManager.getMap().getPlayer().getReqExp());
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
		
		//Writes exp/maxexp on EXP bar
		Writer.useFont(Fonts.Arial_Black_Bold_14);
		Writer.write(MapManager.getMap().getPlayer().getExp() + "/" + MapManager.getMap().getPlayer().getReqExp(),
				new Point(390+(width/2) - 20 , 13));
		
		glColor4f(0f, 0f, 0f, 1f);

		Writer.useFont(Fonts.Courier_White_Bold_28);
		Writer.write(MapManager.getMap().getName(), new Point(10, 40));
		Writer.write(Integer.toString(MapManager.getMap().getPlayer().getLevel()), 
				new Point(Main.DIM.getWidth()-10, 5), Writer.RIGHT);

		if (MsgBoxManager.isActive())
			MsgBoxManager.render();

	}
	
	private static void renderNotifications(Queue<Notification> notifications2, Point position, int alignment)
	{
		Iterator<Notification> it = notifications2.iterator();
		
		while(it.hasNext() && it.next().died())
			it.remove();
		
		it = notifications2.iterator();
		
		for(int i=notifications2.size()-1; it.hasNext(); i--)
			Writer.write(it.next().notification, new Point(position.getX(), position.getY() + i*Writer.fontHeight()), alignment);
	}

	public static void sendNotification(String message)
	{
		notifications.add(ui.new Notification(message,2200));
	}
	
	public static void sendImpNotification(String message)
	{
		importantNotifications.add(ui.new Notification(message, 3200));
	}
	
	class Notification{
		private String notification;
		private Long creationTime;
		private final int lifeTime;
		
		private boolean died()
		{
			return System.currentTimeMillis() > creationTime + lifeTime;
		}
		
		public Notification(String message, int lifeTime)
		{
			notification = message;
			creationTime = System.currentTimeMillis();
			this.lifeTime = lifeTime;
		}
		
	}

}
