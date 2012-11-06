package game.util;

import game.Main;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Writer {

	public static final int LEFT = 0, CENTER = 1, RIGHT = 2;
	private static Fonts currentFont;
	
	private Writer(){}
	
	public static void write(String text, Point position, int alignment)
	{
		if(!Main.DEBUG)
		{
			currentFont.unicodeFont.drawString((int)(position.getX() - alignment*.5*currentFont.getWidth(text)), position.getY(), text);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public static void write(String text, Point position)
	{
		write(text, position, LEFT);
	}
	
	public static void write(List<String> paragraph, Point position, int maxLength, int alignment)
	{
		for(int i=0; i<paragraph.size() && (i<maxLength || maxLength == -1); i++)
			write(paragraph.get(i), new Point(position.getX(), position.getY() + currentFont.HEIGHT*i), alignment);
	}
	
	public static void write(List<String> paragraph, Point position, int maxLength)
	{
		write(paragraph, position, maxLength, LEFT);
	}
	
	public static void useFont(Fonts font)
	{
		currentFont = font;
	}
	
	public static List<String> toParagraph(String text, int maxLineWidth)
	{
		
		List<String> lines = new ArrayList<String>();
		
		if(Main.DEBUG) return lines;
		
		Queue<String> words = new LinkedList<String>();
		
		for(String word: text.split(" "))
			words.add(word);
						
		while(!words.isEmpty())
		{
			lines.add("");
			while(currentFont.getWidth(lines.get(lines.size() - 1) + words.peek()) <= maxLineWidth && !words.isEmpty())
				lines.set(lines.size() - 1, lines.get(lines.size() - 1) +  words.poll() + " ");
		}
				
		return lines;
	}
	
	public static int fontHeight() {
		if(Main.DEBUG) return 10;
		return currentFont.HEIGHT;
	}
	
	public enum Fonts
	{
		Courier_White_Bold_14("Courier New", Font.BOLD, 14, Color.white),
		Monaco_White_Plain_25("Monaco", Font.PLAIN, 25, Color.white),
		Arial_White_Bold_10("Arial", Font.BOLD, 10, Color.white),
		Arial_Black_Bold_14("Arial", Font.BOLD, 14, Color.black),
		Courier_White_Bold_28("Courier New", Font.BOLD, 28, Color.white);
		
		private UnicodeFont unicodeFont;
		public final int HEIGHT;
		
		@SuppressWarnings("unchecked")
		private Fonts(String name, int style, int size, Color color){
			if(!Main.DEBUG)
			{
				unicodeFont = new UnicodeFont(new Font(name, style, size));
				unicodeFont.addAsciiGlyphs();
				unicodeFont.getEffects().add(new ColorEffect(color));
				try
				{
					unicodeFont.loadGlyphs();
				} catch (SlickException e)
				{
					e.printStackTrace();
				}
							
				HEIGHT = unicodeFont.getHeight("Q");
			}else
				HEIGHT = 10;
		}
		
		public int getWidth(String text)
		{
			if(Main.DEBUG) return 5*text.length();
			return unicodeFont.getWidth(text);
		}
		
	}
	
}
