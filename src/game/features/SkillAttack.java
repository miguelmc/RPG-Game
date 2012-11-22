package game.features;

import game.scripting.SkillActionManager;
import game.structure.MapManager;
import game.structure.Slot;
import game.util.Animation;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

/**
 * An instance based on a skill. It is the actual attack.
 */
public class SkillAttack
{

	private int step = 0;
	private boolean active = true;
	private ScriptEngine engine;
	private Skill skill;
	private Point renderPos;
	private int facingDir;
	private Animation animation;
	private boolean stop = false;
	private long lastAttack;

	public SkillAttack(Skill s)
	{
		skill = s;
		facingDir = skill.getAttacker().getFacingDir();

		if (engine == null)
		{
			engine = new ScriptEngineManager().getEngineByName("JavaScript");
			engine.put("sm", new SkillActionManager(this));
		}
		
	}

	public void render()
	{
		if(animation != null)
		{
			System.out.println(animation.currentFrame());
			animation.render(new Point(renderPos.getX()*Slot.SIZE, renderPos.getY()*Slot.SIZE), new Dimension(Slot.SIZE, Slot.SIZE), false, (facingDir+3)%4);
			if(stop && animation.currentFrame() == animation.totalFrames())
				active = false;
		}
	}

	public void play(Point position)
	{
		animation = new Animation("skill/" + skill.hexID() + "/animation.xml", skill.getTexture());
		animation.play();
		Point offset = MapManager.getMap().getOffSet();
		position.setLocation(position.getX() - offset.getX(), position.getY() - offset.getY());
		renderPos = position;
	}

	public void update()
	{
		if(!stop && System.currentTimeMillis() >= lastAttack + 10) //EXECUTE SCRIPT EVERY 10 MILISECONDS
		{
			lastAttack = System.currentTimeMillis();
			try
			{
				engine.put("step", step++);
				engine.eval(new FileReader("data/skill/" + skill.hexID() + "/script.js"));
			} catch (FileNotFoundException e)
			{
				System.out.println("Unable to find " + "data/skill/" + Integer.toString(skill.id()) + "/script.js");
				stop();
			} catch (ScriptException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void stop()
	{
		stop = true;
	}

	public Skill getSkill()
	{
		return skill;
	}

	public boolean isActive()
	{
		return active;
	}

}