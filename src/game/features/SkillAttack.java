package game.features;

import game.scripting.SkillActionManager;
import game.structure.Slot;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.lwjgl.util.Point;

/**
 * An instance based on a skill. It is the actual attack.
 */
public class SkillAttack
{

	private long time = System.currentTimeMillis();
	private double delay = 50;
	private int step = 0;
	private boolean active = true;
	private int state = 0;
	private ScriptEngine engine;
	private Skill skill;
	private boolean playAnimation = false;
	private Point renderPos;
	private int facingDir;

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
		if (playAnimation)
		{
			skill.getSprites()[state++].render(renderPos.getX() * Slot.SIZE, renderPos.getY() * Slot.SIZE, (facingDir + 3) % 4);
			if (state == skill.getSprites().length)
			{
				state = 0;
				playAnimation = false;
			}
		}
	}

	public void play(Point position)
	{
		playAnimation = true;
		renderPos = position;
	}

	public void update()
	{
		// the skill script is called every 6 frames (.1 seconds) and passed the
		// variable "step" to determine how long it has been running
		if (System.currentTimeMillis() > time + delay)
		{
			time = System.currentTimeMillis();
			try
			{
				engine.put("step", step);
				engine.eval(new FileReader("data/skill/" + skill.hexID() + "/script.js"));
			} catch (FileNotFoundException e)
			{
				System.out.println("Unable to find " + "data/skill/" + Integer.toString(skill.id()) + "/script.js");
				stop();
			} catch (ScriptException e)
			{
				e.printStackTrace();
			}
			step++;
		}
	}

	public void stop()
	{
		active = false;
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