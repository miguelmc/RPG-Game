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

	private double counter = 0;
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
			engine.put("sm", new SkillActionManager(this, facingDir));
		}
	}

	public void render()
	{
		if (playAnimation)
		{
			skill.getSprites()[state].render(renderPos.getX() * Slot.SIZE, renderPos.getY() * Slot.SIZE,
					(facingDir + 1) % 4);
			state++;
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
		if (counter % 6 == 0)
		{
			try
			{
				int step = (int) (counter / 6); // TODO make it time based and not frame base for fps independence
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
		}
		counter++;
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
