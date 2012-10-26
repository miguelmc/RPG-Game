package game.ui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.Main;
import game.entities.NPC;
import game.util.Util;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Serves as an interface for sending messages to the player in a textbox. The
 * player stops its actions while the message box is active. Can send an "Ok"
 * messagebox or a "Yes/No"
 */
public class MsgBoxManager
{
	
	//TODO have a runnable instead of automatically running a given script or having a method

	private static String message = "";
	private static boolean selection = true; // only for "Yes/No" messagebox
	private static boolean yesNo = false;
	private static boolean active = false;
	private static int stateYes;
	private static int stateNo;
	private static int state; // the state send to the npc script.
	private static boolean callNPC = true;
	private static Method methodCall;
	private static Object object;

	public static void render()
	{
		
		if (isActive())
		{
			int boxHeight = 140;
			int x1 = 20;
			int x2 = Main.DIM.getWidth() - 20;
			int y1 = Main.DIM.getHeight() - 25 - boxHeight;
			int y2 = Main.DIM.getHeight() - 25;

			GL11.glColor4f(0f, 0f, 0f, 0.6f);
			glBegin(GL_QUADS);
			glVertex2f(x1, y1);
			glVertex2f(x2, y1);
			glVertex2f(x2, y2);
			glVertex2f(x1, y2);
			glEnd();

			GL11.glColor4f(1f, 1f, 1f, 0.3f);
			glBegin(GL_QUADS);
			glVertex2f(x1 + 5, y1 + 5);
			glVertex2f(x2 - 5, y1 + 5);
			glVertex2f(x2 - 5, y2 - 5);
			glVertex2f(x1 + 5, y2 - 5);
			glEnd();

			Util.useFont("Monaco", Font.PLAIN, 25, Color.white);
			String lines[] = Util.tokenizeText(message, Main.DIM.getWidth() - 50, 4);
			for (int i = 0; i < lines.length; i++)
			{
				if (!lines[i].equals(""))
				{
					Util.write(lines[i], x1 + 15, y1 + 10 + Util.getFontHeight() * i);
				}
			}

			if (yesNo)
			{
				x1 = (int) (Main.DIM.getWidth() * .7);
				x2 = (int) (Main.DIM.getWidth() * .93);
				y1 = y2 - 20;
				y2 = y2 + 25;
				GL11.glColor4f(0f, 0f, 0f, .7f);
				glBegin(GL_QUADS);
				glVertex2f(x1, y1);
				glVertex2f(x2, y1);
				glVertex2f(x2, y2);
				glVertex2f(x1, y2);
				glEnd();

				GL11.glColor4f(1f, 1f, 1f, .4f);
				glBegin(GL_QUADS);
				glVertex2f(x1 + 5, y1 + 5);
				glVertex2f(x2 - 5, y1 + 5);
				glVertex2f(x2 - 5, y2 - 5);
				glVertex2f(x1 + 5, y2 - 5);
				glEnd();

				GL11.glDisable(GL11.GL_TEXTURE_2D);

				int avgx = (int) ((x1 + x2) / 2);

				int translate = 0;
				if (!selection)
				{
					translate = avgx - x1 - 4;
				}

				GL11.glColor4f(1f, 1f, 0f, .55f);
				glBegin(GL_QUADS);
				glVertex2f(x1 + 5 + translate, y1 + 5);
				glVertex2f(avgx + translate, y1 + 5);
				glVertex2f(avgx + translate, y2 - 5);
				glVertex2f(x1 + 5 + translate, y2 - 5);
				glEnd();

				Util.write("Yes", x1 + 10, y1 + 5);
				Util.write("No", x1 + 88, y1 + 5);
			}
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
	}

	/**
	 * 
	 * <br>
	 * <b>sendText</b> <br>
	 * <p>
	 * <tt>public static void sendText(String str, boolean YesNo)</tt>
	 * </p>
	 * Creates a message box. An "Ok" messagebox is sent if YesNo is false and a
	 * "Yes/No" messagebox if true. <br>
	 * <br>
	 */
	public static void sendText(String str, boolean YesNo)
	{
		message = str;
		yesNo = YesNo;
		active = true;
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
				state = -1;
				stateYes = -1;
				stateNo = -1;
			case Keyboard.KEY_SPACE: // space and enter do the same
			case Keyboard.KEY_RETURN:
				message = "";
				active = false;
				// runs a npc script passing a state. If the state is -1, the
				// messagebox is closed.
				if(callNPC){
					NPC.getNpc().run(yesNo ? (selection ? stateYes : stateNo) : state);
				}else{
					try {
						methodCall.invoke(object, selection);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				selection = true;
				break;
			case Keyboard.KEY_RIGHT:
				selection = false;
				break;
			case Keyboard.KEY_LEFT:
				selection = true;
				break;
			}
		}

	}

	public static void setYesNo(int yes, int no)
	{
		stateYes = yes;
		stateNo = no;
	}

	public static void setState(int state)
	{
		MsgBoxManager.state = state;
	}

	public static boolean getSelection()
	{
		return selection;
	}

	public static void setActive(boolean active)
	{
		MsgBoxManager.active = active;
	}

	public static void sendText(String text, boolean b, String method, Object object) {
		sendText(text, true);
		try {
			methodCall = object.getClass().getMethod(method, new Class<?>[]{Boolean.class});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		callNPC = false;
		MsgBoxManager.object = object;
	}

}
