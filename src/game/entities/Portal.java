package game.entities;

import game.scripting.PortalActionManager;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Portal extends Entity {

	private static ScriptEngine engine;
	private static PortalActionManager pm = new PortalActionManager();
	
	public Portal(int id) {
		super(id);
	}
	
	public void run(){
		if(engine == null){
			 engine = new ScriptEngineManager().getEngineByName("JavaScript");	 
		}
		
		pm.setPortal(this);
		engine.put("pm", pm);
				
		try {
			engine.eval(new FileReader("data/portal/" + hexID() + "/script.js"));
		} catch (FileNotFoundException e) {
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

}
