package game.ui;

import static org.lwjgl.opengl.GL11.glColor4f;
import game.Main;
import game.entities.superentities.Player;
import game.features.Stat;
import game.structure.MapManager;
import game.ui.window.WindowManager;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Util;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class UserInterface
{

	private static final UserInterface ui = new UserInterface();
	private static final Point NOTIFICATIONS_POS = new Point(10, 70);
	private static Queue<Notification> notifications = new LinkedList<Notification>();
	private static Queue<Notification> importantNotifications = new LinkedList<Notification>();
	private static final Dimension BAR_SIZE = new Dimension(148, 25), LEVEL_UP_SIZE = new Dimension(440, 110), SHORTCUTS_SIZE;
	private static final Point HP_POS = new Point(10, 10), MP_POS = new Point(250, 10), EXP_POS = new Point(490, 10),
							   MAP_NAME_POS = new Point(10, 40), LEVEL_POS = new Point(Main.DIM.getWidth()-10, 5),
							   LEVEL_UP_POS, SHORTCUTS_POS;
	private static final Texture levelUpTexture, skillShortcuts;
	private static boolean levelUp = false;
	private static long levelUpTime;
	private static final int LEVEL_UP_DURATION = 3500;
		
	static
	{
		levelUpTexture = Util.getTexture("UI/levelUp.png");
		LEVEL_UP_POS = new Point(Main.DIM.getWidth()/2, (int)(Main.DIM.getHeight()*.3));
		
		SHORTCUTS_SIZE = new Dimension(384, 84);
		SHORTCUTS_POS = new Point(Main.DIM.getWidth()/2 - SHORTCUTS_SIZE.getWidth()/2, Main.DIM.getHeight() - SHORTCUTS_SIZE.getHeight());
	
	
		skillShortcuts = Util.getTexture("UI/skillShortcuts.png");
	}
	
	public static void render()
	{
		WindowManager.render();
		
		Writer.useFont(Fonts.Arial_Black_Bold_14);
		renderNotifications(notifications, NOTIFICATIONS_POS, Writer.LEFT);
		
		Writer.useFont(Fonts.Monaco_White_Plain_25);
		renderNotifications(importantNotifications, new Point(Main.DIM.getWidth()/2, Main.DIM.getHeight()/4), Writer.CENTER);
			
		glColor4f(1, 1, 1, .5f); //border

		Renderer.render(new Builder(skillShortcuts, SHORTCUTS_POS, SHORTCUTS_SIZE).imageSize(256, 56));
		glColor4f(1, 1, 1, 1); //border

		Player player = MapManager.getMap().getPlayer();
		
		renderBar(HP_POS, new float[]{1, 0, 0}, player.getHP(), player.getStat(Player.TOTAL + Stat.MAXHP.ID));
		renderBar(MP_POS, new float[]{0, 0, 1}, player.getMP(), player.getStat(Player.TOTAL + Stat.MAXMP.ID));
		renderBar(EXP_POS, new float[]{1, .5f, 0}, player.getExp(), player.getReqExp());
		
		Writer.useFont(Fonts.Courier_White_Bold_28);
		Writer.write(MapManager.getMap().getName(), MAP_NAME_POS);
		
		Writer.write("Level: " + player.getLevel(), LEVEL_POS , Writer.RIGHT);

		if(levelUp) //renders the level up animation
		{
			
			float scale = (float) (((System.currentTimeMillis() - levelUpTime)) / (LEVEL_UP_DURATION *.25));
			if(scale > 1) scale = 1;
						
			Renderer.render(new Builder(levelUpTexture, 
										new Point((int) (LEVEL_UP_POS.getX() - LEVEL_UP_SIZE.getWidth()*.5*scale),
												  (int) (LEVEL_UP_POS.getY() - LEVEL_UP_SIZE.getHeight()*.5*scale)), 
										new Dimension((int) (LEVEL_UP_SIZE.getWidth()*scale), 
													  (int) (LEVEL_UP_SIZE.getHeight()*scale))));
			if(System.currentTimeMillis() > levelUpTime + LEVEL_UP_DURATION)
				levelUp = false;
		}
		
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
	
	private static void renderBar(Point position, float color[], int stat, int maxStat)
	{
		assert color.length == 3;
	
		float fillPercent = ((float)stat) / maxStat;
		
		glColor4f(color[0], color[1], color[2], .6f); //fill
		Renderer.renderQuad(position, new Dimension((int) (BAR_SIZE.getWidth()*fillPercent), BAR_SIZE.getHeight()));
		
		glColor4f(.7f, .7f, .7f, .6f); //background
		Renderer.renderQuad(new Point((int) (position.getX() + BAR_SIZE.getWidth()*fillPercent), position.getY()),
							new Dimension((int) (BAR_SIZE.getWidth() * (1 - fillPercent)), BAR_SIZE.getHeight()));
		
		glColor4f(0, 0, 0, 1); //border
		Renderer.renderBorder(2, position, BAR_SIZE);
		
		Writer.useFont(Fonts.Arial_Black_Bold_14); //stats
		Writer.write(stat + "/" + maxStat,
					 new Point(position.getX() + BAR_SIZE.getWidth()/2, position.getY() + BAR_SIZE.getHeight()/2 - Writer.fontHeight()/2),
					 Writer.CENTER);
		
		glColor4f(1, 1, 1, 1);
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

	public static void levelUp() {
		levelUp = true;
		levelUpTime = System.currentTimeMillis();
	}

}
