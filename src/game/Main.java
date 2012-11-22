package game;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import game.menu.MainMenu;
import game.menu.Settings;
import game.structure.MapManager;
import game.structure.Slot;
import game.ui.MsgBoxManager;
import game.ui.UserInterface;
import game.ui.window.WindowManager;
import game.util.MouseManager;
import game.util.SoundManager;
import game.util.Writer;
import game.util.XMLParser;
import game.util.Writer.Fonts;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

/**
 * Game launcher class. Contains the main loop and initializes the game.
 */
public class Main {

	public static String NAME = "Game";
	public static Dimension GRIDSIZE, DIM;
	
	public static boolean DEBUG = false;
	
	private static int state = 0;
	public static final int MAIN_MENU = 0, SETTINGS = 2, GAME = 3, DEAD = 4;
	static // static initializer
	{
		XMLParser parser = new XMLParser("game_config.xml");

		NAME = parser.getAttribute("GAME", "name");
		GRIDSIZE = new Dimension(Integer.parseInt(parser.getAttribute("Game", "width")),
								 Integer.parseInt(parser.getAttribute("Game", "height")));
		Slot.SIZE = Integer.parseInt(parser.getAttribute("Game", "tile_size")
				.replace("px", ""));

		DIM = new Dimension(Slot.SIZE * GRIDSIZE.getWidth(),
							Slot.SIZE * GRIDSIZE.getHeight());
	}

	public Main() {
		
		// create window
		try {
			Display.setDisplayMode(new DisplayMode(DIM.getWidth(), DIM.getHeight()));
			Display.setTitle(NAME);
			Display.create();
		} catch (LWJGLException e) {
			System.out.println("Unable to create display");
			System.exit(1);
		}

		SoundManager.initSounds();
		initGL();
		
		boolean blink = false;
		long next = 0;
		// Game Loop
		while (!Display.isCloseRequested())// as long as close button is not pressed
		{
			glClear(GL_COLOR_BUFFER_BIT); // clears the screen
			
			switch(state)
			{
			case MAIN_MENU:
				MainMenu.input();
				MainMenu.update();
				MainMenu.render();
				break;
			case SETTINGS:
				Settings.input();
				Settings.update();
				Settings.render();
				break;
			case GAME:
				input(); //handles mouse and keyboard events
				MapManager.update(); // updates the current map
				
				MapManager.render(); // render the active map
				UserInterface.render(); // renders the interface
				break;
			case DEAD:
				
				while(Keyboard.next())
				{
					Main.setScene(Main.GAME);
				}
				if(System.currentTimeMillis() > next)
				{
					blink = !blink;
					next = System.currentTimeMillis() + 550;
				}
				
				Writer.useFont(Fonts.Monaco_White_Plain_25);
				Writer.write("YOU ARE DEAD", new Point(Main.DIM.getWidth()/2, Main.DIM.getHeight()/2 - 40), Writer.CENTER);
				Writer.write("Press any key to continue...", new Point(Main.DIM.getWidth()/2, Main.DIM.getHeight()/2), Writer.CENTER);
				if(blink)
					Writer.write("|", new Point(582, 375));
				break;
			}
			
			Display.update(); // update the screen
			Display.sync(100); // set fps to 60
		}
		
		org.lwjgl.openal.AL.destroy();
		Display.destroy();
		System.exit(0);
	}

	private void initGL() {
		// init GL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, DIM.getWidth(), DIM.getHeight(), 0, 1, -1); // set origin to upper-left corner
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND); // enable transparency
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // enable transparency
	}

	private void input() {
		while (Keyboard.next())
		{
			if (MsgBoxManager.isActive()) {
				MsgBoxManager.input();
			} 
			else
			{
				WindowManager.keyboard();
				if(!WindowManager.isShopOpen())
					MapManager.input();
			}
		}

		while (MouseManager.hasEvent())
			WindowManager.mouse();

	}

	public static void main(String[] args) {
		
		for(String arg: args)
			if(arg.equals("d"))
				DEBUG = true;
		new Main();
	}

	public static void setScene(int scene)
	{
		state = scene;
	}
	
}
