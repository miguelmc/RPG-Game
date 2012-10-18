package game.util;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.entities.superentities.SuperEntity;
import game.structure.Map;
import game.structure.Slot;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * <br>
 * Includes some useful methods for the game. <br>
 * <br>
 * 
 */
public class Util
{

	private static ArrayList<UnicodeFont> fonts = new ArrayList<UnicodeFont>();
	private static UnicodeFont currentFont;
	private static Color currentColor;

	/**
	 * <br>
	 * <b>useFont</b> <br>
	 * <p>
	 * <tt>public static void useFont(String fontName, int style, int size, Color c)</tt>
	 * </p>
	 * Sets the current font to the selected font. If the font already exists it
	 * is used, and if it doesn't it is created (might take 1-2 seconds). <br>
	 * <br>
	 * 
	 * @param fontName
	 *            - The name of the font to be used.
	 * @param style
	 *            - The style of the font to be used.
	 * @param size
	 *            - The style of the font to be used.
	 * @param c
	 *            - The color of the font to be used.
	 * @throws SlickException
	 *             if the glyphs for the font are unable to load.
	 * @see java.awt.Font
	 * @see java.awt.Color
	 * @see #write
	 * @see #getFontHeight
	 * @see #getTextWidth
	 */
	@SuppressWarnings("unchecked")
	public static void useFont(String fontName, int style, int size, Color c)
	{

		boolean fontExists = false;
		for (UnicodeFont font : fonts)
		{
			if (font.getFont().getFontName().contains(fontName.replaceAll(" ", ""))
					&& font.getFont().getStyle() == style && font.getFont().getSize() == size)
			{
				fontExists = true;
				currentFont = font;
				if (currentColor != c)
				{
					currentFont.getEffects().add(new ColorEffect(c));
					try
					{
						currentFont.loadGlyphs();
					} catch (SlickException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		if (!fontExists)
		{
			UnicodeFont f = new UnicodeFont(new Font(fontName, style, size));
			f.addAsciiGlyphs();
			f.getEffects().add(new ColorEffect(c));
			try
			{
				f.loadGlyphs();
			} catch (SlickException e)
			{
				e.printStackTrace();
			}
			fonts.add(f);
			currentFont = f;
			currentColor = c;
		}
	}

	/**
	 * 
	 * <br>
	 * <b>write</b> <br>
	 * <p>
	 * <tt>	public static void write(String text, float x, float y)</tt>
	 * </p>
	 * Draws the String 'text' on the display at position '(x,y)' using the
	 * current font. <br>
	 * Use <tt>useFont</tt> to change the current font. <br>
	 * <br>
	 * 
	 * @param text
	 *            - The string to be drawn.
	 * @param x
	 *            - Horizontal position in pixels to be drawn. (Origin at
	 *            upper-left corner)
	 * @param y
	 *            - Vertical position in pixels to be drawn. (Origin at
	 *            upper-left corner)
	 * @see #useFont
	 */
	public static void write(String text, float x, float y)
	{
		currentFont.drawString(x, y, text);
		GL11.glDisable(GL11.GL_TEXTURE_2D); // slick.UnicodeFont.drawString
											// enables GL_TEXTURE_2D but doesn't
											// disables it
	}

	/**
	 * <br>
	 * <b>getFontHeight</b> <br>
	 * <p>
	 * <tt>	public static int getFontHeight()</tt>
	 * </p>
	 * Returns the height in pixels of the String "Q" using the current font. <br>
	 * <br>
	 * 
	 * @return - Height in pixels.
	 * @see #useFont
	 */
	public static int getFontHeight()
	{
		return currentFont.getHeight("Q");
	}

	/**
	 * <br>
	 * <b>getTextWidth</b> <br>
	 * <p>
	 * <tt>public static int getTextWidth(String str)</tt>
	 * </p>
	 * Returns the width in pixels of the input String using the current font. <br>
	 * <br>
	 * 
	 * @return - Width in pixels.
	 * @see #useFont
	 */
	public static int getTextWidth(String str)
	{
		return currentFont.getWidth(str);
	}

	/**
	 * <br>
	 * <b>tokenizeText</b> <br>
	 * <p>
	 * <tt>public static String[] tokenizeText(String text, int widthLimit, int maxLines)</tt>
	 * </p>
	 * Return a String array of the input String splitted so that each line has
	 * a width less that <tt>widthLimit</tt> using the current font. <br>
	 * <br>
	 * 
	 * @param text
	 *            - input String to be tokenized.
	 * @param widthLimit
	 *            - Maximum width of each line.
	 * @param maxLines
	 *            - Maximum length of the returned array. The extra lines are
	 *            ignored.
	 * @return - String array of the tokenized text.
	 * @see #useFont
	 * @see #getTextWidth
	 */
	public static String[] tokenizeText(String text, int widthLimit, int maxLines)
	{
		ArrayList<String> tokens = new ArrayList<String>();

		while (text.contains(" "))
		{
			String token = text.substring(0, text.indexOf(' ') + 1);
			tokens.add(token);
			text = text.substring(text.indexOf(' ') + 1);
		}
		tokens.add(text);

		int lineLength = 0;
		ArrayList<String> lines2 = new ArrayList<String>();
		int tokenCounter = 0;
		int lineCounter = 0;

		while (tokenCounter < tokens.size())
		{
			String word = tokens.get(tokenCounter);
			int wordLength = getTextWidth(word) + 16;
			if (lineLength + wordLength > widthLimit)
			{
				if (lineCounter == maxLines - 1)
					break;
				lineLength = 0;
				lineCounter++;
			}
			if (lines2.size() <= lineCounter)
				lines2.add(word);
			else
				lines2.set(lineCounter, lines2.get(lineCounter) + word);
			lineLength += wordLength;
			tokenCounter++;
		}

		String tokenizedText[] = new String[lines2.size()];

		for (int i = 0; i < lines2.size(); i++)
		{
			if (lines2.get(i).charAt(lines2.get(i).length() - 1) == ' ')
				lines2.set(i, lines2.get(i).substring(0, lines2.get(i).length() - 1));
			tokenizedText[i] = lines2.get(i);
		}
		return tokenizedText;
	}

	/**
	 * <br>
	 * <b>getTexture</b> <br>
	 * <p>
	 * <tt>public static Texture getTexture(String path)</tt>
	 * </p>
	 * Returns the PNG image texture from the selected path from the resources
	 * folder. <br>
	 * <br>
	 * 
	 * @param path
	 *            - The path from the image (Do not include <tt>"/res"</tt> nor
	 *            <tt>".png"</tt>).
	 * @return - Texture
	 * @throws FileNotFoundExcepion
	 *             if the file <tt>"res/"+path+".png"</tt> is not found.
	 * @throws IOException
	 * @see org.newdawn.slick.opengl.Texture
	 */
	public static Texture getTexture(String path)
	{
		try
		{
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("data/" + path)));
		} catch (FileNotFoundException e)
		{
			System.out.println("File Not found: " + "data/" + path);
		} catch (IOException e)
		{
		}
		return null;
	}

	/**
	 * 
	 * <br>
	 * <b>addRelPoints</b> <br>
	 * <p>
	 * <tt>public static Point addRelPoints(Point p, Point p2, int facingDir)</tt>
	 * </p>
	 * Returns the position relative to a point facing certain direction.
	 * Example: to get the position of the slot in front of the player ->
	 * addRelPoints(player.position(), new Point(0, 1), player.getFacingDir()) <br>
	 * <br>
	 * 
	 * @param p
	 *            - The point which the position is relative to.
	 * @param p2
	 *            - The position relative to the point <i>p</i>.
	 * @param facingDir
	 *            - The relative direction of the position.
	 * @see com.game.etities.superentities#UP
	 * @see com.game.etities.superentities#RIGHT
	 * @see com.game.etities.superentities#DOWN
	 * @see com.game.etities.superentities#LEFT
	 */
	public static Point addRelPoints(Point p, Point p2, int facingDir)
	{
		switch (facingDir)
		{
		case SuperEntity.UP:
			return new Point(p.getX() + p2.getX(), p.getY() - p2.getY());
		case SuperEntity.RIGHT:
			return new Point(p.getX() + p2.getY(), p.getY() + p2.getX());
		case SuperEntity.DOWN:
			return new Point(p.getX() - p2.getX(), p.getY() + p2.getY());
		case SuperEntity.LEFT:
			return new Point(p.getX() - p2.getY(), p.getY() - p2.getX());
		}
		return null;
	}

	/**
	 * 
	 * <br>
	 * <b>render</b> <br>
	 * <p>
	 * <tt>public static void render(Texture texture, Point pos)</tt>
	 * </p>
	 * Renders the texture at position <i>pos</i>. <br>
	 * <br>
	 */
	public static void render(Texture texture, Point pos)
	{
		render(texture, pos, new Point(0, 0), new Dimension(0, 0), 0, 0, 0);
	}

	/**
	 * <br>
	 * <b>render</b> <br>
	 * <p>
	 * <tt>public static void render(Texture texture, Point pos, Point offset, Dimension size, int rotateClockWise, int flipX, int flipY)</tt>
	 * </p>
	 * Renders the texture. <br>
	 * <br>
	 * 
	 * @param texture
	 *            - The texture to be rendered to display.
	 * @param pos
	 *            - The position in tiles (Slot.SIZE) where the upper left point
	 *            of the texture will be rendered.
	 * @param offset
	 *            - A position offset in pixel.
	 * @param size
	 *            - The desired dimensions of the image in the display in tiles
	 *            (Slot.SIZE).
	 * @param rotatedClockWise
	 *            - The rotation of the image. 0 for normal, + 1 for every 90
	 *            degrees clockwise.
	 * @param flipX
	 *            - TODO
	 * @param flipY
	 *            - TODO
	 * @see org.newdawn.slick.opengl.Texture
	 */
	public static void render(Texture texture, Point pos, Point offset, Dimension size, int rotateClockWise, int flipX,
			int flipY)
	{

		Point renderPos = new Point(pos.getX() * Slot.SIZE + offset.getX(), pos.getY() * Slot.SIZE + offset.getY());

		if (texture == null
				|| !Map.isPointInGrid(new Point(renderPos.getX() / Slot.SIZE, renderPos.getY() / Slot.SIZE)))
			return;

		Point rotates[] = new Point[4];
		rotates[(0 + rotateClockWise) % 4] = new Point(0, 0);
		rotates[(1 + rotateClockWise) % 4] = new Point(size.getWidth() * Slot.SIZE, 0);
		rotates[(2 + rotateClockWise) % 4] = new Point(size.getWidth() * Slot.SIZE, size.getHeight() * Slot.SIZE);
		rotates[(3 + rotateClockWise) % 4] = new Point(0, size.getHeight() * Slot.SIZE);

		Point flips[] = new Point[4];
		flips[0] = new Point((flipX + flipY) % 2, flipY);
		flips[1] = new Point((flips[0].getX() + 1) % 2, flips[0].getY());
		flips[2] = new Point(flips[1].getX(), (flips[1].getY() + 1) % 2);
		flips[3] = new Point((flips[2].getX() + 1) % 2, flips[2].getY());

		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glLoadIdentity();
		glTranslatef(renderPos.getX(), renderPos.getY(), 0);
		glBegin(GL_QUADS);
		for (int i = 0; i < 4; i++)
		{
			glTexCoord2f(flips[i].getX(), flips[i].getY());
			glVertex2f(rotates[i].getX(), rotates[i].getY());
		}
		glEnd();
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
	}

	/**
	 * <br>
	 * <b>render</b> <br>
	 * <p>
	 * <tt>public static void render(Texture texture, Point pos, Point offset, Dimension size</tt>
	 * </p>
	 * Renders the texture. <br>
	 * <br>
	 * 
	 * @param texture
	 *            - The texture to be rendered to display.
	 * @param pos
	 *            - The position in tiles (Slot.SIZE) where the upper left point
	 *            of the texture will be rendered.
	 * @param offset
	 *            - A position offset in pixel.
	 * @param size
	 *            - The desired dimensions of the image in the display in tiles
	 *            (Slot.SIZE).
	 * @see org.newdawn.slick.opengl.Texture
	 */
	public static void render(Texture texture, Point pos, Point offset, Dimension size)
	{
		render(texture, pos, offset, size, 0, 0, 0);
	}

	/**
	 * 
	 * <br>
	 * <b>pointArithmetic</b> <br>
	 * <p>
	 * <tt>public static Point pointArithmetic(int op, Point p1, Point p2)</tt>
	 * </p>
	 * Adds, subtracts, divides or multiplies the points. The resulting point is
	 * equal to: p1 + op*p2 <br>
	 * <br>
	 */
	public static Point pointArithmetic(int op, Point p1, Point p2)
	{
		return new Point(p1.getX() + op * p2.getX(), p1.getY() + op * p2.getY());
	}

	/**
	 * 
	 * <br>
	 * <b>pointComparison</b> <br>
	 * <p>
	 * <tt>public static boolean pointComparison(int op, Point p1, Point p2)</tt>
	 * </p>
	 * Compares two points by the operator <i>op</i>. Operators 0: less than 1:
	 * equals 2: greater than 3: less or equal to 4: greater or equal to Both x
	 * and y must satisfy the condition. <br>
	 * <br>
	 */
	public static boolean pointComparison(int op, Point p1, Point p2)
	{
		switch (op)
		{
		case 0:
			return p1.getX() < p2.getX() && p1.getY() < p2.getY();
		case 1:
			return p1.equals(p2);
		case 2:
			return p1.getX() > p2.getX() && p1.getY() > p2.getY();
		case 3:
			return p1.getX() <= p2.getX() && p1.getY() <= p2.getY();
		case 4:
			return p1.getX() >= p2.getX() && p1.getY() >= p2.getY();
		}
		return false;
	}

	/**
	 * 
	 * <br>
	 * <b>hexID</b> <br>
	 * <p>
	 * <tt>public static String hexID(int id)</tt>
	 * </p>
	 * Converts the integer id to its hex id. <br>
	 * <br>
	 */
	public static String hexID(int id)
	{
		String hexID = Integer.toHexString(id);
		while (hexID.length() != 4)
		{
			hexID = "0" + hexID;
		}
		return hexID;
	}

}
