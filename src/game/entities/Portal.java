package game.entities;

import game.scripting.PortalActionManager;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Entity used by the player to change map.
 * Its behaviour is defined by a script, for example for checking if the player can access a map and to check for the player
 * facing direction.
 */
public class Portal extends Entity
{

	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");;
	private static PortalActionManager pm = new PortalActionManager();
	
	public Portal(int id)
	{
		super(id);
	}

	public void run()
	{
		pm.setPortal(this);
		engine.put("pm", pm);

		try
		{
			engine.eval(new FileReader("data/portal/" + hexID() + "/script.js"));
		} catch (FileNotFoundException e)
		{
		} catch (ScriptException e)
		{
			e.printStackTrace();
		}
	}

}
