package game.ui.window;

import game.Main;
import game.ui.Shop;
import game.util.MouseManager;
import game.util.Util;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;
import static org.lwjgl.opengl.GL11.*;

public class WindowManager {
	
	private static Map<Integer, Window> windows = new HashMap<Integer, Window>();
	private static Stack<Window> activeWindows = new Stack<Window>();
	private static Shop shop;
	
	static
	{
		windows.put(Keyboard.KEY_I, new Inventory());
		windows.put(Keyboard.KEY_K, new Skills());
		windows.put(Keyboard.KEY_E, new Equipment());
		windows.put(Keyboard.KEY_S, new Stats());
	}
	
	public static void openShop(int id)
	{
		shop = new Shop(id);
		activeWindows.clear();
	}
	
	public static void closeShop()
	{
		shop = null;
	}
	
	public static void render()
	{		
		for(Window window: activeWindows)
		{
			glColor4f(1, 1, 1, .65f);
			window.render();
		}
			
		glColor4f(1, 1, 1, 1);

		
		if(shop != null)
			shop.render();
	}
	
	public static void keyboard()
	{
		if (Keyboard.getEventKeyState())// key released
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				if(shop != null)
					closeShop();
				else if(!activeWindows.isEmpty())
				{
					activeWindows.lastElement().onClose();
					activeWindows.pop();
				}
			}else
			{
				Window window = windows.get(Keyboard.getEventKey());
				if(window != null)
				{
					if(activeWindows.contains(window))
					{
						window.onClose();
						activeWindows.remove(window);
					}else 
					{
						activeWindows.push(window);
						if(!Util.inRange(window.getPosition(), new Point(0, 0), Main.DIM))
							window.resetPosition();
					}
				}
			}
		}
	}
	
	public static void mouse()
	{
		if(activeWindows.isEmpty())
			return;
		
		if(isShopOpen())
			shop.mouseInput();
		else if(Util.inRange(MouseManager.getPosition(), activeWindows.lastElement().getPosition(), activeWindows.lastElement().size))
		{
			activeWindows.lastElement().mouse();
		}else if(MouseManager.mousePressed()) //if the event is a click
		{
			ListIterator<Window> it = activeWindows.listIterator(activeWindows.size()-1); //check for every window in order to give focus if one is clicked
			while(it.hasPrevious())
			{
				Window window = it.previous();
				if(Util.inRange(MouseManager.getPosition(), window.getPosition(), window.size)) //if the window is clicked
				{
					activeWindows.remove(window); //send it to the top
					activeWindows.push(window);
					window.mouse(); //give mouse focus to the window
					break; //terminate the loop
				}
			}
		}
	}

	public static boolean isShopOpen() {
		return shop != null;
	}
	
}
