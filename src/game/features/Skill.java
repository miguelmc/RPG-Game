package game.features;

import game.entities.superentities.SuperEntity;
import game.structure.GameObject;
import game.util.SubImage;
import game.util.Util;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

/**
 * Contains the properties of a skill. It generates an instance of SkillAttack
 * based on its properties to attack.
 */
public class Skill extends GameObject
{

	private SubImage sprites[]; // each frame of the animation
	private int level = 1, maxLevel, delay;
	private String description, subDescription, name;
	private ArrayList<String[]> variables = new ArrayList<String[]>();
	private ArrayList<SkillAttack> attacks = new ArrayList<SkillAttack>(); // TODO change to a stack?
	private SuperEntity attacker;
	private int timePerFrame;
	private Texture thumbnail;

	public Skill(int id, SuperEntity attacker)
	{
		super(id);
		this.attacker = attacker;

		thumbnail = Util.getTexture("skill/" + hexID() + "/thumbnail.png");
		
		XMLParser parser = new XMLParser("skill/" + hexID() + "/data.xml");
		name = parser.getAttribute("Skill", "name");
		delay = Integer.parseInt(parser.getAttribute("Skill", "delay"));
		description = parser.getAttribute("Skill", "description");
		subDescription = parser.getAttribute("Skill/details", "data");

		// TODO make custom lists in xml

		String levelList = parser.getAttribute("Skill/details/list", "level");
		variables.add(levelList.split(","));
		
		parser.setDocument("skill/" + hexID() + "/coordinates.xml");

		int width = Integer.parseInt(parser.getAttribute("Coordinates", "width"));
		int height = Integer.parseInt(parser.getAttribute("Coordinates", "height"));
		timePerFrame = Integer.parseInt(parser.getAttribute("Coordinates", "timePerFrame"));

		Dimension size = new Dimension(width, height);

		List<Map<String, String>> coordinates = parser.getChildrenAttributes("Coordinates");

		sprites = new SubImage[coordinates.size()];
		Texture spriteSheet = Util.getTexture("skill/" + hexID() + "/texture.png");

		for (int i = 0; i < sprites.length; i++)
		{
			sprites[i] = new SubImage(spriteSheet, new Point(Integer.parseInt(coordinates.get(i).get("x")),
															 Integer.parseInt(coordinates.get(i).get("y"))), size);
		
		}
	}

	public String getDescription()
	{
		return description;
	}

	public int getDelay()
	{
		return delay;
	}

	public String getSubDescription()
	{
		int i = 0;
		while (subDescription.contains("%"))
		{
			int index = subDescription.indexOf("%");
			String newString = subDescription.substring(0, index) + variables.get(i)[level];
			if (index != subDescription.length() - 1)
				newString.concat(subDescription.substring(index + 1));
			subDescription = newString;
			i++;
		}
		subDescription.replace("\\", "%");
		return subDescription;
	}

	public int getMaxLevel()
	{
		return maxLevel;
	}

	public int getLevel()
	{
		return level;
	}

	public void update()
	{
		for (ListIterator<SkillAttack> i = attacks.listIterator(); i.hasNext();)
			if (i.next().isActive())
			{
				i.previous().update(); // updates while the skillattack is active
				i.next();
			} else
				i.remove(); // remove from the list if the skillattack finished
	}

	public void render()
	{
		for (SkillAttack sa : attacks)
			sa.render();
	}

	public String[] getVariables(int i)
	{
		return variables.get(i);
	}

	public SubImage[] getSprites()
	{
		return sprites;
	}

	public void attack()
	{
		attacks.add(new SkillAttack(this));
	}

	public SuperEntity getAttacker()
	{
		return attacker;
	}

	public void stopAll()
	{
		attacks.clear();
	}
	
	public int getTimePerFrame()
	{
		return timePerFrame;
	}
	
	public Texture getThumbnail()
	{
		return thumbnail;
	}
	
	public String getName()
	{
		return name;
	}
}