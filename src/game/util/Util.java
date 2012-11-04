package game.util;

import game.entities.superentities.SuperEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.util.Point;
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
		while (hexID.length() < 4)
		{
			hexID = "0" + hexID;
		}
		return hexID;
	}

}
