package game.entities;

import game.scripting.NPCConversationManager;
import game.structure.Slot;
import game.ui.MsgBoxManager;
import game.util.Util;
import game.util.Writer;
import game.util.Writer.Fonts;
import game.util.XMLParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.lwjgl.util.Point;

/**
 * Stands for Non-Player Character.
 * The player can chat with the character and the scripts are defined in javascript.
 * Serve as an interface for quests.
 */
public class NPC extends Entity implements Runnable
{

	private String name;
	private static java.util.Map<Integer, String> names = new HashMap<Integer, String>();
	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	private static NPCConversationManager cm = new NPCConversationManager();

	static{
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
		Writer.useFont(Fonts.Arial_White_Bold_10);
		Point position = new Point((int)((getPositionInGrid().getX()+.5)*Slot.SIZE), getPositionInGrid().getY()*Slot.SIZE - Writer.fontHeight());
		Writer.write(name, position, Writer.CENTER);
	}

	public void run()
	{
		NPCConversationManager.setNPC(this);
			
		engine.put("cm", cm);
				
		try
		{
			engine.eval(new FileReader("data/npc/" + Util.hexID(id()) + "/script.js"));
		} catch (FileNotFoundException e)
		{
			MsgBoxManager.sendMessage("Script not found: data/npc/" + Util.hexID(id()) + "/script.js", MsgBoxManager.OK);
		} catch (ScriptException e)
		{
			e.printStackTrace();
		}
	}



}
