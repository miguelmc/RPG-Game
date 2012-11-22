package game.util;

import game.util.Renderer.Builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Animation{

	private List<Point> coordinates = new ArrayList<Point>();
	private Iterator<Point> iterator;
	private Builder builder;
	private int timePerFrame;
	private long lastRenderTime = 0;
	private int currentFrame;
	private Dimension imageSize = new Dimension();
	
	public Animation(String xmlDoc, Texture texture)
	{	
		XMLParser parser = new XMLParser(xmlDoc);
		
		timePerFrame = Integer.parseInt(parser.getAttribute("Animation", "timePerFrame"));
		
		imageSize.setWidth(Integer.parseInt(parser.getAttribute("Animation", "imageWidth")));
		imageSize.setHeight(Integer.parseInt(parser.getAttribute("Animation", "imageHeight")));
		
		builder = new Builder(texture, new Point(0, 0), new Dimension(0, 0)).imageSize(imageSize.getWidth(), imageSize.getHeight());
		
		List<Map<String, String>> coordinatesXML = parser.getChildrenAttributes("Animation/Coordinates");
		for(Map<String, String> coordinate: coordinatesXML)
			coordinates.add(new Point(Integer.parseInt(coordinate.get("x")), Integer.parseInt(coordinate.get("y"))));	
		
		currentFrame = coordinates.size();
	}
	
	public void play()
	{
		iterator = coordinates.iterator();
		currentFrame = 0;
		lastRenderTime = System.currentTimeMillis();
	}
	
	public void render(Point position, Dimension size, boolean flipX, int rotation)
	{
		if(iterator != null && currentFrame < coordinates.size())
		{
			Renderer.render(builder
							.setPosition(position)
							.setSize(size)
							.textureOffset(coordinates.get(currentFrame))
							.flipX(flipX)
							.rotate(rotation));
			
			if(System.currentTimeMillis() > lastRenderTime + timePerFrame)
			{
				lastRenderTime = System.currentTimeMillis();
				currentFrame++;
			}
		}
	}
		
	public int currentFrame()
	{
		return currentFrame + 1;
	}
	
	public int totalFrames()
	{
		return coordinates.size();
	}

	public boolean rendering() {
		return currentFrame < coordinates.size();
	}
	
	public Dimension getImageSize()
	{
		return imageSize;
	}
	
}
