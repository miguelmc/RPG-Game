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

public class NPC extends Entity
{

	private String name;
	private static java.util.Map<Integer, String> names = new HashMap<Integer, String>();
	private static NPC npc;
	private static ScriptEngine engine;
	private static NPCConversationManager cm = new NPCConversationManager();

	public static void initialize()
	{

		XMLParser parser = new XMLParser("npc/names.xml");

		List<java.util.Map<String, String>> npcNames = parser.getChildrenAttributes("NPCs");
		for (java.util.Map<String, String> data : npcNames)
		{
			names.put(Integer.parseInt(data.get("id"), 16), data.get("name"));
		}

	}

	public NPC(int id)
	{
		super(id);
		setStrong();
		name = names.get(id);
	}

	public void UIRender()
	{
		Util.useFont("Arial", Font.BOLD, 10, Color.white);
		float xTraslation = (Util.getTextWidth(name) - Slot.SIZE) / 2;
		Util.write(name, ((getY() - getMap().getOffSet().getX()) - getMap().getOffSet().getX()) * Slot.SIZE
				- xTraslation, getY() * Slot.SIZE - getMap().getOffSet().getY() * Slot.SIZE - (float) (Slot.SIZE * .3));
	}

	public void run()
	{
		run(0);
	}

	public void run(int state)
	{
		if (state == -1)
		{
			return;
		}
		if (engine == null)
		{
			engine = new ScriptEngineManager().getEngineByName("JavaScript");
		}
		npc = this;
		cm.setState(state);
		engine.put("cm", cm);

		try
		{
			engine.eval(new FileReader("data/npc/" + hexID() + "/script.js"));
		} catch (FileNotFoundException e)
		{
			cm.sendOk("data/npc/" + hexID() + "/script.js");
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
