package game.util;

import game.util.Renderer.Builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Animation{

	private List<Point> coordinates = new ArrayList<Point>();
	private Dimension imageSize;
	private Iterator<Point> iterator;
	private boolean repeat;
	private final int period;
	private long nextFrame;
		
	public Animation(String xmlDoc, int period, boolean repeat)
	{
		parseCoordinates(xmlDoc);
		this.period = period;
		this.repeat = repeat;
		nextFrame = System.currentTimeMillis() + period;
	}
	
	private void parseCoordinates(String xmlDoc) {
		XMLParser parser = new XMLParser(xmlDoc);
		
		int width = Integer.parseInt(parser.getAttribute("Coordinates", "width"));
		int height = Integer.parseInt(parser.getAttribute("Coordinates", "height"));

		imageSize = new Dimension(width, height);

		List<Map<String, String>> xmlCoordinates = parser.getChildrenAttributes("Coordinates");
		
		for(Map<String, String> map: xmlCoordinates)
			coordinates.add(new Point(Integer.parseInt(map.get("x")), Integer.parseInt(map.get("y"))));
	}

	public void render(Builder builder)
	{
		if(iterator != null)
			if(System.currentTimeMillis() >= nextFrame)		 
				if(iterator.hasNext())
				{
					builder.offset(iterator.next()).imageSize(imageSize.getWidth(), imageSize.getHeight());
					Renderer.render(builder);
					nextFrame = System.currentTimeMillis() + period;
				}else if(repeat) iterator = coordinates.iterator();
	}
		
	public void play()
	{
		iterator = coordinates.iterator();
	}
}
