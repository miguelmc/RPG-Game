package game.features;

import game.scripting.SkillActionManager;
import game.structure.MapManager;
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

	private long time = 0, lastFrameTime = 0;
	private int frame = 0;
	private boolean active = true, stopCall = false;
	private int state = -1;
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
			if(System.currentTimeMillis() > lastFrameTime + skill.getTimePerFrame())
			{
				state++;
				lastFrameTime = System.currentTimeMillis();
			}
			skill.getSprites()[state].render(renderPos.getX() * Slot.SIZE, renderPos.getY() * Slot.SIZE, (facingDir + 3)%4);
			if (state == skill.getSprites().length - 1)
			{
				state = 0;
				playAnimation = false;
				if(stopCall)
					active = false;
			}
		}
	}

	public void play(Point position)
	{
		playAnimation = true;
		Point offset = MapManager.getMap().getOffSet();
		position.setLocation(position.getX() - offset.getX(), position.getY() - offset.getY());
		renderPos = position;
	}

	public void update()
	{
		if (System.currentTimeMillis() > time + skill.getTimePerFrame() && !stopCall)
		{
			time = System.currentTimeMillis();
			try
			{
				engine.put("frame", frame);
				engine.eval(new FileReader("data/skill/" + skill.hexID() + "/script.js"));
			} catch (FileNotFoundException e)
			{
				System.out.println("Unable to find " + "data/skill/" + Integer.toString(skill.id()) + "/script.js");
				stop();
			} catch (ScriptException e)
			{
				e.printStackTrace();
			}
			frame++;
		}
	}

	public void stop()
	{
		stopCall = true;
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