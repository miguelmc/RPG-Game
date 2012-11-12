package game.entities.item;

import game.scripting.ItemUsageManager;
import game.scripting.NPCConversationManager;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * An item that can be used by the player.
 * The behavior of the item is defined by a script.
 */
public class UsableItem extends Item
{

	private static ScriptEngine engine;
	
	static
	{
		engine = new ScriptEngineManager().getEngineByName("JavaScript");
		engine.put("im", new ItemUsageManager());
	}

	public UsableItem(int id, int amount)
	{
		super(id, amount);
	}

	public UsableItem(int id)
	{
		this(id, 1);
	}

	public void use()
	{
		try
		{
			engine.eval(new FileReader("data/item/use/" + hexID() + "/script.js"));
		} catch (FileNotFoundException e)
		{
			new NPCConversationManager().sendOk("Script not found: " + hexID());
		} catch (ScriptException e)
		{
			e.printStackTrace();
		}
	}

}
