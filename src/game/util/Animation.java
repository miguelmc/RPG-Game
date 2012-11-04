package game.util;

import game.util.Renderer.Builder;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Animation{

	private Texture texture;
	private List<Point> coordinates;
	private Dimension renderSize, imageSize;
	private Iterator<Point> iterator;
	private Point position;
		
	public Animation(String filePath, Point position, Dimension size)
	{
		this.texture = Util.getTexture(filePath);
		this.position = position;
		renderSize = size;
		parseCoordinates();
	}
	
	private void parseCoordinates() {
		
	}

	public void render()
	{
		if(iterator != null && iterator.hasNext())
		{
			Point point = iterator.next();
			
			Renderer.render(new Builder(
					texture,
					null,
					renderSize));
			
			//TODO render
			
		}
	}
	
	public void play()
	{
		iterator = coordinates.iterator();
	}
		
}
