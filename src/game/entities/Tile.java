package game.entities;

import game.scripting.PortalActionManager;
import game.util.Util;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * The background for every slot.
 * Simplest implementation of an Entity
 */
public class Tile extends Entity
{
	
	int tileSwitch = -1;
	
	private static ScriptEngine engine;
	
	static
	{
		engine = new ScriptEngineManager().getEngineByName("JavaScript");
		engine.put("pm", new PortalActionManager());
	}
	
	public Tile(int id)
	{
		super(id);
	}

	public void setSwitch(int tileSwitch) {
		this.tileSwitch = tileSwitch;
	}
	
	public void step()
	{
		System.out.println("asdljnasdkjbas");
		if(tileSwitch == -1)
			return;
		
		try
		{
			engine.eval(new FileReader("data/switches/" + Util.hexID(tileSwitch) + ".js"));
		} catch (FileNotFoundException e)
		{
		} catch (ScriptException e)
		{
			e.printStackTrace();
		}
	}
	
}
