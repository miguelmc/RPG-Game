package game.ui;

import game.Main;
import game.util.Renderer;
import game.util.Writer;
import game.util.Writer.Fonts;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

/**
 * Serves as an interface for sending messages to the player in a textbox. The
 * player stops its actions while the message box is active. Can send an "Ok"
 * messagebox or a "Yes/No"
 */
public class MsgBoxManager
{
	
	public static final int OK = 0, YES_NO = 1, YES = 2, NO = 3, ABORTED = 4;
	
	private static int answer = 0; // only for "Yes/No" messagebox
	private static int type = OK;
	private static boolean active;
	private static Runnable action;
	
	private static final Dimension BOX_SIZE, INNER_BOX_SIZE, OPTION_BOX_SIZE, HIGHLIGHT_SIZE;
	private static final Point BOX_POSITION, INNER_BOX_POSITION, OPTION_BOX_POSITION;
	
	private static List<String> paragraph;
	private static Point highlightPos;

	static
	{
		//initializes all coordinates/dimensions
		BOX_SIZE = new Dimension(Main.DIM.getWidth() - 40, 140);
		BOX_POSITION = new Point(20, Main.DIM.getHeight() - 25 - BOX_SIZE.getHeight());
		INNER_BOX_SIZE = new Dimension(BOX_SIZE.getWidth() - 10, BOX_SIZE.getHeight() - 10);
		INNER_BOX_POSITION = new Point(BOX_POSITION.getX() + 5, BOX_POSITION.getY() + 5);
		OPTION_BOX_SIZE = new Dimension(Main.DIM.getWidth()/4, 45);
		OPTION_BOX_POSITION = new Point((int)(Main.DIM.getHeight()*.7), Main.DIM.getHeight() - 50);
		HIGHLIGHT_SIZE = new Dimension((OPTION_BOX_SIZE.getWidth()-10)/2, OPTION_BOX_SIZE.getHeight()-10);
		highlightPos = new Point(OPTION_BOX_POSITION.getX() + 5, OPTION_BOX_POSITION.getY() + 5);
	}
	
	public static void render()
	{		

		if(!active)
			return;
		
		GL11.glColor4f(0f, 0f, 0f, 0.6f);
		Renderer.renderQuad(BOX_POSITION, BOX_SIZE); // renders the background
		
		GL11.glColor4f(1f, 1f, 1f, 0.3f); // inner rectangle with different color
		Renderer.renderQuad(INNER_BOX_POSITION, 
							INNER_BOX_SIZE);
			
		//writes the text
		
		Writer.useFont(Fonts.Monaco_White_Plain_25);
		Writer.write(paragraph, new Point(BOX_POSITION.getX() + 15, BOX_POSITION.getY() + 10), 4);

		if (type == YES_NO)
		{			
			GL11.glColor4f(0f, 0f, 0f, .7f); //renders the optionBox
			Renderer.renderQuad(OPTION_BOX_POSITION, OPTION_BOX_SIZE);
			
			GL11.glColor4f(1f, 1f, 0f, .55f); // renders the option selection highlight
			Renderer.renderQuad(highlightPos, HIGHLIGHT_SIZE);
			
			//Writes the yes/no option
			Writer.write("YES", new Point(OPTION_BOX_POSITION.getX() + 25, OPTION_BOX_POSITION.getY() + 5));
			Writer.write("NO", new Point(OPTION_BOX_POSITION.getX() + (OPTION_BOX_SIZE.getWidth() - 10)/2 + 35,  OPTION_BOX_POSITION.getY() + 5));
		}
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		
	}

	public static void sendMessage(String message, int type, Runnable runnable)
	{
		active = true;
		Writer.useFont(Fonts.Monaco_White_Plain_25);
		paragraph = Writer.toParagraph(message, Main.DIM.getWidth() - 65);
		MsgBoxManager.type = type;
		action = runnable;	
		answer = YES;
		highlightPos.setX(OPTION_BOX_POSITION.getX() + 5);
	}
	
	public static void sendMessage(String message, int type)
	{
		sendMessage(message, type, null);
	}
	
	public static boolean isActive()
	{
		return active;
	}

	public static void input()
	{ // the keyboard events are only received if it is active
		if (Keyboard.getEventKeyState())
		{
			switch (Keyboard.getEventKey())
			{
			case Keyboard.KEY_ESCAPE:
				answer = ABORTED;
			case Keyboard.KEY_SPACE:
			case Keyboard.KEY_RETURN:
				active = false;
				if(action != null)
					action.run();
				break;
			case Keyboard.KEY_RIGHT:
				answer = NO;
				highlightPos.setX(OPTION_BOX_POSITION.getX() + 5 + (OPTION_BOX_SIZE.getWidth() - 10)/2);
				break;
			case Keyboard.KEY_LEFT:
				answer = YES;
				highlightPos.setX(OPTION_BOX_POSITION.getX() + 5);
				break;
			}
		}

	}

	public static int getAnswer()
	{
		return answer;
	}

}
