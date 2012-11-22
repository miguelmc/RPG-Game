package game.features;

import game.entities.superentities.SuperEntity;
import game.structure.GameObject;
import game.util.Util;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.newdawn.slick.opengl.Texture;

/**
 * Contains the properties of a skill. It generates an instance of SkillAttack
 * based on its properties to attack.
 */
public class Skill extends GameObject
{

	private Texture texture;
	private int level = 0, maxLevel, delay;
	private String description, name;
	private List<SkillAttack> attacks = new ArrayList<SkillAttack>();
	private float damage, damageIncrease;
	private SuperEntity attacker;
	private int timePerFrame;
	private Texture thumbnail;
	private long lastAttack = 0;
	private boolean delayed = false;
	private int mp;

	public Skill(int id, SuperEntity attacker)
	{
		super(id);
		this.attacker = attacker;

		thumbnail = Util.getTexture("skill/" + hexID() + "/thumbnail.png");
		
		XMLParser parser = new XMLParser("skill/" + hexID() + "/data.xml");
		name = parser.getAttribute("Skill", "name");
		delay = Integer.parseInt(parser.getAttribute("Skill", "delay"));
		description = parser.getAttribute("Skill", "description");
		maxLevel = Integer.parseInt(parser.getAttribute("Skill", "maxLevel"));
		mp = Integer.parseInt(parser.getAttribute("Skill", "mp"));
		
		damage = Float.parseFloat(parser.getAttribute("Skill/Damage", "damage"));
		damageIncrease = Float.parseFloat(parser.getAttribute("Skill/Damage", "increase"));
		
		texture = Util.getTexture("skill/" + hexID() + "/texture.png");
		
		if(id == 0x0701) raiseLevel();
		
	}

	public String getDescription()
	{
		return description;
	}

	public int getDelay()
	{
		return delay;
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

	public boolean attack()
	{
		if(System.currentTimeMillis() > lastAttack + getDelay())
		{
			attacks.add(new SkillAttack(this));
			lastAttack = System.currentTimeMillis();
			delayed = false;
			return true;
		}
		
		return false;
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
	
	public Texture getTexture()
	{
		return texture;
	}
	
	public boolean hasAttacks()
	{
		return !attacks.isEmpty();
	}
	
	public float getDamage()
	{
		return damage + (getLevel() - 1) * damageIncrease;
	}

	public boolean raiseLevel() {
		if(getLevel() == getMaxLevel())
			return false;
		
		level++;
		return true;
	}
	
	public void delaySkill(int time)
	{
		if(!delayed)
		{
			lastAttack += time;
			delayed = true;
		}
	}

	public int getMP() {
		return mp;
	}
	
}