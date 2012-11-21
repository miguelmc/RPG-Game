package game.menu;

import game.Main;
import game.util.MouseManager;
import game.util.Writer;
import game.util.Writer.Fonts;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;

public class MainMenu {
	
	private static boolean blink = false;
	private static long next = 0;

	public static void input()
	{
		while(Keyboard.next())
		{
			Main.setScene(Main.GAME);
		}
		
		while(MouseManager.hasEvent())
		{
			
		}
	}
	
	public static void update()
	{
	}
	
	public static void render()
	{
		if(System.currentTimeMillis() > next)
		{
			blink = !blink;
			next = System.currentTimeMillis() + 550;
		}
		
		Writer.useFont(Fonts.Monaco_White_Plain_25);
		Writer.write("MAIN MENU", new Point(Main.DIM.getWidth()/2, Main.DIM.getHeight()/2 - 40), Writer.CENTER);
		Writer.write("Press any key to continue...", new Point(Main.DIM.getWidth()/2, Main.DIM.getHeight()/2), Writer.CENTER);
		if(blink)
			Writer.write("|", new Point(582, 375));
	}
}
