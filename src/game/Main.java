package game;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import game.entities.Entity;
import game.structure.MapManager;
import game.structure.Slot;
import game.ui.MsgBoxManager;
import game.ui.UserInterface;
import game.ui.window.Window;
import game.util.XMLParser;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Dimension;

/**
 * Game launcher class.
 * Contains the main loop and initializes the game.
 */
public class Main{
	
	public static String NAME = "Game";
	public static Dimension GRIDSIZE, DIM;
	
	public Main(){
		
		loadConfigurations(); //reads game configurations from config xml file
		
		//create window
		try {
			Display.setDisplayMode(new DisplayMode(DIM.getWidth(), DIM.getHeight()));
			Display.setTitle(NAME);
			Display.create();
		} catch (LWJGLException e) {
			System.out.println("Unable to create display");
			System.exit(1); 
		}
		
		//initializes opengl setting and static initialization for game objects
		init();
		
		//Game Loop
		while(!Display.isCloseRequested()){ // as long as close button is not pressed
			
			input(); 
			MapManager.update(); // updates the current map
			
			glClear(GL_COLOR_BUFFER_BIT); // clears the screen
			MapManager.render(); // render the active map
			UserInterface.render(); // renders the interface
			
			Display.update(); //update the screen
			Display.sync(60); // set fps to 60
			
		}
		
		Display.destroy();
		System.exit(0);
	}
	
	private void init(){
				
		//init GL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, DIM.getWidth(), DIM.getHeight(), 0, 1, -1); // set origin to upper-left corner
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND); // enable transparency
    	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // enable transparency
    	
		Entity.initialize(); // reads npc names (there's no xml for each npc so all the names are in a single file)
		
		MapManager.init(); //creates the maps
		UserInterface.init(); //loads fonts and textures for windows
		
	}
	
	private void input() {
		//give input to the map and windows unless the messagebox is active
		
		while(Keyboard.next()){ // checks for a keyboard event
			if(MsgBoxManager.isActive()){
				MsgBoxManager.input();
			}else{
				MapManager.input();
				Window.input();
			}
		}
		
		while(Mouse.next()){ //checks for a mouse event
			Window.mouseInput();
		}
		
	}
	
	private void loadConfigurations() {

		XMLParser parser = new XMLParser("game_config.xml");
		
		NAME = parser.getAttribute("GAME", "name");
		
		GRIDSIZE = new Dimension(Integer.parseInt(parser.getAttribute("Game", "width")),
				Integer.parseInt(parser.getAttribute("Game", "height")));
		
		Slot.SIZE = Integer.parseInt(parser.getAttribute("Game", "tile_size").replace("px", ""));
		
		DIM = new Dimension(Slot.SIZE*GRIDSIZE.getWidth(), Slot.SIZE*GRIDSIZE.getHeight());
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}
