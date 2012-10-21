package game.entities;

import game.scripting.NPCConversationManager;
import game.structure.Slot;
import game.util.Util;
import game.util.XMLParser;

import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Stands for Non-Player Character.
 * The player can chat with the character and the scripts are defined in javascript.
 * Serve as an interface for quests.
 */
public class NPC extends Entity
{

	private String name;
	private static java.util.Map<Integer, String> names = new HashMap<Integer, String>();
	private static NPC npc; //reference to the npc whose script is being executed.
	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");;
	private static NPCConversationManager cm = new NPCConversationManager();

	static{
		//There's no xml file for every npc, all the names are stored in a single file mapped with their ids.
				XMLParser parser = new XMLParser("npc/names.xml");

				List<java.util.Map<String, String>> npcNames = parser.getChildrenAttributes("NPCs");
				for (java.util.Map<String, String> data : npcNames)
					names.put(Integer.parseInt(data.get("id"), 16), data.get("name"));
	}
	
	public NPC(int id)
	{
		super(id);
		setStrong();
		name = names.get(id);
	}

	public void UIRender()
	{
		//Render NPC name
		Util.useFont("Arial", Font.BOLD, 10, Color.white);
		float xTraslation = (Util.getTextWidth(name) - Slot.SIZE) / 2;
		Util.write(name, ((getX() - getMap().getOffSet().getX())) * Slot.SIZE
				- xTraslation, getY() * Slot.SIZE - getMap().getOffSet().getY() * Slot.SIZE - (float) (Slot.SIZE * .3));
	}

	public void run()
	{
		run(0); //run script with initial state = 0
	}

	public void run(int state)
	{
		if (state == -1) //state for closing the conversation
			return;
		
		npc = this;
		cm.setState(state);
		engine.put("cm", cm);

		try
		{
			engine.eval(new FileReader("data/npc/" + hexID() + "/script.js"));
		} catch (FileNotFoundException e)
		{
			cm.sendOk("Script not found: data/npc/" + hexID() + "/script.js");
		} catch (ScriptException e)
		{
			e.printStackTrace();
		}
	}

	public static NPC getNpc()
	{
		return npc;
	}

}
